package org.techtown.nuzak;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.pedro.library.AutoPermissions;

import org.checkerframework.checker.units.qual.A;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.Tensor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;
import java.text.SimpleDateFormat;

import static android.app.Activity.RESULT_OK;

public class Fragment2 extends Fragment {

    ViewGroup rootView;
    Button uploadButton;

    ImageView uploadedPhoto;
    TextView taleTitle;
    String imageName;
    String text;

    private final static String TAG = "MainActivity";

    private ImageView mContentImageView;
    private ImageView mStyleImageView;

    private Bitmap mContentImage;
    private Bitmap mStyleImage;

    private RadioGroup mRadioGroup;
    private Button mTransferButton;

    private final static int REQUEST_CONTENT_IMG = 1;
    private final static int REQUEST_STYLE_IMG = 2;

    private final static int STYLE_IMG_SIZE = 256;
    private final static int CONTENT_IMG_SIZE = 384;
    private final static int DIM_BATCH_SIZE = 1;
    private final static int DIM_PIXEL_SIZE = 3;

    private final static int USING_CPU = 1;
    private final static int USING_GPU = 2;
    private final static int USING_NNAPI = 3;

    private int mDelegationMode = USING_CPU;

    private final static String PREDICT_MODEL = "style_predict.tflite";
    private final static String TRANSFORM_MODE = "style_transform.tflite";

    private final static int IMAGE_MEAN = 0;
    private final static int IMAGE_STD = 255;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        taleTitle = rootView.findViewById(R.id.taleTitle);
/*
        uploadButton = rootView.findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                launcher.launch(intent);
            }
        });
*/
        initViews();
        AutoPermissions.Companion.loadAllPermissions(getActivity(), 101);
        return rootView;
    }

    /*ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>()
            {
                @Override
                public void onActivityResult(ActivityResult result)
                {
                    if (result.getResultCode() == RESULT_OK)
                    {
                        Intent intent = result.getData();
                        Uri uri = intent.getData();
                        //imageview.setImageURI(uri);
                        Glide.with(getActivity())
                                .load(uri)
                                .into(uploadedPhoto);
                    }
                }
            });
    */

    private void initViews() {
        // init content and style imageview
        mContentImageView = rootView.findViewById(R.id.content_img);
        mContentImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CONTENT_IMG);
            }
        });

        mStyleImageView = rootView.findViewById(R.id.style_img);
        mStyleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_STYLE_IMG);
            }
        });


        // init transfer button
        mTransferButton = rootView.findViewById(R.id.bt_transfer);
        mTransferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContentImage == null) {
                    Toast.makeText(getActivity(), "사진이 선택되지 않았습니다", Toast. LENGTH_SHORT).show();
                    return;
                }

                // init some key things
                Interpreter predictInterpreter;
                Interpreter transformInterpreter;

                Interpreter.Options predictOptions = new Interpreter.Options();
                //predictOptions.setNumThreads(5);

                try {
                    // init two interpreter instances: style predict and style transform
                    predictInterpreter = new Interpreter(
                            loadModelFile(getActivity(), PREDICT_MODEL), predictOptions);

                    Interpreter.Options transformOptions = new Interpreter.Options();
                    transformInterpreter = new Interpreter(
                            loadModelFile(getActivity(), TRANSFORM_MODE), transformOptions);

                    ByteBuffer bottleneck = runPredict(predictInterpreter, mStyleImage);

                    Bitmap mTransferredImage =
                            runTransform(transformInterpreter, mContentImage, bottleneck);

                    saveImage(mTransferredImage);

                    final String urlStr = "http://c9e6-35-194-248-251.ngrok.io/story/" + taleTitle.getText().toString();

                    try{
                        Thread a = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                request(urlStr);
                            }
                        });

                        a.start();
                        a.join();
                    }
                    catch(Exception e){
                        System.out.println("예외가 발생하였습니다.");
                    }
                    Intent intent = new Intent(getActivity(), TransferredActivity.class);
                    intent.putExtra("Title", taleTitle.getText().toString());
                    intent.putExtra("FileName", imageName);
                    intent.putExtra("Text", text);

                    startActivity(intent);



                } catch (IOException ex) {
                    Log.e(TAG, "Init interpreter failed with IOException!" + ex);
                }
            }
        });
    }

    private void saveImage(Bitmap finalBitmap) {
        String dataDir = getActivity().getFilesDir().toString();
        File myDir = new File(dataDir);
        myDir.mkdirs();
        imageName = getDate() + taleTitle.getText().toString()+".jpg";
        File file = new File(myDir, imageName);
        if (file.exists()) file.delete();
        Log.i(TAG, "save transferred image to " + dataDir + "/"+ imageName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");
        return mFormat.format(date);
    }

    private ByteBuffer runPredict(Interpreter tflite, Bitmap styleImage) {
        TensorImage inputTensorImage = getInputTensorImage(tflite, styleImage);

        Tensor outputTensor = tflite.getOutputTensor(/* outputTensorIndex */ 0);
        TensorBuffer outputTensorBuffer
                = TensorBuffer.createFixedSize(outputTensor.shape(), outputTensor.dataType());

        long startTime = SystemClock.elapsedRealtime();
        tflite.run(inputTensorImage.getBuffer(), outputTensorBuffer.getBuffer());
        long timeInterval = SystemClock.elapsedRealtime() - startTime;

        return outputTensorBuffer.getBuffer();
    }

    private Bitmap runTransform(Interpreter tflite, Bitmap contentImage, ByteBuffer bottleneck) {
        TensorImage inputTensorImage = getInputTensorImage(tflite, contentImage);

        Object[] inputs = new Object[2];
        inputs[0] = inputTensorImage.getBuffer();
        inputs[1] = bottleneck;

        int[] outputShape =
                new int[] {DIM_BATCH_SIZE, CONTENT_IMG_SIZE, CONTENT_IMG_SIZE, DIM_PIXEL_SIZE};
        DataType outputDataType = tflite.getOutputTensor(/* outputTensorIndex */ 0).dataType();
        TensorBuffer outputTensorBuffer = TensorBuffer.createFixedSize(outputShape, outputDataType);
        Map<Integer, Object> outputs = new HashMap<>();
        outputs.put(0, outputTensorBuffer.getBuffer());

        long startTime = SystemClock.elapsedRealtime();
        tflite.runForMultipleInputsOutputs(inputs, outputs);
        long timeInterval = SystemClock.elapsedRealtime() - startTime;

        return convertOutputToBmp(outputTensorBuffer.getFloatArray());
    }

    private TensorImage getInputTensorImage(Interpreter tflite, Bitmap inputBitmap) {
        DataType imageDataType = tflite.getInputTensor(/* imageTensorIndex */0).dataType();
        TensorImage inputTensorImage = new TensorImage(imageDataType);
        inputTensorImage.load(inputBitmap);

        ImageProcessor imageProcessor =
                new ImageProcessor.Builder().add(new NormalizeOp(IMAGE_MEAN, IMAGE_STD)).build();

        return imageProcessor.process(inputTensorImage);
    }

    private Bitmap convertOutputToBmp(float[] output) {
        Bitmap result = Bitmap.createBitmap(
                CONTENT_IMG_SIZE, CONTENT_IMG_SIZE, Bitmap.Config.ARGB_8888);
        int[] pixels = new int[CONTENT_IMG_SIZE * CONTENT_IMG_SIZE];
        int a = 0xFF << 24;
        for (int i = 0, j = 0; j < output.length; i++) {
            int r = (int)(output[j++] * 255.0f);
            int g = (int)(output[j++] * 255.0f);
            int b = (int)(output[j++] * 255.0f);
            pixels[i] = (a | (r << 16) | (g << 8) | b);
        }
        result.setPixels(pixels, 0, CONTENT_IMG_SIZE, 0, 0, CONTENT_IMG_SIZE, CONTENT_IMG_SIZE);
        return result;
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity, String modePath) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(modePath);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(uri));
/*
                bitmap = preProcessBitmap(bitmap,
                        requestCode == REQUEST_CONTENT_IMG ? CONTENT_IMG_SIZE : STYLE_IMG_SIZE);
  */
                bitmap = preProcessBitmap(bitmap, STYLE_IMG_SIZE);
                mStyleImageView.setImageBitmap(bitmap);
                mStyleImage = bitmap;

                if (requestCode == REQUEST_CONTENT_IMG) {
                    bitmap = preProcessBitmap(bitmap, CONTENT_IMG_SIZE);
                    mContentImageView.setImageBitmap(bitmap);
                    mContentImage = bitmap;
                } else {
                    mStyleImageView.setImageBitmap(bitmap);
                    mStyleImage = bitmap;
                }
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Not found input image: " + uri.toString());
            }
        }
    }

    private Bitmap preProcessBitmap(Bitmap bitmap, int size) {
        return Bitmap.createScaledBitmap(bitmap, size, size, false);
    }


    public void request(String urlStr) {
       StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(urlStr);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(10000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);

                int resCode = conn.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line = null;
                while (true) {
                    line = reader.readLine();
                    if (line == null) {
                        break;
                    }

                    output.append(line + "\n");
                }
                reader.close();
                conn.disconnect();
            }
        } catch (Exception ex) {
            System.out.println("예외 발생함 : " + ex.toString());
        }
        System.out.println(output.toString());
        text = output.toString();
    }
}
package org.techtown.nuzak;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.File;

public class Layout1View extends LinearLayout {
    TextView textView;
    ImageView imageView;
    Button readButton;

    Context context;

    int id;

    public Layout1View(Context context) {
        super(context);

        init(context);
    }

    public Layout1View(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.layout1, this, true);

        textView = (TextView) findViewById(R.id.title);
        imageView = (ImageView) findViewById(R.id.imageView);
        readButton = (Button) findViewById(R.id.readButton);

        this.context = context;
    }

    public void setTitle(String name) {
        textView.setText(name);
    }

    public void setImage(String image) {
        String dataDir = getContext().getFilesDir().toString();
        File myDir = new File(dataDir);
        File file = new File(myDir, image);

        Bitmap imageBmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        imageView.setImageBitmap(imageBmp);
    }

    public void setStoryId(int id){
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StoryActivity.class);
                intent.putExtra("id", id+1);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);
            }
        });
    }
}

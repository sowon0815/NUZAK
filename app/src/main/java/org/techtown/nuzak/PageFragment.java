package org.techtown.nuzak;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.nuzak.R;

import java.io.File;
import java.util.Locale;

public class PageFragment extends Fragment implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;
    private TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_fragment, container, false);

        tts = new TextToSpeech(getActivity(), this);

        textView = rootView.findViewById(R.id.textView);
        ImageView imageView = rootView.findViewById(R.id.imageView);

        Bundle bundle = getArguments();

        char [] text = bundle.getCharArray("text");
        textView.setText(text, 0, text.length);

        imageView.setImageResource(bundle.getInt("image"));

        Button playButton = rootView.findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) // LOLLIPOP이상 버전에서만 실행 가능
            @Override
            public void onClick(View v){
                speakOut();
            }
        });
        Button stopButton = rootView.findViewById(R.id.stopButton);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) // LOLLIPOP이상 버전에서만 실행 가능
            @Override
            public void onClick(View v) {
                if (tts != null) tts.stop();
            }
        });

        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speakOut(){
        CharSequence text = textView.getText();
        tts.setPitch((float)1); // 음성 톤 높이 지정
        tts.setSpeechRate((float)0.85); // 음성 속도 지정

        // 첫 번째 매개변수: 음성 출력을 할 텍스트
        // 두 번째 매개변수: 1. TextToSpeech.QUEUE_FLUSH - 진행중인 음성 출력을 끊고 이번 TTS의 음성 출력
        //                 2. TextToSpeech.QUEUE_ADD - 진행중인 음성 출력이 끝난 후에 이번 TTS의 음성 출력
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "id1");
    }

    @Override
    public void onDestroy() {
        if(tts != null){ // 사용한 TTS객체 제거
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onInit(int status) { // OnInitListener를 통해서 TTS 초기화
        if(status == TextToSpeech.SUCCESS){
            int result = tts.setLanguage(Locale.ENGLISH); // TTS언어 한국어로 설정

            if(result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA){
                Log.e("TTS", "This Language is not supported");
            }else{
//                speak_out.setEnabled(true);
//                speakOut();// onInit에 음성출력할 텍스트를 넣어줌
            }
        }else{
            Log.e("TTS", "Initialization Failed!");
        }
    }
}
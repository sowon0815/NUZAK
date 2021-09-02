package org.techtown.nuzak;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import static androidx.core.content.ContextCompat.startActivity;


public class Layout1 extends LinearLayout {
    ImageView imageView;
    TextView textView;
    Button readButton;

    Bundle bundle;

    public Layout1(Context context) {
        super(context);
        init(context);
    }

    public Layout1(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.title);
        readButton = findViewById(R.id.readButton);
    }

    //또는 ... setStory 함수로 합쳐서 아래 함수 둘 다 한번에 설정하기
    public void setImage(int resId){
        imageView.setImageResource(resId);
    }

    public void setTitle(String title){
        textView.setText(title);
    }

    public void showStory(final int resId){
        readButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 확인할것
                // show text
                Intent intent = new Intent(getContext().getApplicationContext(), StoryActivity.class);
                intent.putExtra("id", resId);
                startActivity(getContext(), intent, null);

            }
        });
    }

}
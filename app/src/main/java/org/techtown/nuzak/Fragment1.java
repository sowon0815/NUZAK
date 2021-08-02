package org.techtown.nuzak;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.nuzak.R;

public class Fragment1 extends Fragment {

    org.techtown.nuzak.Layout1 layout1;
    Button readButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        layout1 = rootView.findViewById(R.id.layout1);
        layout1.setImage(R.drawable.ic_baseline_add_24);
        layout1.setTitle("아보카도");

        readButton = rootView.findViewById(R.id.readButton);
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), StoryActivity.class);
                //SimpleData data = new SimpleData(100, "Hello Android");
                //intent.putExtra("data", data); //data라는 key값에 대해 data 객체를 넣겠다
                startActivity(intent);
            }
        });

        return rootView;
    }

}
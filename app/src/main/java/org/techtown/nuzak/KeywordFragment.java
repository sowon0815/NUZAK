package org.techtown.nuzak;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.nuzak.R;

import java.io.File;

public class KeywordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.keyword_fragment, container, false);

        TextView textView = rootView.findViewById(R.id.keyword_textView);

        Bundle bundle = getArguments();

        char [] text = bundle.getCharArray("text");
        textView.setText(text, 0, text.length);

        return rootView;
    }
}
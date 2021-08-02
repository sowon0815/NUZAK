package org.techtown.nuzak;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.techtown.nuzak.R;

public class PageFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.page_fragment, container, false);

        TextView textView = rootView.findViewById(R.id.textView);
        ImageView imageView = rootView.findViewById(R.id.imageView);

        Bundle bundle = getArguments();

        char [] text = bundle.getCharArray("text");
        textView.setText(text, 0, text.length);

        imageView.setImageResource(bundle.getInt("image"));



        return rootView;
    }
}
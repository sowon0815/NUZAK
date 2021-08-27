package org.techtown.nuzak;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.io.File;

public class CoverFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.cover_fragment, container, false);

        TextView textView = rootView.findViewById(R.id.TitleView);
        ImageView imageView = rootView.findViewById(R.id.CoverImageView);

        Bundle bundle = getArguments();

        char [] text = bundle.getCharArray("text");
        textView.setText(text, 0, text.length);

        String imagePath = bundle.getString("imagePath");

        String dataDir = getActivity().getFilesDir().toString();
        File myDir = new File(dataDir);
        File file = new File(myDir, imagePath);

        Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
        imageView.setImageBitmap(image);

        return rootView;
    }

}

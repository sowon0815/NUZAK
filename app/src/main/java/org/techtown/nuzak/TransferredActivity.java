/**
 * Copyright 2019-2020 dpthinker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.techtown.nuzak;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.*;
import java.text.SimpleDateFormat;

public class TransferredActivity extends AppCompatActivity {
    Intent intent;
    Button finishButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transferred);

        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //이제 작성해야함
                //이거 누르면 카드 하나 추가하는 걸로...
                //아니면 그냥 돌아가는 거라도!

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        intent = getIntent();
        
        String dataDir = getFilesDir().toString();
        File myDir = new File(dataDir);

        String fileName = intent.getExtras().getString("FileName");
        String Title = intent.getExtras().getString("Title");
        String Text = intent.getExtras().getString("Text");

        File file = new File(myDir, fileName);

        Bitmap transferredBmp = BitmapFactory.decodeFile(file.getAbsolutePath());

        TextView titleView = findViewById(R.id.titleView);
        ImageView transferredImgView = findViewById(R.id.transferred_img);
        TextView creationDate = findViewById(R.id.creationDate);

        titleView.setText(Title);
        transferredImgView.setImageBitmap(transferredBmp);
        creationDate.setText(fileName);

        insertStory(Title, Text, fileName);
    }

    public void insertStory(String title, String text, String image){
        DBOpenHelper dbHelper = new DBOpenHelper(this);

        dbHelper.insertColumn(title, text, image);
    }

}

package org.techtown.nuzak;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import org.techtown.nuzak.PageFragment;
import org.techtown.nuzak.R;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class StoryActivity  extends AppCompatActivity {
    private ViewPager2 pager;
    private ScreenSlidePagerAdapter pageAdapter;

    ArrayList<char []> textArray;
    ArrayList<Integer> imageArray;

    Bundle bundle;

    String title;
    String text;
    String image;

    Button returnButton;

    DBOpenHelper dbHelper;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        intent = getIntent();
        int id = intent.getExtras().getInt("id");

        dbHelper = new DBOpenHelper(this);

        Story storyData = dbHelper.getStoryData(id);

        imageArray = new ArrayList<Integer>();
        textArray = new ArrayList<char []>();

        this.createImage();
        this.createText(storyData.getText());

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        pageAdapter = new ScreenSlidePagerAdapter(this);

        CoverFragment coverFragment = createCover(storyData.getTitle(), storyData.getImage()); //표지 fragment, 동화 제목과 이미지 파일명 전달
        pageAdapter.addItem(coverFragment);

        int bgImage = (int) (Math.random() * 4); //배경 이미지 4가지 중 랜덤으로 고르기

        for(int i=0;i<textArray.size();i++) {
            PageFragment fragment = new PageFragment();

            bundle = new Bundle(); //프래그먼트에 이미지랑 텍스트 정보 전달할 번들
            bundle.putCharArray("text", textArray.get(i));
            bundle.putInt("image", imageArray.get(bgImage));

            fragment.setArguments(bundle);

            pageAdapter.addItem(fragment); //페이지 추가
        }

        String keyword = storyData.getKeyword();
        KeywordFragment keyWordFragment = createKeyword(keyword);
        pageAdapter.addItem(keyWordFragment);

        pager.setAdapter(pageAdapter);

        returnButton = findViewById(R.id.returnButton);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }

    void createText(String text){
        StringTokenizer st = new StringTokenizer(text, "\n");

        String s = "";
        int count = 0;
        int token = st.countTokens();
        int line = 1;

        for(int i=0;i<token;i++){
            count=0;
            s = "";
            for(int j=0;j<line;j++){
                count++;
                s+=st.nextToken();
            }
            this.textArray.add(s.toCharArray());
        }

        if(0<count&&count<line) this.textArray.add(s.toCharArray());
    }

    void createImage(){
        this.imageArray.add(R.drawable.page1);
        this.imageArray.add(R.drawable.page2);
        this.imageArray.add(R.drawable.page3);
        this.imageArray.add(R.drawable.page4);
    }

    static class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        ArrayList<Fragment> items = new ArrayList<>();

        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        public void addItem(Fragment item) {
            items.add(item);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }
    CoverFragment createCover(String Title, String image){ //표지 fragment 만드는 메서드
        CoverFragment coverFragment = new CoverFragment();

        bundle = new Bundle(); //프래그먼트에 이미지랑 텍스트 정보 전달할 번들

        bundle.putCharArray("text", Title.toCharArray());
        bundle.putString("imagePath", image);

        coverFragment.setArguments(bundle);

        return coverFragment;
    }
    KeywordFragment createKeyword(String keyword){
        KeywordFragment keywordFragment = new KeywordFragment();

        StringTokenizer st = new StringTokenizer(keyword, ",");

        String s = "";
        String temp;

        while(st.hasMoreTokens()){
            temp = st.nextToken();
            if(temp.charAt(0) == ' ') temp = temp.substring(1);
            else if(temp.charAt(temp.length()-1) == ' ') temp = temp.substring(0, temp.length()-1);

            s += temp+"\n";
        }

        bundle = new Bundle();
        bundle.putCharArray("text", s.toCharArray());

        keywordFragment.setArguments(bundle);

        return keywordFragment;
    }
}
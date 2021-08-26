package org.techtown.nuzak;

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

import java.util.ArrayList;

public class StoryActivity  extends AppCompatActivity {
    private ViewPager2 pager;
    private ScreenSlidePagerAdapter pageAdapter;
    ArrayList<Integer> image;
    ArrayList<char []> text;
    Bundle bundle;

    Button returnButton;

    DBOpenHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);

        dbHelper = new DBOpenHelper(this);

        image = new ArrayList<>();
        text = new ArrayList<>();

        this.createTextImage();

        pager = findViewById(R.id.pager);
        pager.setOffscreenPageLimit(2);

        pageAdapter = new ScreenSlidePagerAdapter(this);

        for(int i=0;i<text.size();i++) {
            PageFragment fragment = new PageFragment();

            bundle = new Bundle(); //프래그먼트에 이미지랑 텍스트 정보 전달할 번들
            bundle.putCharArray("text", text.get(i));
            bundle.putInt("image", image.get(i%4));

            fragment.setArguments(bundle);

            pageAdapter.addItem(fragment);
        }

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

    void createTextImage(){
        String s="One day an honest lumberjack was chopping a tree in a forest.\n" +
                "“Chop! Chop!”\n" +
                "Unfortunately, his ax flew out of his hand and into the pond.\n" +
                "“I can’t live on without my ax. How can I work?”\n" +
                "He was so upset that he cried.\n" +
                "The God of the pond knew he was a good man and wanted to help him.\n" +
                "“Is this your ax?”\n" +
                "“Let me see! That is a silver ax. Sorry, that’s not mine.”\n" +
                "The Lumberjack was so disappointed.\n";
        this.text.add(s.toCharArray());

        s="The God of the pond showed him another ax.\n" +
                "“Is this your ax?”\n" +
                "“Mmm, let me see! That is a golden ax. Sorry, that’s not mine, either.”\n" +
                "“Please just find MY ax. I need it to work,” the Lumberjack begged.\n" +
                "Finally, the God of the pond showed him an old rusty iron ax.\n" +
                "“Is this your ax?”\n" +
                "“Yes, yes, yes! That’s an iron ax. That’s mine.”\n" +
                "He was so happy to find his old iron ax.\n";
        this.text.add(s.toCharArray());

        s="The God of the pond was so happy with his honesty that he gave him a reward.\n" +
                "“Hmm, you are such an honest man. You can have all three axes.”\n" +
                "“Thank you so much. Now, I can get more work done.”\n" +
                "A dishonest man living in the same village heard the news and visited the Lumberjack.\n" +
                "“You are so lucky. Where is that pond?”\n" +
                "“It’s over there in the woods.”\n" +
                "He ran off quickly, thinking about gold and silver axes.\n";
        this.text.add(s.toCharArray());

        s="The man found the pond and began chopping the tree.\n" +
                "“Chop! Chop! Now it’s time to throw my ax Into the pond.”\n" +
                "He waited and waited.\n" +
                "“Oh, hurry up! Just come out quickly and give me my reward.”\n" +
                "The God of the pond appeared and asked, “Yawn! Who woke me up again? Is this silver ax yours?”\n" +
                "“Yes, yes, yes! That is mine.”\n" +
                "“How about this golden ax?”\n" +
                "“Yes, yes, yes! Of course, that is mine, too,” the Man shouted quickly.\n";
        this.text.add(s.toCharArray());

        s="“Well, maybe this is your ax, too?”\n" +
                "The God of the pond showed him the iron ax. It was his ax.\n" +
                "“Oh, yeah! That is mine, too. All three axes are mine.”\n" +
                "The Dishonest Man smiled.\n" +
                "“Go away, you greedy liar! I have nothing for you.”\n" +
                "The God of the pond was angry with him for lying and disappeared in the smoke.\n";
        this.text.add(s.toCharArray());

        s= "“Oh, no! That’s not fair. Just give me back my old iron ax! That one is not yours.”\n" +
                "The Man begged and begged, but the God of the pond never appeared to him again.\n" +
                "Because he was dishonest, the Man even lost his own ax.\n";
        this.text.add(s.toCharArray());

        this.image.add(R.drawable.page1);
        this.image.add(R.drawable.page2);
        this.image.add(R.drawable.page3);
        this.image.add(R.drawable.page4);
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

}
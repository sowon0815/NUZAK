package org.techtown.nuzak;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.nuzak.R;

import java.util.ArrayList;


public class Fragment1 extends Fragment {

    org.techtown.nuzak.Layout1 layout1;
    Button readButton;

    String dbname = "InnerDatabase(SQLite).db";
    String tablename = "storytable";
    String sql;
    SQLiteDatabase db;   // db를 다루기 위한 SQLiteDatabase 객체 생성
    Cursor resultset;   // select 문 출력위해 사용하는 Cursor 형태 객체 생성
    ListView listView;   // ListView 객체 생성
    String[] result;   // ArrayAdapter에 넣을 배열 생성

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        StoryAdapter adapter;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        db = getActivity().openOrCreateDatabase(dbname, Context.MODE_PRIVATE,null);
        listView = (ListView)rootView.findViewById(R.id.listView);

        sql = "select * from "+ tablename;
        resultset = db.rawQuery(sql, null);   // select 사용시 사용(sql문, where조건 줬을 때 넣는 값)

        int count = resultset.getCount();   // db에 저장된 행 개수를 읽어온다

        adapter = new StoryAdapter();
        for (int i=0; i<count; i++){
            resultset.moveToNext();   // 첫번째에서 다음 레코드가 없을때까지 읽음
            String str_title = resultset.getString(1);   // 첫번째 속성
            String str_image = resultset.getString(3);   // 세번째 속성
            int level = resultset.getInt(5);
            adapter.addItem(new Story(str_title, str_image, level));
        }

        listView.setAdapter(adapter);

        // 이벤트 처리 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story item = (Story) adapter.getItem(position);
                Toast.makeText(getContext().getApplicationContext(), "선택 :"+item.getTitle(), Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
    class StoryAdapter extends BaseAdapter {
        ArrayList<Story> items = new ArrayList<Story>();


        // Generate > implement methods
        @Override
        public int getCount() {
            return items.size();
        }

        public void addItem(Story item) {
            items.add(item);
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // 뷰 객체 재사용
            Layout1View view = null;
            if (convertView == null) {
                view = new Layout1View(getActivity().getApplicationContext());
            } else {
                view = (Layout1View) convertView;
            }

            Story item = items.get(position);

            view.setTitle(item.getTitle());
            view.setImage(item.getImage());
            view.setStoryId(position);
            view.setStoryLevel(item.getLevel());

            return view;
        }
    }
}
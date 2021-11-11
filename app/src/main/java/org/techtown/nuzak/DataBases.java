package org.techtown.nuzak;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class createDB implements BaseColumns {
        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String IMAGE = "image";
        public static final String KEYWORD = "keyword";
        public static final String LEVEL = "level";

        public static final String _TABLENAME0 = "storytable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +TITLE+" text not null , "
                +TEXT+" text not null , "
                +IMAGE+" text not null , "
                +KEYWORD+" text not null , "
                +LEVEL+" integer not null);";
    }
}

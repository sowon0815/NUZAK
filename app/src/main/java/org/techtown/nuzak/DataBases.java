package org.techtown.nuzak;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class createDB implements BaseColumns {
        public static final String TITLE = "title";
        public static final String TEXT = "text";
        public static final String IMAGE = "image";

        public static final String _TABLENAME0 = "storytable";
        public static final String _CREATE0 = "create table if not exists "+_TABLENAME0+"("
                +_ID+" integer primary key autoincrement, "
                +TITLE+" text not null , "
                +TEXT+" text not null , "
                +IMAGE+" text not null );";
    }
}

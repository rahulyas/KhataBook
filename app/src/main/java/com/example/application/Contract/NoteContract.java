package com.example.application.Contract;

import android.provider.BaseColumns;

public class NoteContract {

    public static final String TABLE_NAME = "note";

    public static final class NoteEntry implements BaseColumns {

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_EMPLOYEE_ID = "employee_id";

    }
}

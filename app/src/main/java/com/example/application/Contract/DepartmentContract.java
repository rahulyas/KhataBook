package com.example.application.Contract;

import android.provider.BaseColumns;

public class DepartmentContract {
    public static final String TABLE_NAME = "department";
/* BaseColumns := The BaseColumns interface provides names for the very common _ID and _COUNT columns. */


    public static final class DepartmentEntry implements BaseColumns {

        //DepartmentEntry class for deifining column names of Department table
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_DEPARTMENT_NAME = "name";
        public static final String COLUMN_DEPARTMENT_DESCRIPTION = "description" ;

    }
}

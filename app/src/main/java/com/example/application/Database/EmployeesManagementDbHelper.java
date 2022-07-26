package com.example.application.Database;

import static com.example.application.Contract.DepartmentContract.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.application.Contract.DepartmentContract;
import com.example.application.Contract.EmployeeContract;
import com.example.application.Contract.TaskContract;
import com.example.application.Model.DepartmentItem;
import com.google.android.gms.tasks.Task;

import java.sql.SQLDataException;
import java.util.ArrayList;

public class EmployeesManagementDbHelper extends SQLiteOpenHelper {
// This is database name table
    public static final String DATABASE_NAME = "employees_management.db";

// Database version. If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    public EmployeesManagementDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the employee table
        String SQL_CREATE_EMPLOYEE_TABLE = "CREATE TABLE " + EmployeeContract.TABLE_NAME + "("
                + EmployeeContract.EmployeeEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME + " VARCHAR(70) NOT NULL, "
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_BIRTHDATE + " DATE NOT NULL,"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID + " INTEGER NOT NULL,"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_JOB + " VARCHAR(50) NOT NULL,"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHONE + " VARCHAR(20),"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL + " VARCHAR(255),"
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHOTO + " VARCHAR(255), "
                + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NOTES + " VARCHAR(1024), "
                + "FOREIGN KEY(" + EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID + ") REFERENCES " + TABLE_NAME + "(" + DepartmentContract.DepartmentEntry._ID + ")"
                + ");";

        // Create a String that contains the SQL statement to create the department table
        String SQL_CREATE_DEPARTMENT_TABLE = "CREATE TABLE " + TABLE_NAME+"("
                + DepartmentContract.DepartmentEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME + " VARCHAR(255)  , "
                + DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION + "  VARCHAR(300) "
                +");";

        // Create a String that contains the SQL statement to create the task table
        String SQL_CREATE_TASK_TABLE = "CREATE TABLE " + TaskContract.TABLE_NAME + "("
                + TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TaskContract.TaskEntry.COLUMN_TASK_NAME + " VARCHAR(70) NOT NULL, "
                + TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION + " VARCHAR(300), "
                + TaskContract.TaskEntry.COLUMN_TASK_DEADLINE + " DATETIME ,"
                + TaskContract.TaskEntry.COLUMN_TASK_DATE + " DATETIME ,"
                + TaskContract.TaskEntry.COLUMN_TASK_COMPLETED + " TINYINT(1),"
                + TaskContract.TaskEntry.COLUMN_TASK_EVALUATION+" INTEGER"
                +");"
                ;
        // Create a String that contains the SQL statement to create the employee_task table
        String SQL_CREATE_EMPLOYEE_TASK_TABLE = "CREATE TABLE " + "employee_task " + "( "
                + EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID + " INTEGER NOT NULL, "
                + TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID+ " INTEGER NOT NULL, "
                + "FOREIGN KEY (" + EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID + ") REFERENCES " + EmployeeContract.TABLE_NAME + "(" + EmployeeContract.EmployeeEntry._ID + "), "
                + "FOREIGN KEY (" + TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID + ") REFERENCES " + TaskContract.TABLE_NAME +"(" + TaskContract.TaskEntry._ID + ") "
                +");"
                ;


        //executes SQL create statements
        db.execSQL(SQL_CREATE_DEPARTMENT_TABLE);
        db.execSQL(SQL_CREATE_EMPLOYEE_TABLE);
        db.execSQL(SQL_CREATE_TASK_TABLE);
        db.execSQL(SQL_CREATE_EMPLOYEE_TASK_TABLE);

    }
    //This is called when the database needs to be upgraded.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // The database is still at version 1, so there's nothing to do be done here.
         //DATABASE_VERSION ++;
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

// ADD DEPARTMENT
    public boolean addDepartment(String department_name , String department_description)
    {
        SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database
        ContentValues cv  = new ContentValues(); //used for inserting an entry
        cv.put(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME,department_name);
        cv.put(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION,department_description);
        long result = db.insert(TABLE_NAME,null,cv); //reutrns a flag to indicate succes of insertion
        if(result == -1){
            return false;
        }
        else{
            return true;
        } //-1 if insert fails
    }

// Add Employee
    public boolean addEmployee(String employee_name, String employee_birthdate ,long department_id,String employee_job,String employee_email,String employee_phone,String employee_photo){
        //adds an employee entry to employee table

        SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database
        ContentValues cv  = new ContentValues(); //used for inserting an entry


        // no need to check for null as it is required to be provided
        cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,employee_name);
        cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_BIRTHDATE,employee_birthdate);
        cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID,department_id);
        cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_JOB,employee_job);

        if(employee_email!=null && !employee_email.isEmpty()&&!employee_email.trim().isEmpty()) //checks if field is provided if not it is not added in the query
            cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL,employee_email);
        if(employee_phone!=null && !employee_phone.isEmpty()&&!employee_phone.trim().isEmpty()) // to be edited
            cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHONE,employee_phone);
        if(employee_photo!=null && !employee_photo.isEmpty()&&!employee_photo.trim().isEmpty())
            cv.put(EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHOTO,employee_photo);


        long flag = db.insert(EmployeeContract.TABLE_NAME,null,cv); //reutrns a flag to indicate succes of insertion

        return flag != -1; //-1 if insert fails

    }


    public Cursor getEmployeesOfTask(long task_id){
        SQLiteDatabase db  = this.getReadableDatabase();
        String select = "SELECT * ";
        String from = " FROM employee_task inner join "+ EmployeeContract.TABLE_NAME + " on employee_task."
                + EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID + " = "+EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID;
        String where = " WHERE employee_task."+TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID + " = ?" ;
        String query = select+from+where;      String selectionArgs[] = {String.valueOf(task_id)};
        //cursor is a table containing the rows returned form the query
        Cursor c = db.rawQuery(query, selectionArgs  );
        return c;
    }

    public Cursor getTasksOfDepartment(long department_id){
        //gets tasks of a specific deparrtment
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db
        String select = "SELECT DISTINCT " +
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_NAME + " AS TaskName , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DEADLINE +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DATE +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_EVALUATION +" AS Evaluation , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_COMPLETED +" , "+
                TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID +" , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID +" , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID +" , "+
                TABLE_NAME+"."+ DepartmentContract.DepartmentEntry._ID +" , "+
                "employee_task."+EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID+" , "+
                "employee_task."+TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID
                ;


        String from = " from "+ TABLE_NAME + " INNER JOIN " + EmployeeContract.TABLE_NAME
                + " ON " +EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID+ " = "
                + TABLE_NAME+"."+ DepartmentContract.DepartmentEntry._ID
                + " INNER JOIN " + " employee_task "
                + " ON " +EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID+ " = "
                + " employee_task."+EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID
                + " INNER JOIN " + TaskContract.TABLE_NAME
                + " ON " +TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry._ID+ " = "
                + " employee_task."+TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID
                ;
        String where = " WHERE "+ TABLE_NAME+"."+ DepartmentContract.DepartmentEntry._ID + " = " + String.valueOf(department_id);
        String group = " GROUP BY " + TaskContract.TABLE_NAME + "."+ TaskContract.TaskEntry._ID + " ";
        String query = select+from+where+group;
        return db.rawQuery(query,null); //don't forget to close the cursor after usage

    }

    public Cursor getTasksOfEmployee(long employee_id){
        //gets tasks of a specific employee
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db

        String select = "SELECT DISTINCT " +
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_NAME + " AS TaskName  , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DEADLINE +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_DATE +" , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_EVALUATION +" AS Evaluation , "+
                TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry.COLUMN_TASK_COMPLETED +" AS Completed , "+
                TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID +" , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID +" , "+
                EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID +" , "+
                TABLE_NAME+"."+ DepartmentContract.DepartmentEntry._ID +" , "+
                "employee_task."+EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID+" , "+
                "employee_task."+TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID
                ;


        String from = " from "+ TABLE_NAME + " INNER JOIN " + EmployeeContract.TABLE_NAME
                + " ON " +EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID+ " = "
                + TABLE_NAME+"."+ DepartmentContract.DepartmentEntry._ID
                + " INNER JOIN " + " employee_task "
                + " ON " +EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID+ " = "
                + " employee_task."+EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID
                + " INNER JOIN " + TaskContract.TABLE_NAME
                + " ON " +TaskContract.TABLE_NAME+"."+ TaskContract.TaskEntry._ID+ " = "
                + " employee_task."+TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID
                ;

        String where = " WHERE "+EmployeeContract.TABLE_NAME+"."+ EmployeeContract.EmployeeEntry._ID + " = " + String.valueOf(employee_id);
        String group = " GROUP BY " + TaskContract.TABLE_NAME + "."+ TaskContract.TaskEntry._ID + " ";
        String query = select+from+where+group;


        return db.rawQuery(query,null); //don't forget to close the cursor after usage


    }

    public Cursor getAllTasksCursor(){
        //gets all tasks
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db

        //specify the columns to be read
        String [] columns = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_TASK_DEADLINE,
                TaskContract.TaskEntry.COLUMN_TASK_DATE,
                TaskContract.TaskEntry.COLUMN_TASK_EVALUATION,
                TaskContract.TaskEntry.COLUMN_TASK_COMPLETED
        };

        //cursor is a table containing the rows returned form the query
        return db.query(TaskContract.TABLE_NAME,columns,null,null,null,null,null); //don't forget to close the cursor after usage

    }

    public Cursor getSpecifiTaskCursor(long task_id){

        //gets specific task by its id
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db

        //specify the columns to be read
        String [] columns = {
                TaskContract.TaskEntry._ID,
                TaskContract.TaskEntry.COLUMN_TASK_NAME,
                TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION,
                TaskContract.TaskEntry.COLUMN_TASK_DEADLINE,
                TaskContract.TaskEntry.COLUMN_TASK_DATE,
                TaskContract.TaskEntry.COLUMN_TASK_EVALUATION,
                TaskContract.TaskEntry.COLUMN_TASK_COMPLETED
        };

        //where statement to filter quere
        String selection = TaskContract.TaskEntry._ID + " =?"; //where TaskEntry._ID=task_id
        String selectionArgs[] = { String.valueOf(task_id)  };


        //cursor is a table containing the rows returned form the query
        return  db.query(TaskContract.TABLE_NAME,columns,selection,selectionArgs,null,null,null); //don't forget to close the cursor after usage

    }

    public Cursor getAllDepartments()
    {
        //gets all departments
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db

        //specify the columns to be read
        String [] columns = {
                DepartmentContract.DepartmentEntry._ID,
                DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME,
                DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION
        };

        String orderBy = DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME + " ASC "; //order by statement

        //cursor is a table containing the rows returned form the query
        return  db.query(TABLE_NAME,columns,null,null,null,null,orderBy); //don't forget to close the cursor after usage

    }

    public Cursor getDepartment(Long departmentId){
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db
        //specify the columns to be read
        String [] columns = {
                DepartmentContract.DepartmentEntry._ID,
                DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME,
                DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION
        };
        String selection = DepartmentContract.DepartmentEntry._ID + " =?"; //where statement
        String selectionArgs[] = { String.valueOf(departmentId)  };

        return  db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);//don't forget to close the cursor after usage

    }

    public Cursor getEmployee(Long employeeId){
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db
        //specify the columns to be read
        String [] columns = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHONE,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_BIRTHDATE,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_EMAIL,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_JOB,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHOTO,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NOTES
        };
        String selection = EmployeeContract.EmployeeEntry._ID + " =?"; //where statement
        String selectionArgs[] = { String.valueOf(employeeId)  };

        return  db.query(EmployeeContract.TABLE_NAME,columns,selection,selectionArgs,null,null,null);//don't forget to close the cursor after usage

    }

    public Cursor getEmployessOfDepartment(long department_id)
    {
        //gets all employees of a given department
        SQLiteDatabase db  = this.getReadableDatabase(); //get readable instance of the db

        //specify the columns to be read
        String [] columns = {
                EmployeeContract.EmployeeEntry._ID,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_JOB,
                EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_PHOTO

        };
         //department_id is the right one
        String selection = EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_DEPARTMENT_ID + " =?"; //where statement
        String selectionArgs[] = { String.valueOf(department_id)  };
        String orderBy = EmployeeContract.EmployeeEntry.COLUMN_EMPLOYEE_NAME + " ASC";


        //cursor is a table containing the rows returned form the query
        //don't forget to close the cursor after usage

        return  db.query(EmployeeContract.TABLE_NAME,columns,selection,selectionArgs,null,null,orderBy);
    }


   public Long addTask(Task task) {
       //adds task to db
    //   SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database
      // ContentValues cv = new ContentValues(); //used for inserting an entry

   /*     cv.put(TaskContract.TaskEntry.COLUMN_TASK_NAME,task.getTaskName());
        cv.put(TaskContract.TaskEntry.COLUMN_TASK_EVALUATION, task.getEvaluation());

        if(task.getTaskDetails()!=null && !task.getTaskDetails().isEmpty()&&!task.getTaskDetails().trim().isEmpty())
            cv.put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION,task.getTaskDetails());

        if(task.getTaskDeadline()!=null && !task.getTaskDeadline().isEmpty()&&!task.getTaskDeadline().trim().isEmpty())
            cv.put(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE, task.getTaskDeadline());
        cv.put(TaskContract.TaskEntry.COLUMN_TASK_COMPLETED,task.isDone());
        cv.put(TaskContract.TaskEntry.COLUMN_TASK_DATE,task.getTaskDate());
        long task_id = db.insert(TaskContract.TABLE_NAME,null,cv); //reutrns a flag to indicate succes of insertion


        cv = new ContentValues();
        ArrayList<Long> emplyee_ids = task.getEmployees_id();
        if (emplyee_ids!=null)
        {
            for(long emp_id:emplyee_ids){
                cv.put(EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID,emp_id);
                cv.put(TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID,task_id);
                long flag = db.insert("employee_task",null,cv); //reutrns a flag to indicate succes of insertion
            }
        }*/
        return null;
    }

    public boolean deleteEmployee(long employee_id){
        SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database

        db.delete("employee_task",EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID+ "="+employee_id,null);
        int flag =  db.delete(EmployeeContract.TABLE_NAME, EmployeeContract.EmployeeEntry._ID + "=" + employee_id,null) ;

        //resets autoincrement
        Cursor c = db.rawQuery("SELECT MAX("+ EmployeeContract.EmployeeEntry._ID+") from "+EmployeeContract.TABLE_NAME,null);
        c.moveToFirst();
        long max_id = c.getLong(0);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(max_id+1) +" WHERE name ='  " + EmployeeContract.TABLE_NAME+" ' " );
        c.close();


        return flag>0;

    }

    public boolean deleteTask(long task_id){
        SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database
        db.delete("employee_task",TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID+ "="+task_id,null);
        int flag = db.delete(TaskContract.TABLE_NAME, TaskContract.TaskEntry._ID + "=" + task_id,null);
        //resets autoincrement
        Cursor c = db.rawQuery("SELECT MAX("+ TaskContract.TaskEntry._ID+") from "+TaskContract.TABLE_NAME,null);
        c.moveToFirst();
        long max_id = c.getLong(0);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(max_id+1) +" WHERE name ='  " + TaskContract.TABLE_NAME+" ' " );
        c.close();
        return flag>0;

    }


    public boolean deleteDepartment(long department_id){
        SQLiteDatabase db = this.getWritableDatabase(); //gets writeable instance of database

        Cursor c = getEmployessOfDepartment(department_id);
        while(c.moveToNext()) {
            db.delete(EmployeeContract.TABLE_NAME, EmployeeContract.EmployeeEntry._ID + "  = "+c.getString(0),null);
        }
        c.close();

        //resets autoincrement for employess table
        Cursor c2 = db.rawQuery("SELECT MAX("+ EmployeeContract.EmployeeEntry._ID+") from "+EmployeeContract.TABLE_NAME,null);
        c2.moveToFirst();
        long max_id = c2.getLong(0);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(max_id+1) +" WHERE name ='  " + EmployeeContract.TABLE_NAME+" ' " );
        c2.close();

        db.delete(EmployeeContract.TABLE_NAME, DepartmentContract.DepartmentEntry._ID + "=" + department_id,null);
        int flag = db.delete(TABLE_NAME, DepartmentContract.DepartmentEntry._ID + "=" + department_id,null) ;

        //resets autoincrement
        Cursor c3 = db.rawQuery("SELECT MAX("+ DepartmentContract.DepartmentEntry._ID+") from "+ TABLE_NAME,null);
        c3.moveToFirst();
        max_id = c3.getLong(0);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(max_id+1) +" WHERE name ='  " + TABLE_NAME+" ' " );
        c3.close();

        return flag>0;
    }

    public boolean deleteAllDepartments()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int f2 = db.delete("employee_task",null,null);
        int f1 = db.delete(TaskContract.TABLE_NAME,null,null);
        int f3 = db.delete(EmployeeContract.TABLE_NAME,null,null);
        int f4 = db.delete(TABLE_NAME,null,null);

        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + EmployeeContract.TABLE_NAME+" ' " );
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + TaskContract.TABLE_NAME+" ' " );
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + TABLE_NAME+" ' " );
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + " employee_task " +" ' " );


        return f1>0 && f2>0 && f3>0 && f4>0 ;
    }

    public boolean deleteAllTasks()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        int f2 = db.delete("employee_task",null,null);
        int f1 = db.delete(TaskContract.TABLE_NAME,null,null);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + " employee_task " +" ' " );
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = " + String.valueOf(0) +" WHERE name ='  " + TaskContract.TABLE_NAME+" ' " );


        return f1>0 && f2>0 ;
    }

    public boolean updateDepartment(DepartmentItem departmentItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DepartmentContract.DepartmentEntry._ID, departmentItem.id);
        contentValues.put(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_NAME, departmentItem.name);
        contentValues.put(DepartmentContract.DepartmentEntry.COLUMN_DEPARTMENT_DESCRIPTION, departmentItem.details);
        db.update(TABLE_NAME, contentValues, DepartmentContract.DepartmentEntry._ID + " = " + departmentItem.id,null);
        return true;
    }

 public boolean updateTask(Task task){
       // int task_id = (int) task.getId();
        //ArrayList<Long> employee_ids = task.getEmployees_id();
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteDatabase db_ = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        ArrayList <Long>list_of_current_ids= new ArrayList<>();
        ArrayList <Long>list1= new ArrayList<>();
        ArrayList <Long>list2= new ArrayList<>();
        //cv.put(TaskContract.TaskEntry.COLUMN_TASK_NAME,task.getTaskName());
       // cv.put(TaskContract.TaskEntry.COLUMN_TASK_EVALUATION, task.getEvaluation());
        //cv.put(TaskContract.TaskEntry.COLUMN_TASK_DESCRIPTION,task.getTaskDetails());
        //cv.put(TaskContract.TaskEntry.COLUMN_TASK_DEADLINE,task.getTaskDeadline());
        //db.update(TaskContract.TABLE_NAME, cv, TaskContract.TaskEntry._ID + "=" + task_id,null);


       /* Cursor c= db_.rawQuery("SELECT "+EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID+" from employee_task where "+ TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID+ "= "+ task_id, null);
        if (c.moveToFirst()){
            while(!c.isAfterLast()){
                @SuppressLint("Range") String data = c.getString(c.getColumnIndex(EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID));
                list_of_current_ids.add(Long.parseLong(data));
                c.moveToNext();
            }
        }
        c.close();
*/
        //list1.addAll(employee_ids);
        list2.addAll(list_of_current_ids);
        list1.removeAll(list_of_current_ids);
        //list2.removeAll(employee_ids);

        if(list2.size()>0){
            for(long emp_id:list2){
                long flag= db.delete("employee_task",EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID+ "="+emp_id,null);
                if(flag==-1) return false;
            }
        }

        if(list1.size()>0){
            cv=new ContentValues();
            for(long emp_id:list1){
                cv.put(EmployeeContract.TABLE_NAME+ EmployeeContract.EmployeeEntry._ID,emp_id);
          //      cv.put(TaskContract.TABLE_NAME+ TaskContract.TaskEntry._ID,task_id);
                long flag = db.insert("employee_task",null,cv); //reutrns a flag to indicate succes of insertion
                if(flag==-1) return flag==-1;
            }
        }

        return true;

    }
    public boolean updateTaskEvaluation(Long task_id,boolean task_completed , int task_evaluation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TaskContract.TaskEntry.COLUMN_TASK_COMPLETED, task_completed);
        cv.put(TaskContract.TaskEntry.COLUMN_TASK_EVALUATION, task_evaluation);
        return  db.update(TaskContract.TABLE_NAME, cv, TaskContract.TaskEntry._ID + "=" + task_id,null)> 0;
    }

    public boolean updateEmployee(long id,ContentValues values){
        SQLiteDatabase db = this.getWritableDatabase();
        String selection = EmployeeContract.EmployeeEntry._ID + " =?"; //where statement
        String selectionArgs[] = { String.valueOf(id)  };
        return  db.update(EmployeeContract.TABLE_NAME,values,selection,selectionArgs)>0;
    }

}

package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "StudentManagement.db";
    private static final int DATABASE_VERSION = 1;

    // Table and Column Names
    public static final String TABLE_STUDENTS = "students";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_STUDENT_ID = "student_id";
    public static final String COLUMN_STUDENT_NAME = "student_name";
    public static final String COLUMN_IS_ENROLLED = "is_enrolled";

    // Create table SQL Query
    private static final String CREATE_TABLE_STUDENTS =
            "CREATE TABLE " + TABLE_STUDENTS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_STUDENT_ID + " TEXT NOT NULL, " +
                    COLUMN_STUDENT_NAME + " TEXT NOT NULL, " +
                    COLUMN_IS_ENROLLED + " INTEGER NOT NULL" +
                    ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_STUDENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        onCreate(db);
    }

    // 1. ADD / CREATE Student
    public long addStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_STUDENT_NAME, student.getStudentName());
        values.put(COLUMN_IS_ENROLLED, student.isEnrolled() ? 1 : 0);

        long id = db.insert(TABLE_STUDENTS, null, values);
        db.close();
        return id;
    }

    // 2. VIEW / READ All Students
    public List<Student> getAllStudents() {
        List<Student> studentList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_STUDENTS + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int idIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
                int studentIdIndex = cursor.getColumnIndexOrThrow(COLUMN_STUDENT_ID);
                int nameIndex = cursor.getColumnIndexOrThrow(COLUMN_STUDENT_NAME);
                int enrolledIndex = cursor.getColumnIndexOrThrow(COLUMN_IS_ENROLLED);

                long id = cursor.getLong(idIndex);
                String studentId = cursor.getString(studentIdIndex);
                String name = cursor.getString(nameIndex);
                boolean isEnrolled = cursor.getInt(enrolledIndex) == 1;

                Student student = new Student(id, studentId, name, isEnrolled);
                studentList.add(student);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return studentList;
    }

    // 3. EDIT / UPDATE Student
    public int updateStudent(Student student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_ID, student.getStudentId());
        values.put(COLUMN_STUDENT_NAME, student.getStudentName());
        values.put(COLUMN_IS_ENROLLED, student.isEnrolled() ? 1 : 0);

        int rowsAffected = db.update(TABLE_STUDENTS, values,
                COLUMN_ID + " = ?", new String[]{String.valueOf(student.getId())});
        db.close();
        return rowsAffected;
    }

    // 4. DELETE Student
    public int deleteStudent(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_STUDENTS,
                COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted;
    }
}

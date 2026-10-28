package com.example.midexam;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etStudentId, etStudentName;
    private MaterialCheckBox cbEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView, btnClear;
    private RecyclerView rvStudents;
    private TextView tvEmptyList;

    private DatabaseHelper dbHelper;
    private StudentAdapter studentAdapter;
    private Student selectedStudent = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Views
        initViews();

        // Initialize SQLite Database Helper
        dbHelper = new DatabaseHelper(this);

        // Setup RecyclerView
        setupRecyclerView();

        // Setup Event Listeners for Buttons
        setupListeners();

        // Load data on startup
        loadStudentData();
    }

    private void initViews() {
        etStudentId = findViewById(R.id.etStudentId);
        etStudentName = findViewById(R.id.etStudentName);
        cbEnrolled = findViewById(R.id.cbEnrolled);

        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        btnClear = findViewById(R.id.btnClear);

        rvStudents = findViewById(R.id.rvStudents);
        tvEmptyList = findViewById(R.id.tvEmptyList);
    }

    private void setupRecyclerView() {
        rvStudents.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this, null, student -> {
            selectedStudent = student;
            etStudentId.setText(student.getStudentId());
            etStudentName.setText(student.getStudentName());
            cbEnrolled.setChecked(student.isEnrolled());

            Toast.makeText(MainActivity.this, "Selected: " + student.getStudentName(), Toast.LENGTH_SHORT).show();
        });
        rvStudents.setAdapter(studentAdapter);
    }

    private void setupListeners() {
        // ADD Button Handler
        btnAdd.setOnClickListener(v -> addStudent());

        // EDIT Button Handler
        btnEdit.setOnClickListener(v -> editStudent());

        // DELETE Button Handler
        btnDelete.setOnClickListener(v -> deleteStudent());

        // VIEW Button Handler
        btnView.setOnClickListener(v -> {
            loadStudentData();
            Toast.makeText(MainActivity.this, "Data loaded from database", Toast.LENGTH_SHORT).show();
        });

        // CLEAR Button Handler
        btnClear.setOnClickListener(v -> clearForm());
    }

    // 1. CREATE / ADD Operation
    private void addStudent() {
        String studentIdStr = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";
        String nameStr = etStudentName.getText() != null ? etStudentName.getText().toString().trim() : "";
        boolean isEnrolled = cbEnrolled.isChecked();

        if (TextUtils.isEmpty(nameStr)) {
            etStudentName.setError("Student Name is required");
            etStudentName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(studentIdStr)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return;
        }

        Student student = new Student(studentIdStr, nameStr, isEnrolled);
        long result = dbHelper.addStudent(student);

        if (result != -1) {
            Toast.makeText(this, "Student added successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show();
        }
    }

    // 2. READ / VIEW Operation
    private void loadStudentData() {
        List<Student> studentList = dbHelper.getAllStudents();
        studentAdapter.setStudentList(studentList);

        if (studentList.isEmpty()) {
            tvEmptyList.setVisibility(View.VISIBLE);
            rvStudents.setVisibility(View.GONE);
        } else {
            tvEmptyList.setVisibility(View.GONE);
            rvStudents.setVisibility(View.VISIBLE);
        }
    }

    // 3. UPDATE / EDIT Operation
    private void editStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Please tap a student from the list first to EDIT", Toast.LENGTH_LONG).show();
            return;
        }

        String studentIdStr = etStudentId.getText() != null ? etStudentId.getText().toString().trim() : "";
        String nameStr = etStudentName.getText() != null ? etStudentName.getText().toString().trim() : "";
        boolean isEnrolled = cbEnrolled.isChecked();

        if (TextUtils.isEmpty(nameStr)) {
            etStudentName.setError("Student Name is required");
            etStudentName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(studentIdStr)) {
            etStudentId.setError("Student ID is required");
            etStudentId.requestFocus();
            return;
        }

        selectedStudent.setStudentId(studentIdStr);
        selectedStudent.setStudentName(nameStr);
        selectedStudent.setEnrolled(isEnrolled);

        int rows = dbHelper.updateStudent(selectedStudent);
        if (rows > 0) {
            Toast.makeText(this, "Student updated successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to update student", Toast.LENGTH_SHORT).show();
        }
    }

    // 4. DELETE Operation
    private void deleteStudent() {
        if (selectedStudent == null) {
            Toast.makeText(this, "Please tap a student from the list first to DELETE", Toast.LENGTH_LONG).show();
            return;
        }

        int rows = dbHelper.deleteStudent(selectedStudent.getId());
        if (rows > 0) {
            Toast.makeText(this, "Student deleted successfully!", Toast.LENGTH_SHORT).show();
            clearForm();
            loadStudentData();
        } else {
            Toast.makeText(this, "Failed to delete student", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearForm() {
        etStudentId.setText("");
        etStudentName.setText("");
        cbEnrolled.setChecked(false);
        etStudentId.setError(null);
        etStudentName.setError(null);
        selectedStudent = null;
        if (studentAdapter != null) {
            studentAdapter.setSelectedStudent(null);
        }
        etStudentId.clearFocus();
        etStudentName.clearFocus();
    }
}

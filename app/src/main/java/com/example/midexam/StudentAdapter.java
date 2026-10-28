package com.example.midexam;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }

    private final Context context;
    private List<Student> studentList;
    private final OnStudentClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;

    public StudentAdapter(Context context, List<Student> studentList, OnStudentClickListener listener) {
        this.context = context;
        this.studentList = studentList != null ? studentList : new ArrayList<>();
        this.listener = listener;
    }

    public void setStudentList(List<Student> newStudentList) {
        this.studentList = newStudentList;
        this.selectedPosition = RecyclerView.NO_POSITION;
        notifyDataSetChanged();
    }

    public void setSelectedStudent(Student student) {
        if (student == null) {
            selectedPosition = RecyclerView.NO_POSITION;
        } else {
            for (int i = 0; i < studentList.size(); i++) {
                if (studentList.get(i).getId() == student.getId()) {
                    selectedPosition = i;
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.tvStudentName.setText(student.getStudentName());
        holder.tvStudentId.setText("ID: " + student.getStudentId());

        if (student.isEnrolled()) {
            holder.chipStatus.setText("Enrolled");
            holder.chipStatus.setChipBackgroundColorResource(android.R.color.holo_green_light);
            holder.chipStatus.setTextColor(Color.WHITE);
        } else {
            holder.chipStatus.setText("Not Enrolled");
            holder.chipStatus.setChipBackgroundColorResource(android.R.color.holo_red_light);
            holder.chipStatus.setTextColor(Color.WHITE);
        }

        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getBindingAdapterPosition();
            notifyItemChanged(previousSelected);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onStudentClick(student);
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView tvStudentName, tvStudentId;
        Chip chipStatus;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStudentId = itemView.findViewById(R.id.tvStudentId);
            chipStatus = itemView.findViewById(R.id.chipStatus);
        }
    }
}

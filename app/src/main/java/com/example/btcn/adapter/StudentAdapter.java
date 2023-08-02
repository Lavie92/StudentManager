package com.example.btcn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btcn.R;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.dao.StudentFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private OnItemClickListener itemClickListener;
    private List<Faculty> facultyList;

    public interface OnDataChangeListener {
        void onDataChanged(List<Student> students);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Student student);
    }

    private List<Student> studentList;
    StudentFirebaseDAO studentFirebaseDAO;

    public String getFacultyNameById(String facultyId) {
        for (Faculty faculty : facultyList) {
            if (faculty.getId().equals(facultyId)) {
                return faculty.getName();
            }
        }
        return "";
    }

    public StudentAdapter(List<Student> studentList, List<Faculty> facultyList,
            StudentFirebaseDAO studentFirebaseDAO) {
        this.studentList = studentList;
        this.facultyList = facultyList;
        this.studentFirebaseDAO = studentFirebaseDAO;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Student student = studentList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(student);
                }
            }
        });
        holder.txtName.setText(student.getName());
        holder.txtStudentFaculty.setText(student.getFacultyId());
        holder.txtGpa.setText(String.format("%.2f", student.getGpa()));
        String facultyName = getFacultyNameById(student.getFacultyId());
        holder.txtStudentFaculty.setText(facultyName);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void listenStudentFirestore(StudentFirebaseDAO studentFirebaseDAO,
            RecyclerView recyclerView) {
        studentFirebaseDAO.listenStudents(new StudentAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Student> students) {
                studentList.clear();
                studentList.addAll(students);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {

        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtStudentFaculty;
        private TextView txtGpa;


        public ViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.image_avatar);
            txtName = itemView.findViewById(R.id.text_name);
            txtStudentFaculty = itemView.findViewById(R.id.text_student_faculty);
            txtGpa = itemView.findViewById(R.id.text_gpa);
        }
    }

    public void addStudent(Student student) {
        studentFirebaseDAO.Insert(student);
    }
}
package com.example.btcn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btcn.R;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.dao.StudentFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<Student> studentList;
    StudentFirebaseDAO studentFirebaseDAO;

    public StudentAdapter(List<Student> studentList, StudentFirebaseDAO studentFirebaseDAO) {
        this.studentList = studentList;
        this.studentFirebaseDAO = studentFirebaseDAO;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Student student = studentList.get(position);

        holder.txtName.setText(student.getName());
        holder.txtStudentId.setText(student.getId());
        holder.txtGpa.setText(String.format("%.2f", student.getGpa()));

//        // set avatar
//        Glide.with(holder.imgAvatar)
//                .load(student.getAvatarUrl())
//                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public void listenStudentFirestore(StudentFirebaseDAO studentFirebaseDAO, RecyclerView recyclerView) {
        studentFirebaseDAO.listenStudents(new StudentAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Student> students) {
                studentList.clear();
                studentList.addAll(students);
                recyclerView.getAdapter().notifyDataSetChanged();
            }
        });
    }



    public interface OnDataChangeListener {
        void onDataChanged(List<Student> students);
    }
    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgAvatar;
        private TextView txtName;
        private TextView txtStudentId;
        private TextView txtGpa;

        public ViewHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.image_avatar);
            txtName = itemView.findViewById(R.id.text_name);
            txtStudentId = itemView.findViewById(R.id.text_student_id);
            txtGpa = itemView.findViewById(R.id.text_gpa);
        }
    }

}
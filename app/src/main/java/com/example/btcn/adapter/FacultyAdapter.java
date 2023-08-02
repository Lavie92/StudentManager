package com.example.btcn.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.btcn.R;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {


    public interface  OnItemClickListener {
        void onItemClick(Faculty faculty);
    }
    private List<Faculty> facultyList;
    private OnItemClickListener itemClickListener;
    FacultyFirebaseDAO facultyFirebaseDAO;
    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FacultyAdapter(List<Faculty> facultyList, FacultyFirebaseDAO facultyFirebaseDAO) {
        this.facultyList = facultyList;
        this.facultyFirebaseDAO = facultyFirebaseDAO;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_faculty, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Faculty faculty = facultyList.get(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.onItemClick(faculty);
                }
            }
        });
        holder.textName.setText(faculty.getName());
        holder.textCode.setText(faculty.getId());
        holder.textStudentCount.setText(String.valueOf(faculty.getStudentCount()));
    }
    public void listenFacultyFirestore(FacultyFirebaseDAO facultyFirebaseDAO, RecyclerView rcFaculty) {
        facultyFirebaseDAO.listenFaculties(new OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Faculty> faculties) {
                facultyList.clear();
                facultyList.addAll(faculties);
                notifyDataSetChanged();
            }
        });
    }

    public interface OnDataChangeListener {
        void onDataChanged(List<Faculty> faculties);
    }

    @Override
    public int getItemCount() {
        return facultyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textCode;
        private TextView textStudentCount;

        public ViewHolder(View itemView) {
            super(itemView);

            textName = itemView.findViewById(R.id.text_faculty_name);
            textCode = itemView.findViewById(R.id.text_faculty_code);
            textStudentCount = itemView.findViewById(R.id.text_student_count);
        }
    }
    public Faculty getItem(int position) {
        return facultyList.get(position);
    }

    public void addFaculty(Faculty faculty) {
        facultyFirebaseDAO.Insert(faculty);
    }
}
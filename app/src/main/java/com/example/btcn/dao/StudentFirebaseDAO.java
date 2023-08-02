package com.example.btcn.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.adapter.StudentAdapter;
import com.example.btcn.fragment.StudentFragment;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class StudentFirebaseDAO {

    private FirebaseFirestore db;
    Context context;

    public StudentFirebaseDAO(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void Insert(Student student) {
        student.setId(UUID.randomUUID().toString());
        HashMap<String, Object> mapproduct = student.convertToHashMap();
        db.collection("Student").document(student.getId())
                .set(mapproduct)

                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm mới student thành công!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Thêm mới student thất bại!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public List<Student> getAllStudents() {

        List<Student> students = new ArrayList<>();

        db.collection("Student")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Student student = document.toObject(Student.class);
                            students.add(student);
                        }
                    }
                });

        return students;

    }

    public void listenStudents(StudentAdapter.OnDataChangeListener listener) {
        db.collection("Student").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable
            FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FireBase", "listen:error", e);
                    return;
                }

                List<Student> studentList = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    Student student = doc.toObject(Student.class);
                    studentList.add(student);
                }

                listener.onDataChanged(studentList);
            }
        });
    }
    public void updateStudent(String id, Student student) {
        student.setFacultyId(student.getFacultyId());

        db.collection("Students").document(id)
                .set(student);
    }

    public void deleteStudent(String id) {
        db.collection("Students").document(id)
                .delete();
    }

}
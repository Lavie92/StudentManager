package com.example.btcn.dao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class FacultyFirebaseDAO {

    private FirebaseFirestore db;
    Context context;

    public FacultyFirebaseDAO(Context context) {
        db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void Insert(Faculty faculty) {
        faculty.setId(UUID.randomUUID().toString());
        HashMap<String, Object> mapproduct = faculty.convertToHashMap();
        db.collection("Faculty").document(faculty.getId())
                .set(mapproduct)

                .addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(context, "Thêm mới khoa thành công!",
                                Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Thêm mới khoa thất bại!",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void updateFaculty(String id, Faculty faculty) {
        db.collection("Faculty").document(id)
                .set(faculty);
    }

    public void Update(Faculty faculty) {
        if (faculty.getId() != null) {
            db.collection("Faculty").document(faculty.getId()).set(faculty);
        }
    }

    public void deleteFacultyAndStudent(String id) {
        // Lấy danh sách studentIds
        db.collection("Student")
                .whereEqualTo("facultyId", id)
                .get()
                .addOnCompleteListener(task -> {
                    List<String> studentIds = new ArrayList<>();
                    for (DocumentSnapshot doc : task.getResult()) {
                        studentIds.add(doc.getId());
                    }

                    // Xóa student
                    deleteStudents(studentIds);

                    // Xóa faculty
                    deleteFacultyAndStudent(id);
                });
    }

    public List<Faculty> getAllFaculties() {

        List<Faculty> faculties = new ArrayList<>();

        db.collection("Faculty")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Faculty faculty = document.toObject(Faculty.class);
                            faculties.add(faculty);
                        }
                    }
                });

        return faculties;

    }

    public void listenFaculties(FacultyAdapter.OnDataChangeListener listener) {
        db.collection("Faculty").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable
            FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w("FireBase", "listen:error", e);
                    return;
                }

                List<Faculty> faculties = new ArrayList<>();
                for (DocumentSnapshot doc : snapshots.getDocuments()) {
                    Faculty faculty = doc.toObject(Faculty.class);
                    faculties.add(faculty);
                }

                listener.onDataChanged(faculties);
            }
        });
    }

    private void deleteStudents(List<String> studentIds) {
        WriteBatch batch = db.batch();

        for (String studentId : studentIds) {
            DocumentReference ref = db.collection("Students").document(studentId);
            batch.delete(ref);
        }

        batch.commit();
    }

    public void deleteFaculty(String id) {
        db.collection("Faculties").document(id)
                .delete();
    }

}
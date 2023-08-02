package com.example.btcn.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btcn.R;
import com.example.btcn.adapter.CustomArrayAdapter;
import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.adapter.StudentAdapter;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.dao.StudentFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class StudentFragment extends Fragment {

    private List<Student> studentList = new ArrayList<>();
    private StudentAdapter studentAdapter;
    private List<Faculty> facultyList = new ArrayList<>();
    private FacultyAdapter facultyAdapter;
    private StudentFirebaseDAO studentFirebaseDAO;
    private FacultyFirebaseDAO facultyFirebaseDAO;

    public StudentFragment() {
        // Required empty public constructor
    }

    public static StudentFragment newInstance() {
        return new StudentFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);

        studentFirebaseDAO = new StudentFirebaseDAO(getActivity());
        facultyFirebaseDAO = new FacultyFirebaseDAO(getActivity());

        RecyclerView rcStudent = view.findViewById(R.id.rcStudent);

        facultyAdapter = new FacultyAdapter(facultyList, facultyFirebaseDAO);
        facultyFirebaseDAO.listenFaculties(new FacultyAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Faculty> faculties) {
                facultyList.clear();
                facultyList.addAll(faculties);
                facultyAdapter.notifyDataSetChanged();
            }
        });

        studentAdapter = new StudentAdapter(studentList, facultyList,studentFirebaseDAO);
        studentFirebaseDAO.listenStudents(new StudentAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Student> students) {
                studentList.clear();
                studentList.addAll(students);
                studentAdapter.notifyDataSetChanged();
            }
        });
        rcStudent.setAdapter(studentAdapter);
        rcStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcStudent.addItemDecoration(itemDecoration);

        studentAdapter.setItemClickListener(new StudentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Student student) {
                editStudent(student);
            }
        });

        FloatingActionButton btnAdd = view.findViewById(R.id.btnAddStudent);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewStudent(view);
            }
        });

        return view;
    }

    public void addNewStudent(View view) {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_add_student, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setView(viewDialog);
        AlertDialog alert = builder.create();
        alert.show();
        EditText edtStudentName = viewDialog.findViewById(R.id.edt_name);
        EditText edtGPA = viewDialog.findViewById(R.id.edt_gpa);
        Spinner spinnerFaculty = viewDialog.findViewById(R.id.spinner_faculty);
        CustomArrayAdapter facultyAdapter = new CustomArrayAdapter(getContext(),
                android.R.layout.simple_spinner_item, facultyList);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFaculty.setAdapter(facultyAdapter);

        viewDialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String studentName = edtStudentName.getText().toString().trim();
                String studentGPA = edtGPA.getText().toString();
                if (studentName.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập tên sinh viên", Toast.LENGTH_SHORT).show();
                } else if (studentGPA.isEmpty()) {
                    Toast.makeText(getActivity(), "Vui lòng nhập gpa sinh viên", Toast.LENGTH_SHORT).show();
                } else {
                    Faculty selectedFaculty = (Faculty) spinnerFaculty.getSelectedItem();
                    String facultyId = selectedFaculty.getId();
                    double gpa = Double.parseDouble(studentGPA);

                    Student student = new Student();
                    student.setName(studentName);
                    student.setFacultyId(facultyId);
                    student.setGpa(gpa);
                    studentFirebaseDAO.Insert(student);

                    alert.dismiss();
                }
            }
        });
    }

    public void editStudent(Student student) {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_add_student, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EditText edtName = viewDialog.findViewById(R.id.edt_name);
        EditText gpa = viewDialog.findViewById(R.id.edt_gpa);
        Spinner spinner_faculty = viewDialog.findViewById(R.id.spinner_faculty);

        edtName.setText(student.getName());
        gpa.setText(String.valueOf(student.getGpa()));

        List<String> facultyNames = new ArrayList<>();
        for (Faculty faculty : facultyList) {
            facultyNames.add(faculty.getName());
        }

        ArrayAdapter<String> facultyAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, facultyNames);
        facultyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_faculty.setAdapter(facultyAdapter);
        for (int i = 0; i < facultyList.size(); i++) {
            if (facultyList.get(i).getId().equals(student.getFacultyId())) {
                spinner_faculty.setSelection(i);
                break;
            }
        }

        builder.setView(viewDialog)
                .setTitle("Edit Student")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = edtName.getText().toString().trim();
                        String newGpa = gpa.getText().toString().trim();
                        int selectedFacultyIndex = spinner_faculty.getSelectedItemPosition();

                        if (newName.isEmpty()) {
                            Toast.makeText(getActivity(), "Please enter the student name", Toast.LENGTH_SHORT).show();
                        } else {
                            student.setName(newName);
                            student.setGpa(Double.parseDouble(newGpa));

                            // Get the selected faculty object from the list based on the index
                            if (selectedFacultyIndex >= 0 && selectedFacultyIndex < facultyList.size()) {
                                Faculty selectedFaculty = facultyList.get(selectedFacultyIndex);
                                student.setFacultyId(selectedFaculty.getId());
                            }

                            studentFirebaseDAO.Update(student);
                            dialog.dismiss();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

}

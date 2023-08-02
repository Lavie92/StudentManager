package com.example.btcn.fragment;

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

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    List<Student> studentList = new ArrayList<>();
    private List<Faculty> facultyList;
    public StudentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment song.
     */
    // TODO: Rename and change types and number of parameters
    public static StudentFragment newInstance(String param1, String param2) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StudentFirebaseDAO studentFirebaseDAO = new StudentFirebaseDAO(getActivity());
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        RecyclerView rcStudent = view.findViewById(R.id.rcStudent);
        FacultyFirebaseDAO facultyFirebaseDAO = new FacultyFirebaseDAO(getActivity());
        FacultyAdapter facultyAdapter = new FacultyAdapter(facultyList, facultyFirebaseDAO);
        rcStudent.setAdapter(facultyAdapter);
        facultyList = new ArrayList<>();
//        facultyList.addAll(facultyFirebaseDAO.getAllFaculties());
        StudentAdapter studentAdapter = new StudentAdapter(studentList, studentFirebaseDAO);
        studentAdapter.listenStudentFirestore(studentFirebaseDAO, rcStudent);
        rcStudent.setAdapter(studentAdapter);
        rcStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcStudent.addItemDecoration(itemDecoration);
        facultyFirebaseDAO.listenFaculties(new FacultyAdapter.OnDataChangeListener() {
            @Override
            public void onDataChanged(List<Faculty> faculties) {
                facultyList.clear();
                facultyList.addAll(faculties);
                facultyAdapter.notifyDataSetChanged();
            }
        });
        studentList = studentFirebaseDAO.getAllStudents();
        rcStudent.setAdapter(studentAdapter);
        rcStudent.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton btnAdd = view.findViewById(R.id.btnAddStudent);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            // Hiển thị thông báo lỗi nếu EditText tên sinh viên là rỗng
                            Toast.makeText(getActivity(), "Vui lòng nhập tên sinh viên", Toast.LENGTH_SHORT).show();
                        } else if (studentGPA.isEmpty()) {
                            Toast.makeText(getActivity(), "Vui lòng nhập gpa sinh viên", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // Lấy facultyId từ Faculty đã chọn
                            Faculty selectedFaculty = (Faculty) spinnerFaculty.getSelectedItem();
                            String facultyId = selectedFaculty.getId();
                            double gpa = Double.parseDouble(studentGPA);

                            // Thêm sinh viên vào Firebase
                            Student student = new Student();
                            student.setName(studentName);
                            student.setFacultyId(facultyId); // Lưu facultyId đã chọn
                            student.setGpa(gpa);
                            studentFirebaseDAO.Insert(student);

                            // Đóng dialog và cập nhật danh sách sinh viên trong RecyclerView
                            alert.dismiss();
                        }
                    }
                });

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}
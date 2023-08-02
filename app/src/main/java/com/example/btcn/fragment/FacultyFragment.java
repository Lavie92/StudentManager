package com.example.btcn.fragment;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.btcn.R;
import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.adapter.StudentAdapter;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.example.btcn.models.Student;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class FacultyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    List<Faculty> facultyList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    FacultyFirebaseDAO facultyFirebaseDAO = new FacultyFirebaseDAO(getActivity());


    public FacultyFragment() {

    }
    public static FacultyFragment newInstance(String param1, String param2) {
        FacultyFragment fragment = new FacultyFragment();
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
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);
        RecyclerView rcFaculty = view.findViewById(R.id.rc_faculty);

        FacultyAdapter facultyAdapter = new FacultyAdapter(facultyList, facultyFirebaseDAO);
        facultyAdapter.listenFacultyFirestore(facultyFirebaseDAO, rcFaculty);
        facultyAdapter.setItemClickListener(new FacultyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Faculty faculty) {
                editFaculty(faculty);
            }
        });
        rcFaculty.setAdapter(facultyAdapter);
        rcFaculty.setLayoutManager(new LinearLayoutManager(getActivity()));
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        rcFaculty.addItemDecoration(itemDecoration);
        facultyList = facultyFirebaseDAO.getAllFaculties();
        rcFaculty.setAdapter(facultyAdapter);
        rcFaculty.setLayoutManager(new LinearLayoutManager(getActivity()));
        FloatingActionButton btnAdd = view.findViewById(R.id.btn_add_faculty);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View viewDialog = getLayoutInflater().inflate(R.layout.dialog_add_faculty, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setView(viewDialog);
                AlertDialog alert = builder.create();
                alert.show();
                EditText edtFacultyName = viewDialog.findViewById(R.id.edt_faculty_name);
                viewDialog.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String facultyName = edtFacultyName.getText().toString().trim();
                        if (facultyName.isEmpty()) {
                            Toast.makeText(getActivity(), "Vui lòng nhập tên khoa", Toast.LENGTH_SHORT).show();
                        } else {
                            Faculty faculty = new Faculty();
                            faculty.setName(facultyName);
                            facultyAdapter.addFaculty(faculty);
                            facultyAdapter.notifyItemInserted(facultyAdapter.getItemCount() - 1);
                            alert.dismiss();
                        }
                    }
                });
            }
        });
        return view;
    }
    public void editFaculty(Faculty faculty) {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_add_faculty, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        EditText edtName = viewDialog.findViewById(R.id.edt_faculty_name);
        edtName.setText(faculty.getName());
        builder.setView(viewDialog)
                .setTitle("Edit Faculty")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = edtName.getText().toString().trim();

                        if (newName.isEmpty()) {
                            facultyFirebaseDAO.deleteFaculty(faculty.getId());
                            Toast.makeText(getActivity(), "Đã xoá Faculty thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            faculty.setName(newName);
                            facultyFirebaseDAO.Update(faculty);
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
package com.example.btcn.fragment;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.btcn.R;
import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.models.Faculty;
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

    public FacultyFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment course.
     */
    // TODO: Rename and change types and number of parameters
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
        FacultyFirebaseDAO facultyFirebaseDAO = new FacultyFirebaseDAO(getActivity());
        View view = inflater.inflate(R.layout.fragment_faculty, container, false);
        RecyclerView rcFaculty = view.findViewById(R.id.rc_faculty);

        FacultyAdapter facultyAdapter = new FacultyAdapter(facultyList, facultyFirebaseDAO);
        facultyAdapter.listenFacultyFirestore(facultyFirebaseDAO, rcFaculty);

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
}
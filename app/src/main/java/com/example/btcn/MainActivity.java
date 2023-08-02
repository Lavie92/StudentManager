package com.example.btcn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.btcn.adapter.FacultyAdapter;
import com.example.btcn.adapter.ViewPager2Adapter;
import com.example.btcn.dao.FacultyFirebaseDAO;
import com.example.btcn.models.Faculty;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<Faculty> facultyList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPagerAndBottomNav();

    }
    private void setupViewPagerAndBottomNav() {

        ViewPager2 viewPager2 = findViewById(R.id.viewPage2);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        ViewPager2Adapter adapter = new ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.home) {
                    viewPager2.setCurrentItem(0);
                } else if (item.getItemId() == R.id.faculty) {
                    viewPager2.setCurrentItem(1);
                } else if (item.getItemId() == R.id.student) {
                    viewPager2.setCurrentItem(2);
                } else {
                    viewPager2.setCurrentItem(0);
                }
                return true;
            }
        });

        viewPager2.registerOnPageChangeCallback (new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected (int position)
            {
                switch (position)
                {
                    case 0:
                        bottomNav.getMenu().findItem(R.id.home).setChecked(true);
                        break;
                    case 1:
                        bottomNav.getMenu().findItem(R.id.faculty).setChecked(true);
                        break;
                    case 2:
                        bottomNav.getMenu().findItem(R.id.student).setChecked(true);
                        break;
                }
                super.onPageSelected(position);
            }
        });

    }
}
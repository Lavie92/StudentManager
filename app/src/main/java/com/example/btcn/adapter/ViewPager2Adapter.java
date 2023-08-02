package com.example.btcn.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.btcn.R;
import com.example.btcn.fragment.FacultyFragment;
import com.example.btcn.fragment.home;
import com.example.btcn.fragment.StudentFragment;
import com.example.btcn.models.Faculty;

public class ViewPager2Adapter extends FragmentStateAdapter {
    public ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position){
        if(position == 2){
            return new StudentFragment();
        } else if (position == 1) {
            return new FacultyFragment();
        }else {
            return new home();
        }
    }
    @Override
    public int getItemCount(){
        return 3;
    }
}

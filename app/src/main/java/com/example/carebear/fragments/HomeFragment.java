//package com.example.carebear;
//
//import android.content.Intent;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//
//
//public class HomeFragment extends Fragment {
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        Button btnLogout = view.findViewById(R.id.btn_logout);
//        btnLogout.setOnClickListener(v -> {
//            // Navigate back to MainActivity (welcome screen)
//            Intent intent = new Intent(getActivity(), MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        });
//
//        return view;
//    }
//}

// HomeFragment.java

package com.example.carebear.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.carebear.R;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}

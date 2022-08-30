package com.dofury.foodguide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Community extends Fragment {
    private View view;
    public static Community newInstance(){
        return new Community();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_community, container, false);
        Button button = view.findViewById(R.id.testButton);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View arg0){
                ((Activity)getActivity()).setFrag(Setting.newInstance());
            }
        });
        return view;
    }
}

    
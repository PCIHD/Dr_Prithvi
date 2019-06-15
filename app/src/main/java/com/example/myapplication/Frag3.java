package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frag3 extends Fragment {
    private LinearLayout listView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag3_layout , container , false);
        Activity activity = getActivity();
        listView = (LinearLayout)view.findViewById(R.id.listView1);
        TextView Scheme1 = new TextView(activity);
        Scheme1.setContentDescription("Scheme1");
        Scheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), scheme1.class );
                startActivity(i);


      }
});



        return view;
    }
}
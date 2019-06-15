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

public class Frag1 extends Fragment {
    private LinearLayout listView;
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String[] menuItems = {"APPLE","BLUEBERRY","CHERRY"};
        View view = inflater.inflate(R.layout.frag1_layout , container , false);
        Activity activity = getActivity();
        listView = (LinearLayout)view.findViewById(R.id.listView1);
        ImageView Tomato = new ImageView(activity);
        Tomato.setImageResource(R.drawable.tomatoes);
        Tomato.setContentDescription("Tomato");
        Tomato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getActivity(), tomato.class );
                startActivity(i);


            }
        });
        listView.addView(Tomato);

        ImageView Apple = new ImageView(activity);
        Apple.setImageResource(R.drawable.apple);
        Apple.setContentDescription("Apple");
        Apple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), apple.class);
                startActivity(i);

            }
        });
        listView.addView(Apple);

        ImageView Blueberry = new ImageView(activity);
        Blueberry.setImageResource(R.drawable.blueberry);
        Blueberry.setContentDescription("Blueberry");
        Blueberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), Blue2.class);
                startActivity(i);

            }
        });
        listView.addView(Blueberry);

        ImageView Cherry = new ImageView(activity);
        Cherry.setImageResource(R.drawable.cherry);
        Cherry.setContentDescription("Cherry");
        Cherry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), cherry2.class);
                startActivity(i);

            }
        });
        listView.addView(Cherry);

        ImageView Corn = new ImageView(activity);
        Corn.setImageResource(R.drawable.corn);
        Corn.setContentDescription("Corn");
        Corn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), corn2.class);
                startActivity(i);

            }
        });
        listView.addView(Corn);

        ImageView Grapes = new ImageView(activity);
        Grapes.setImageResource(R.drawable.grapes);
        Grapes.setContentDescription("Grapes");
        Grapes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), grape2.class);
                startActivity(i);

            }
        });
        listView.addView(Grapes);

        ImageView Orange = new ImageView(activity);
        Orange.setImageResource(R.drawable.orange);
        Orange.setContentDescription("Orange");
        Orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), orange2.class);
                startActivity(i);

            }
        });
        listView.addView(Orange);

        ImageView Peach = new ImageView(activity);
        Peach.setImageResource(R.drawable.peach2);
        Peach.setContentDescription("Peach");
        Peach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), peach2.class);
                startActivity(i);

            }
        });
        listView.addView(Peach);
        ImageView Pepper = new ImageView(activity);
        Pepper.setImageResource(R.drawable.pepper);
        Pepper.setContentDescription("Pepper");
        Pepper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), pepper2.class);
                startActivity(i);

            }
        });
        listView.addView(Pepper);

        ImageView Potato = new ImageView(activity);
        Potato.setImageResource(R.drawable.potato);
        Potato.setContentDescription("Potato");
        Potato.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), potato2.class);
                startActivity(i);

            }
        });
        listView.addView(Potato);

        ImageView Raspberry = new ImageView(activity);
        Raspberry.setImageResource(R.drawable.raspberry);
        Raspberry.setContentDescription("Raspberry");
        Raspberry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), raspberry2.class);
                startActivity(i);

            }
        });
        listView.addView(Raspberry);





        /*

        // Define string array.
        String[] listValue = new String[] {"ONE","TWO","THREE","FOUR","FIVE","SIX","SEVEN","EIGHT"};

        @Override
        /*protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main); */

/*



            // ListView on item selected listener.
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
            {

                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // TODO Auto-generated method stub

                    // Getting listview click value into String variable.
                    String TempListViewClickedValue = listValue[position].toString();

                    Intent intent = new Intent(activity,ag1_second.class);

                    // Sending value to another activity using intent.
                    intent.putExtra("ListViewClickedValue", TempListViewClickedValue);

                    startActivity(intent);

                }
            });
            */
        return view;
    }
}

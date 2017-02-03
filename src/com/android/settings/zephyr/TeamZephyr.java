package com.android.settings.zephyr;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.settings.R;

import java.util.Random;

public class TeamZephyr extends Fragment {

    public TeamZephyr() {
        // empty fragment constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.team_zephyr, container, false);

        Random rng = new Random();

        ViewGroup crewGroup = (ViewGroup) root.findViewById(R.id.devs);

        // remove all developers from the view randomize them, add em back
        int N = crewGroup.getChildCount();
        while (N > 0) {
            View removed = crewGroup.getChildAt(rng.nextInt(N));
            crewGroup.removeView(removed);
            crewGroup.addView(removed);
            N -= 1;
        }
        return root;
    }

}

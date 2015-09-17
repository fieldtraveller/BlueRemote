package com.alex.blueremote;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class device_list_fragment extends Fragment {
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parentViewGroup,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fg, parentViewGroup, false);
        return rootView;
    }

}

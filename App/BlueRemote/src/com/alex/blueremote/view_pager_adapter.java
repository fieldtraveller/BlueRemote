package com.alex.blueremote;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class view_pager_adapter extends FragmentPagerAdapter {

	int tab_count=0;
	CharSequence titles[];
	
	ArrayList<Fragment> fragment_list;
	
	public view_pager_adapter(FragmentManager fm) {
		super(fm);
	}
	
	public view_pager_adapter(FragmentManager fm,int tab_count,CharSequence titles[]) {
		super(fm);
		
		this.tab_count=tab_count;
		this.titles=titles;
		
		fragment_list=new ArrayList<Fragment>(); 
	}
	
	@Override
	public Fragment getItem(int arg0) {
		switch(arg0)
		{
		case 0:
			fragment_list.add(new control_interface_fragment());
			return fragment_list.get(0);

		case 1:
			fragment_list.add(new terminal_fragment());
			return fragment_list.get(1);			
		}
		return null;
	}
	
	@Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

	@Override
	public int getCount() {
		return tab_count;
	}
}

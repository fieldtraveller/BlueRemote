package helper.view_pager_helper;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class view_pager_adapter extends FragmentPagerAdapter {

	CharSequence titles[];
    
    ArrayList<Fragment> fragment_list;
    fragment_initialization_interface fii;
    
    public view_pager_adapter(FragmentManager fm,fragment_initialization_interface fii) 
	{
		super(fm);
		
		this.fii=fii;
		initialize_all_fragments();
	}
	
	public view_pager_adapter(FragmentManager fm,CharSequence titles[],fragment_initialization_interface fii) 
	{
		super(fm);
		
		this.titles=titles;
		
		this.fii=fii;
		initialize_all_fragments();
	}
	
	@Override
	public Fragment getItem(int arg0) 
	{
		return fragment_list.get(arg0);
	}
	
	@Override
    public CharSequence getPageTitle(int position) 
	{
        return titles[position];
    }

	@Override
	public int getCount() 
	{
		return fragment_list.size();
	}
	
	private void initialize_all_fragments()
	{
		if(this.fii==null)
		{
			try 
			{
				throw new NullPointerException("fragment_initialization_interface=null.");
			} 
			catch (NullPointerException e) 
			{
				e.printStackTrace();
			}
		}
		
		fragment_list=new ArrayList<Fragment>();
		
		int number_of_iterations=titles.length;
		for(int count=0;count<number_of_iterations;count++)
		{
			fragment_list.add(fii.initialize_fragment(count));
		}
		
	}
	
	public interface fragment_initialization_interface
	{
		public Fragment initialize_fragment(int order_of_fragment_in_view_pager);
	}

}

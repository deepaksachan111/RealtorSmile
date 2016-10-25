package quaere.com.realtorsmile.swipeview;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import quaere.com.realtorsmile.R;

@SuppressLint("NewApi")
public class TestFragmentAdapter extends FragmentPagerAdapter implements
		IconPagerAdapter {
	

	protected int images[] = { R.drawable.screen1, R.drawable.screen2,
			R.drawable.screen3, R.drawable.screen4, R.drawable.screen5,
			R.drawable.screen6 };
	
	

	@SuppressWarnings("unused")
	private int mCount = images.length;

	@SuppressLint("NewApi")
	public TestFragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public Fragment getItem(int position) {
		return (Fragment) TestFragment.newInstance(images[position
				% images.length]);
	}

	@Override
	public int getCount() {
		return 6;
	}

	public void setCount(int count) {
		if (count > 0 && count <= 10) {
			mCount = count;
			notifyDataSetChanged();
		}
	}

	@Override
	public int getIconResId(int index) {

		return 0;
	}

}

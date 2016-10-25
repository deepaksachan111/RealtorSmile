package quaere.com.realtorsmile.swipeview;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;

import java.util.Random;

import quaere.com.realtorsmile.R;


public abstract class BaseSampleActivity extends FragmentActivity {
	private static final Random RANDOM = new Random();

	TestFragmentAdapter mAdapter;
	ViewPager mPager;
	CirclePageIndicator mIndicator;

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

}
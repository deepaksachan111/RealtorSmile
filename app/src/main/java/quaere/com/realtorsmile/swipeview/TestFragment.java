package quaere.com.realtorsmile.swipeview;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

@SuppressWarnings("unused")
public final class TestFragment extends Fragment {
   
	private static final String KEY_CONTENT = "TestFragment:Content";

   /* public static TestFragment newInstance(String content) {
        TestFragment fragment = new TestFragment();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append(content).append(" ");
        }
        builder.deleteCharAt(builder.length() - 1);
        fragment.mContent = builder.toString();

        return fragment;
    }*/

    //private String mContent = "??????";
    private int nContent ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
            mContent = savedInstanceState.getString(KEY_CONTENT);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       
       
        ImageView imgview = new ImageView(getActivity());
        imgview.setImageResource(nContent);
        
      //  View myView = getLayoutInflater().inflate(R.layout.login, null);
        LinearLayout layout = new LinearLayout(getActivity());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);
        layout.setGravity(Gravity.CENTER);
        imgview.setLayoutParams(params);
        layout.addView(imgview);

        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
       // outState.putString(KEY_CONTENT, nContent);
    }

	public static Fragment newInstance(int i) {
		// TODO Auto-generated method stub
		
		 TestFragment fragment = new TestFragment();
		 fragment.nContent = i;
		return fragment;
	}
}

package quaere.com.realtorsmile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.HttpGet;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by QServer on 2/20/2016.
 */
public class StatusAsyncTask extends AsyncTask<String, Void, String> {

    StatusAdapter statusAdapter;
    ArrayList<ModelClass> statusalllist= new ArrayList<>();
    ArrayList<String> emptNameArray =new ArrayList<>();
    ArrayList<String> emptIdArray= new ArrayList<>();


    String empName, emp_response_code, response;
    Spinner assingToSpin;
    Activity activity;
    String url;
    ProgressDialog pDialog;

    public StatusAsyncTask(Activity activity, ArrayList assignToArray, StatusAdapter assignToAdapter, Spinner assingToSpin, String url) {
        this.activity = activity;
        emptNameArray = assignToArray;
        this.statusAdapter = assignToAdapter;
        this.assingToSpin = assingToSpin;
        this.url = url;


    }

    @Override
    public void onPreExecute() {
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Loading data.."); // typically you will define such
        // strings in a remote file.
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httppost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.v("Source Response ", response);
            JSONArray jsonArray = new JSONArray(response);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jObj = jsonArray.getJSONObject(j);
                empName = jObj.getString("Status");
                Log.v("EmpName", empName);
               String ids = jObj.getString("PK_StatusID");

                emp_response_code = jObj.getString("ResponseCode");
                //Adding detail to Array
                emptNameArray.add(empName);
                emptIdArray.add(ids);
            }

            for(int i =0; i<emptNameArray.size();i++){

                ModelClass modelClass = new ModelClass();
                modelClass.setContactName(emptNameArray.get(i));
                modelClass.setContactID(emptIdArray.get(i));
                statusalllist.add(modelClass);
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception is caught here ......." + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        pDialog.dismiss();
        try {
            if (emp_response_code.equals("1")) {
                System.out.println("respnse code 1............ inside condition");
                statusAdapter = new StatusAdapter(activity, android.R.layout.simple_list_item_1, statusalllist);
                //  source_detail_adapter = new SourceDetailAdapter(AddContactBuilderActivity.this,customListArray);
                assingToSpin.setAdapter(statusAdapter);
            }

        } catch (Exception e) {
            Toast.makeText(activity, "Connection  Failed ", Toast.LENGTH_LONG).show();
        }

    }
    public class StatusAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<ModelClass> data;
        private LayoutInflater inflators = null;


        public StatusAdapter(Activity context, int textViewResourceId,
                             ArrayList<ModelClass> values) {
            this.activity = context;
            this.data = values;
            // inflators = (LayoutInflater) ((Activity)context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder{
            public TextView contactType;
            public ViewHolder(View vi) {
                contactType = (TextView) vi.findViewById(R.id.contact_spinner_rowTv);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View vi = convertView;
            ViewHolder holder;
            inflators = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                vi = inflators.inflate(R.layout.contact_type_spinner_row, null);
                holder = new ViewHolder(vi);
                vi.setTag(holder);
            }
            else

                holder = (ViewHolder) vi.getTag();

            if (data.size() <= 0) {

            } else {

                ModelClass  tempValues = (ModelClass) data.get(position);
                holder.contactType.setText(tempValues.getContactName());
            }
            return vi;
        }
    }
}
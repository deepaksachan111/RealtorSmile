package quaere.com.realtorsmile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by QServer on 2/20/2016.
 */
public class ReminderBeforeAsyncTask extends AsyncTask<String, Void, String> {

    ArrayList<ModelClass> modelClassArrayList = new ArrayList<>();
    ArrayList<String> emptNameArray= new ArrayList<>();
    ArrayList<String> emptidArray = new ArrayList<>();

    String empName, emp_response_code,response;
    ReminderBeforeAdapter reminderBeforeAdapter;
    Spinner assingToSpin;
    Activity activity;
    String url ;
    ProgressDialog pDialog;

    public ReminderBeforeAsyncTask(Activity activity,ArrayList assignToArray,ReminderBeforeAdapter assignToAdapter,Spinner assingToSpin,String url){
        this.activity = activity;
        emptNameArray = assignToArray;
        this.reminderBeforeAdapter = assignToAdapter;
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
            Log.v("Reminder Response ", response);
            JSONArray jsonArray = new JSONArray(response);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jObj = jsonArray.getJSONObject(j);
                empName = jObj.getString("Reminder");
                Log.v("EmpName", empName);

                String id = jObj.getString("PK_ReminderID");
                emp_response_code = jObj.getString("ResponseCode");
                //Adding detail to Array

                emptNameArray.add(empName);
                emptidArray.add(id);

             //   modelClassArrayList.add(new ModelClass(empName,id));

            }

            for(int i = 0; i < emptNameArray.size();i++){
                ModelClass data = new ModelClass();
                data.setContactName(emptNameArray.get(i));
                data.setContactID(emptidArray.get(i));
                modelClassArrayList.add(data);
            }

          //  emptNameArray.add("Select");

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
                reminderBeforeAdapter = new ReminderBeforeAdapter(activity,R.layout.contact_type_spinner_row, modelClassArrayList)
                {
               //  source_detail_adapter = new SourceDetailAdapter(AddContactBuilderActivity.this,customListArray);


          /*     @Override
                public boolean isEnabled(int position){
                    if(position == 0)
                    {

                        spinneractivity_meeting_relatedto2.setSelection(4);
                        // Disable the second item from Spinner
                        return false;
                    }
                    else
                    {
                        return true;
                    }
                }
*/

                    @Override
                    public int getCount() {
                        // don't display last item. It is used as hint.
                        int count = super.getCount();
                        return count > 0 ? count - 1 : count;
                    }

                @Override
                public View getDropDownView(int position, View convertView,
                        ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(R.id.contact_spinner_rowTv);
                    if(position==0) {

                        // Set the disable item text color
                        tv.setTextColor(Color.RED);
                    }
                    else {
                        tv.setTextColor(Color.BLACK);
                    }
                    return view;
                }
            };

                // reminderBeforeAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item1);
                assingToSpin.setAdapter(reminderBeforeAdapter);

               // assingToSpin .setSelection(reminderBeforeAdapter.getCount());
            }

        } catch (Exception e) {
            Toast.makeText(activity, "Connection  Failed ", Toast.LENGTH_LONG).show();
        }

    }
    public class ReminderBeforeAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<ModelClass> data;
        private LayoutInflater inflators = null;


        public ReminderBeforeAdapter(Activity context, int textViewResourceId,
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


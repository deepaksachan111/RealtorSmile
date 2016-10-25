package quaere.com.realtorsmile;

import android.app.Activity;
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
 * Created by QServer on 2/21/2016.
 */
 public class ContactTypeAsyncTask extends AsyncTask<String,Void,String> {
    ArrayList<String> nameArray,idArray;
    String name, id,response,responseCode;
    ContactTypeAdapter adapter;
    Spinner spinner;
    Activity activity;
    ArrayList<ModelClass> customListArray;
    String url ;

    public ContactTypeAsyncTask(Activity activity, ArrayList<String> typeList,ArrayList<String> idList,ArrayList<ModelClass> customListArray, ContactTypeAdapter adapter, Spinner spinner, String url){
        this.activity = activity;
        nameArray = typeList;
        idArray = idList;
        this.adapter = adapter;
        this.spinner = spinner;
        this.customListArray = customListArray;
        this.url = url;

    }

    @Override
    protected void onPreExecute() {
        customListArray.clear();
        nameArray.clear();
        idArray.clear();

    }

    @Override
    protected String doInBackground(String... strings) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httppost = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httppost);
            HttpEntity httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
            Log.v("ContactType  Response ", response);
            JSONArray jsonArray = new JSONArray(response);

            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jObj = jsonArray.getJSONObject(j);
                name = jObj.getString("ContactType");
                id = jObj.getString("PK_ContactTypeID");
                responseCode = jObj.getString("ResponseCode");
                //Adding detail to Array
                nameArray.add(name);
                idArray.add(id);
            }
            for (int k = 0; k < nameArray.size(); k++) {
                ModelClass model = new ModelClass();
                //******* Firstly take data in model object ******//**//*
                model.setContactName(nameArray.get(k));
                model.setContactID(idArray.get(k));

                // model.setPdrecordno(detailNoArray.get(k));
                customListArray.add(model);

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Exception is caught here ......." + e.toString());
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            if (responseCode.equals("1")) {
                adapter = new ContactTypeAdapter(activity,R.layout.contact_type_spinner_row, customListArray);
                //  source_detail_adapter = new SourceDetailAdapter(AddContactBuilderActivity.this,customListArray);
                spinner.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

        } catch (Exception e) {
            Toast.makeText(activity, "Connection  Failed ", Toast.LENGTH_LONG).show();
        }

    }

    public class ContactTypeAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<ModelClass> data;
        private LayoutInflater inflators = null;


        public ContactTypeAdapter(Activity context, int textViewResourceId,
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
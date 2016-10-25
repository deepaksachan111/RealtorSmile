package quaere.com.realtorsmile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
 * Created by QServer on 2/19/2016.
 */
public class RelatedToAsyncTask extends AsyncTask<String, Void, String> {

    ArrayList<String> emptNameArray;
    String relatedTo, relatedto_response_code,response;
    ArrayAdapter<String> assignToAdapter;
    Spinner assingToSpin;
    Activity activity;
    String url ;
    ProgressDialog pDialog;


    public RelatedToAsyncTask(Activity activity,ArrayList assignToArray,ArrayAdapter assignToAdapter,Spinner assingToSpin,String url){
        this.activity = activity;
        emptNameArray = assignToArray;
        this.assignToAdapter = assignToAdapter;
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
            Log.v("Related To Response ", response);
            JSONArray jsonArray = new JSONArray(response);
            emptNameArray.clear();
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject jObj = jsonArray.getJSONObject(j);
                relatedTo = jObj.getString("Related");
                String PK_RelatedID = jObj.getString("PK_RelatedID");
                Log.v("EmpName", relatedTo);
                relatedto_response_code = jObj.getString("ResponseCode");
                //Adding detail to Array
                emptNameArray.add(relatedTo);



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
            if (relatedto_response_code.equals("1")) {
                System.out.println("respnse code 1............ inside condition");

                assignToAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, emptNameArray);
                //  source_detail_adapter = new SourceDetailAdapter(AddContactBuilderActivity.this,customListArray);
                assingToSpin.setAdapter(assignToAdapter);


            }



        } catch (Exception e) {
            Toast.makeText(activity, "Connection  Failed ", Toast.LENGTH_LONG).show();
        }

    }
}

package quaere.com.realtorsmile.DocumentsActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;

public class DocumentActivity extends Activity implements View.OnClickListener{
    private ListView document_listView;
    EditText document_searchEt;
    private static int count = 0;
    private static boolean isNotAdded;
    private CheckBox checkBox_header;

    LinearLayout document_headerLayout;

    Toolbar document_toolbar;
    TextView document_home, document_add_new, document_delete;
    private ProgressDialog pDialog;

    String userId, document_Name, document_folder, document_response_code;
    String document_url;

    int noOfObjects;
    DocumentDetailAdapter document_Adapter;
    List<String> documentNameArray = new ArrayList<String>();
    List<String> documentfolderArray = new ArrayList<String>();

    public ArrayList<DocumentData> customListArray = new ArrayList<DocumentData>();
    //To save checked items, and <b>re-add</b> while scrolling.
    SparseBooleanArray mChecked = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document_activity);

        document_headerLayout = (LinearLayout) findViewById(R.id.document_listview_header);
        document_listView = (ListView) findViewById(R.id.document_list_view);
        document_searchEt = (EditText) findViewById(R.id.document_search_editText);


        document_toolbar = (Toolbar) findViewById(R.id.document_toolbar);
        document_home = (TextView) document_toolbar.findViewById(R.id.document_tool_home);
        document_add_new = (TextView) document_toolbar.findViewById(R.id.document_addNew);
        document_delete = (TextView) document_toolbar.findViewById(R.id.document_delete);
        checkBox_header = (CheckBox) document_headerLayout.findViewById(R.id.document_checkBox_header);


        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        userId = emailid.get(SessionManager.KEY_EMAIL);

        document_home.setOnClickListener(this);
        document_add_new.setOnClickListener(this);
        document_delete.setOnClickListener(this);


     ;
        //  Toast.makeText(getApplicationContext(), "you email is " + userId, Toast.LENGTH_LONG).show();

        document_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetDocumentRecords/" + userId;
        new DocumentAsyncTask().execute(document_url);
     //   document_Adapter = (new DocumentDetailAdapter(DocumentActivity.this, customListArray));
        isNotAdded = true;



        if (isNotAdded) {
            checkBox_header.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    for (int i = 0; i < customListArray.size(); i++) {
                        mChecked.put(i, checkBox_header.isChecked());
                    }

                    document_Adapter.notifyDataSetChanged();

                }
            });

            isNotAdded = false;
        }

        document_listView.setItemsCanFocus(false);
        document_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i("inside on Itemclick", "checked " + position);
                // SparseBooleanArray checked = adapter.getItem(position);
                // Use "if else" only if header is added
                if (position == 0) {
                    Toast.makeText(getApplicationContext(),
                            checkBox_header.getId() + "\n" + checkBox_header.isChecked(),
                            Toast.LENGTH_SHORT).show();
                } else {
                    // position = position - 1; // "-1" If Header is Added
                    Toast.makeText(getApplicationContext(),
                            position + "\n" + mChecked.get(position), Toast.LENGTH_SHORT).show();
                }
            }
        });
        document_searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                document_Adapter.getFilter().filter(document_searchEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


            case R.id. document_tool_home:
                onBackPressed();
                break;
            case R.id.document_addNew:
                // Toast.makeText(getApplicationContext(),"you have clicke add potential",Toast.LENGTH_SHORT).show();
                 startActivity(new Intent(DocumentActivity.this, AddNewDocumentActivity.class));
             //   startActivityForResult(new Intent(PotentialsActivity.this, PotentialAddNew.class), ACTIVITY_REQUEST_CODE);
                break;
            case R.id.document_delete:

                break;

        }

    }



    public class DocumentAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DocumentActivity.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(document_url);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("Potential  Response ", response);
                // String response = jsonResponse;


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }


        @Override
        protected void onPostExecute(String s) {


            try {

                JSONArray jsonArray = new JSONArray(s);
                noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);
                    //   Log.v("No of times " + j, "shhhhhhhhh");
                    // int k = j + 1;
                    // detailNo = ""+k;
                    document_Name = jObj.getString("DocumentName");

                    document_folder = jObj.getString("FolderName");;

                    document_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    //Adding detail to Array
                    documentNameArray.add(document_Name);

                    documentfolderArray.add(document_folder);


                }
                for (int k = 0; k < documentNameArray.size(); k++) {

                    DocumentData model = new DocumentData();

                    //******* Firstly take data in model object ******//**//*
                    model.setDocumentname(documentNameArray.get(k));
                    model.setDocumentfolder(documentfolderArray.get(k));

                    // model.setPdrecordno(detailNoArray.get(k));
                    customListArray.add(model);
                }
                document_Adapter = (new DocumentDetailAdapter(DocumentActivity.this, customListArray));
                document_listView.setAdapter(document_Adapter);

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (document_response_code.equals("1")) {

                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();
                    pDialog.dismiss();
                }

            } catch (Exception e) {
                Toast.makeText(DocumentActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }
    private class DocumentDetailAdapter extends BaseAdapter {
        private ArrayList<DocumentData> mOriginalValues; // Original Values
        private ArrayList<DocumentData> mDisplayedValues;
        private Activity activity;
        private LayoutInflater inflators = null;
        // ModelClass tempValues;
        CheckBox checkBox;

        public DocumentDetailAdapter(DocumentActivity a, ArrayList<DocumentData> customListArray) {
            activity = a;

            inflators = (LayoutInflater) ((Activity) activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mOriginalValues = customListArray;
            this.mDisplayedValues = customListArray;
        }

        @Override
        public int getCount() {
            count = mDisplayedValues.size();
            // System.out.println("count  "+count);
            return count;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup viewGroup) {
            View vi = convertView;
            if (vi == null) {

                final LayoutInflater sInflater = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                vi = sInflater.inflate(R.layout.document_listview_row, null, false);

            }


            final TextView potentialNameTv = (TextView) vi.findViewById(R.id.potential_row_nameTv);
            final TextView potentialTitleTv = (TextView) vi.findViewById(R.id.potential_row_titleTv);
            checkBox = (CheckBox) vi.findViewById(R.id.potential_row_checkbox);

                  /* **************ADDING CONTENTS**************** */

            DocumentData tempValues = (DocumentData) mDisplayedValues.get(position);
            potentialNameTv.setText(tempValues.getDocumentname());
            potentialTitleTv.setText(tempValues.getDocumentfolder());
            potentialNameTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "you have clicked " + potentialNameTv.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
            // holder.joiningDateTv.setText(tempValues.getJoiningDate());
            checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                mChecked.put(position, isChecked);

                                if (isAllValuesChecked()) {

                                    checkBox_header.setChecked(isChecked);
                                }

                            } else {

                                /*
                                 * Removed UnChecked Position
                                 */
                                mChecked.delete(position);

                                /*
                                 * Remove Checked in Header
                                 */
                                checkBox_header.setChecked(isChecked);

                            }

                        }
                    });

            /*
             * Set CheckBox "TRUE" or "FALSE" if mChecked == true
             */
            checkBox.setChecked((mChecked.get(position) == true ? true : false));

            return vi;
        }

        //Find if all values are checked.

        protected boolean isAllValuesChecked() {

            for (int i = 0; i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }

        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    mDisplayedValues = (ArrayList<DocumentData>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<DocumentData> FilteredArrList = new ArrayList<DocumentData>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<DocumentData>(mDisplayedValues); // saves the original data in mOriginalValues
                    }

                    /********
                     *
                     *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                     *  else does the Filtering and returns FilteredArrList(Filtered)
                     *
                     ********/
                    if (constraint == null || constraint.length() == 0) {

                        // set the Original result to return
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        constraint = constraint.toString().toLowerCase();
                        for (int i = 0; i < mOriginalValues.size(); i++) {
                            String name = mOriginalValues.get(i).getDocumentname();
                            String conType = mOriginalValues.get(i).getDocumentfolder();

                            if (name.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new DocumentData(mOriginalValues.get(i).getDocumentname(), mOriginalValues.get(i).getDocumentfolder()));
                            } else if (conType.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new DocumentData(mOriginalValues.get(i).getDocumentname(),  mOriginalValues.get(i).getDocumentfolder()));
                            }

                        }
                        // set the Filtered result to return
                        results.count = FilteredArrList.size();
                        results.values = FilteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }
    }


}

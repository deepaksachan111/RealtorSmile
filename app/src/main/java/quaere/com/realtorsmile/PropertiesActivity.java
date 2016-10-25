package quaere.com.realtorsmile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by intex on 1/13/2016.
 */
public class PropertiesActivity extends Activity {
    private ListView listView;
    EditText searchFilterEt;
    private static int count = 0;
    private  boolean isNotAdded;
    private CheckBox checkBox_header;
    RelativeLayout headerLayout;
    Spinner searchSpinner;
    Button searchBtn;
    Toolbar toolbar;
    TextView home, add_new, mail, msg, delete;
    private ProgressDialog pDialog;
    List<String> searchFor;
    //  static String[] textviewContent;
    String userId, contactName, contact_type, contact_mobile, contact_response_code;
    String contact_url;
    int noOfObjects;
    ContactDetailAdapter1 adapter;
    List<String> contactNameArray = new ArrayList<String>();
    List<String> contact_typeArray = new ArrayList<String>();
    List<String> contact_mobileArray = new ArrayList<String>();

    public ArrayList<ModelClass> customListArray = new ArrayList<ModelClass>();

    //To save checked items, and <b>re-add</b> while scrolling.
    SparseBooleanArray mChecked = new SparseBooleanArray();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.properties_activity);
        headerLayout = (RelativeLayout) findViewById(R.id.contact_list_view_header);
        listView = (ListView) findViewById(R.id.prop_list_view);
        searchFilterEt = (EditText) findViewById(R.id.properties_search_editText);
        searchSpinner = (Spinner) findViewById(R.id.contact_search_spinner);
        searchBtn = (Button) findViewById(R.id.contact_search_btn);

        toolbar = (Toolbar) findViewById(R.id.contact_toolbar);
        home = (TextView) toolbar.findViewById(R.id.contact_tool_home);
        add_new = (TextView) toolbar.findViewById(R.id.contact_addNew);
        mail = (TextView) toolbar.findViewById(R.id.contact_mail);
        msg = (TextView) toolbar.findViewById(R.id.contact_sms);
        delete = (TextView) toolbar.findViewById(R.id.contact_delete);
        checkBox_header = (CheckBox) headerLayout.findViewById(R.id.checkBox_header);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        userId = emailid.get(SessionManager.KEY_EMAIL);


        userId = getIntent().getStringExtra("userId");
        Toast.makeText(getApplicationContext(), "you email is " + userId, Toast.LENGTH_LONG).show();

        contact_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetContactRecords/" + userId;
        new ContactAsyncTask().execute(contact_url);


        isNotAdded = true;
        adapter = (new ContactDetailAdapter1(PropertiesActivity.this,customListArray));
        // contactDetailAdapter = (new ContactDetailAdapter(ContactsActivity.this,customListArray));
        // Spinner Drop down elements
        searchFor = new ArrayList<String>();
        searchFor.add("All");
        searchFor.add("FullName");
        searchFor.add("ContactType");
        searchFor.add("Mobile");

        //adding data to search spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, searchFor);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(dataAdapter);

        if (isNotAdded) {
            checkBox_header.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    for (int i = 0; i < customListArray.size(); i++) {
                        mChecked.put(i, checkBox_header.isChecked());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
            isNotAdded = false;
        }

        searchFilterEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                // adapter.getFilter().filter(cs.toString());
                adapter.getFilter().filter(searchFilterEt.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //  adapter.getFilter().filter(searchFilterEt.getText().toString());

            }
        });
    }


    public class ContactAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PropertiesActivity.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(contact_url);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("Contact  Response ", response);
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
                    contactName = jObj.getString("FirstName");

                    contact_type = jObj.getString("ContactType");

                    contact_mobile = jObj.getString("Mobile");
                    contact_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);

                    //Adding detail to Array
                    contactNameArray.add(contactName);
                    contact_mobileArray.add(contact_mobile);
                    contact_typeArray.add(contact_type);


                }
                for (int k = 0; k < contactNameArray.size(); k++) {

                    final ModelClass model = new ModelClass();

                    //******* Firstly take data in model object ******//**//*
                    model.setContactName(contactNameArray.get(k));
                    model.setContact_ConType(contact_typeArray.get(k));
                    model.setContact_mobile(contact_mobileArray.get(k));
                    // model.setPdrecordno(detailNoArray.get(k));
                    customListArray.add(model);
                }
                listView.setAdapter(adapter);
                //  listView.setItemsCanFocus(true);
                //  listView.setOnItemClickListener(PropertiesActivity.this);

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contact_response_code.equals("1")) {

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
                Toast.makeText(PropertiesActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }

        }
    }

    // CustomAdapter
    public class ContactDetailAdapter1 extends BaseAdapter implements Filterable{

        private Activity activity;
        private LayoutInflater inflators = null;
        // ModelClass tempValues;
        CheckBox checkBox;
        // SparseBooleanArray mChecked= listView.getCheckedItemPositions();
        private ArrayList<ModelClass> mOriginalValues; // Original Values
        private ArrayList<ModelClass> mDisplayedValues;    // Values to be displayed
        /* public ContactDetailAdapter1(PropertiesActivity contactsActivity) {
             activity = contactsActivity;

             inflators = (LayoutInflater) ((Activity) activity)
                     .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         }
 */
        public ContactDetailAdapter1(PropertiesActivity propertiesActivity, ArrayList<ModelClass> customListArray) {
            activity = propertiesActivity;
            inflators = (LayoutInflater) ((Activity) activity).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.mOriginalValues = customListArray;
            this.mDisplayedValues = customListArray;
        }

        @Override
        public int getCount() {
            // Length of our listView
            count = mDisplayedValues.size();
            return count;
        }

        @Override
        public Object getItem(int position) {
            int itemPostion = position;

            return position;
        }

        @Override
        public long getItemId(int position) {

            // Current Item's ID
            return position;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint,FilterResults results) {

                    mDisplayedValues = (ArrayList<ModelClass>) results.values; // has the filtered values
                    notifyDataSetChanged();  // notifies the data with new filtered values
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                    ArrayList<ModelClass> FilteredArrList = new ArrayList<ModelClass>();

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<ModelClass>(mDisplayedValues); // saves the original data in mOriginalValues
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
                            String name = mOriginalValues.get(i).getContactName();
                            String conType = mOriginalValues.get(i).getContact_ConType();
                            String mobileNo = mOriginalValues.get(i).getContact_mobile();
                            if (name.toLowerCase().startsWith(constraint.toString())) {
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(),mOriginalValues.get(i).getContact_mobile(),mOriginalValues.get(i).getContact_ConType()));
                            }
                            else if(conType.toLowerCase().startsWith(constraint.toString())){
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(),mOriginalValues.get(i).getContact_mobile(),mOriginalValues.get(i).getContact_ConType()));
                            }
                            else if(mobileNo.toLowerCase().startsWith(constraint.toString())){
                                FilteredArrList.add(new ModelClass(mOriginalValues.get(i).getContactName(),mOriginalValues.get(i).getContact_mobile(),mOriginalValues.get(i).getContact_ConType()));
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


        public class ViewHolder {


            public ViewHolder(View vi) {


            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View vi = convertView;

            if (vi == null) {

                /*
                 * LayoutInflater
                 */
                final LayoutInflater sInflater = (LayoutInflater) activity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

                /*
                 * Inflate Custom List View
                 */
                vi = sInflater.inflate(R.layout.contact_list_view_row, null, false);

            }


            final TextView  contactNameTv = (TextView) vi.findViewById(R.id.contact_fullNameTv);
            final TextView contactTypeTv = (TextView) vi.findViewById(R.id.contact_typeTv);
            final TextView contactMobileNoTv = (TextView) vi.findViewById(R.id.contact_mobileNoTv);
            checkBox = (CheckBox) vi.findViewById(R.id.contact_checkBox);

                  /* **************ADDING CONTENTS**************** */

            ModelClass tempValues = (ModelClass) mDisplayedValues.get(position);
            contactNameTv.setText(tempValues.getContactName());
            contactTypeTv.setText(tempValues.getContact_ConType());
            contactMobileNoTv.setText(tempValues.getContact_mobile());
            // vi.setClickable(true);
            // vi.setFocusable(true);
            vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    new AlertDialog.Builder(PropertiesActivity.this).setTitle("touched").show();
                    Toast.makeText(view.getContext(),"you have clicked "+contactNameTv.getText().toString()+"\n"+""+contactTypeTv.getText().
                            toString()+"\n"+""+contactMobileNoTv.getText().toString(),Toast.LENGTH_SHORT).show();
                }
            });
            // holder.joiningDateTv.setText(tempValues.getJoiningDate());
            checkBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {

                                /*
                                 * Saving Checked Position
                                 */
                                mChecked.put(position, isChecked);

                                /*
                                 * Find if all the check boxes are true
                                 */
                                if (isAllValuesChecked()) {

                                    /*
                                     * set HeaderCheck box to true
                                     */
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

            return vi  ;
        }
        /*
         * Find if all values are checked.
         */

        protected boolean isAllValuesChecked() {

            for (int i = 0; i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
        }


    }

}

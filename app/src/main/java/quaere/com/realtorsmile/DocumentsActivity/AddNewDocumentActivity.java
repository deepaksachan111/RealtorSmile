package quaere.com.realtorsmile.DocumentsActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;


import quaere.com.realtorsmile.AssignToAsyncTask;
import quaere.com.realtorsmile.ModelClass;
import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.RelatedToAsyncTask;
import quaere.com.realtorsmile.SessionManager;

public class AddNewDocumentActivity extends Activity implements View.OnClickListener {


    private static final int REQUEST_PICK_FILE = 1;
    private boolean flag = true;
    private File selectedFile;
    private static final int PICKFILE_RESULT_CODE = 1;
    private Button btn_choosefile, btn_save, btn_save_n_new, btn_clear;
    private EditText edt_document_name, edt_setchoosefile, edt_description, edt_keywords, edt_customsharing, edt_addnewdoucument_chosefile;
    private Spinner spinner_doc_assignto, spinner_doc_folder, spinner_relatedto1, spinner_relatedto2;
    private ImageView iv_folder;
    private TextView textview, tv_googledrive, tv_realtorsmile_drive;
    private RadioGroup radioGroup_visibility;
    private LinearLayout linearLayout_choosefile,linear_customsharing;
    private RadioButton radio_document_customsharing ;

    private String userid;
    String selected_realatedto;
    ArrayList<String> relatedtolist1 = new ArrayList<>();
    ArrayList<String> relatedtolist2 = new ArrayList<>();


    AssignToAsyncTask.AssignToAdapter assignToAdapter ;
    ArrayAdapter relatedtoarrayAdapter1;
    ArrayAdapter relatedtoarrayAdapter2;

    ArrayList<String> addfolderlist = new ArrayList<>();
    ArrayAdapter addfolderadapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_document);
        findID();

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String,String> map = sessionManager.getUserDetails();
          userid = map.get(SessionManager.KEY_EMAIL);
        btn_choosefile.setOnClickListener(this);
        tv_googledrive.setOnClickListener(this);
        tv_realtorsmile_drive.setOnClickListener(this);
        edt_addnewdoucument_chosefile.setOnClickListener(this);
        linearLayout_choosefile.setOnClickListener(this);
        edt_customsharing.setOnClickListener(this);

        edt_description.setOnClickListener(this);
        edt_keywords.setOnClickListener(this);
        iv_folder.setOnClickListener(this);
        edt_document_name.setOnClickListener(this);
        radio_document_customsharing.setOnClickListener(this);
        linear_customsharing.setOnClickListener(this);


        relatedtolist1.add("Property");
        relatedtolist1.add("Potential");
        relatedtolist1.add("Project");
        relatedtolist1.add("Listing");
        relatedtolist1.add("Case");
        relatedtolist1.add("Closing");
        relatedtolist1.add("Booking");
        relatedtolist1.add("Transaction");

     relatedtoarrayAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,relatedtolist1);
        spinner_relatedto1.setAdapter(relatedtoarrayAdapter1);
        spinner_relatedto1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_realatedto = parent.getItemAtPosition(position).toString();
                Log.v("Selected ITem :", selected_realatedto);

                String url = "http://rsmile.quaeretech.com/RealtorSmile.svc/GetRelated/" + userid + "/" + selected_realatedto;
                 RelatedToAsyncTask relatedtoAsyncTask=  new RelatedToAsyncTask(AddNewDocumentActivity.this,relatedtolist2,relatedtoarrayAdapter2,spinner_relatedto2,url);
               relatedtoAsyncTask.execute();
              //  new RelatedtoAsyncTask().execute(url);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ArrayList<String> assignToArray = new ArrayList<String>();
        String assignto_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/getEmployee/"+userid;
        AssignToAsyncTask assgnTo = new AssignToAsyncTask(this,assignToArray,assignToAdapter,spinner_doc_assignto,assignto_url);
        assgnTo.execute();



    }

    private void addFolder() {

        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.dialogeaddnewgeneral);

        final EditText contact_grpEt = (EditText) openDialog.findViewById(R.id.et_dialogeaddnewgeneral_contactgroup1);
        Button save = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_save1);
        Button close = (Button) openDialog.findViewById(R.id.btn_dialogeaddnewgeneral_close1);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addfolderlist.add(contact_grpEt.getText().toString());

                openDialog.dismiss();

             addfolderadapter = new ArrayAdapter(AddNewDocumentActivity.this,android.R.layout.simple_list_item_1,addfolderlist);
                spinner_doc_folder.setAdapter(addfolderadapter);
                //  build_groupEt.setText(myItem);
           //     Toast.makeText(getApplicationContext(), gettexteditext2 + "Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });
        // AlertDialog alertDialog = openDialog.create();
     /*   Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        openDialog.getWindow().getAttributes().verticalMargin = 0.50F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);*/

        WindowManager.LayoutParams wmlp = openDialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.TOP | Gravity.RIGHT;
        wmlp.x = 90;   //x position
        wmlp.y = 230;   //y position
        wmlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        openDialog.show();
    }

    private void findID() {
        radio_document_customsharing =(RadioButton)findViewById(R.id.radio_document_custom);
        btn_choosefile = (Button) findViewById(R.id.btn_addnewdocument_choosefile);
        edt_document_name = (EditText) findViewById(R.id.addnewdocument_edt_documentname);
        spinner_doc_assignto = (Spinner) findViewById(R.id.addnewdocument_spiner_assignto);
        spinner_doc_folder = (Spinner) findViewById(R.id.ed_addnewdocument_spinner_folder);
        iv_folder = (ImageView) findViewById(R.id.iv_documentfolder);
        edt_description = (EditText) findViewById(R.id.edt_addnewdocument_edt_description);
        edt_keywords = (EditText) findViewById(R.id.edt_addnewdocument_edt_keywords);

        spinner_relatedto1 = (Spinner) findViewById(R.id.addnewdocument_spiner_relatedto);
        spinner_relatedto2 = (Spinner) findViewById(R.id.addnewdocument_spiner_relatedto2);

        edt_customsharing = (EditText) findViewById(R.id.edt_addnewdocument_customsharing);
        linear_customsharing = (LinearLayout) findViewById(R.id.linearlayout_addnewdocument_customsharing);

        tv_googledrive = (TextView) findViewById(R.id.tv_google_drive_attachment);
        tv_realtorsmile_drive = (TextView) findViewById(R.id.tv_realtorsmile_attachment);
        edt_addnewdoucument_chosefile = (EditText) findViewById(R.id.edt_addnewdoucument_chosefile);
        linearLayout_choosefile = (LinearLayout) findViewById(R.id.layout_linear_choosefile);
        btn_save = (Button) findViewById(R.id.btn_addnewdocument_save);
        btn_save_n_new = (Button) findViewById(R.id.btn_addnewdocument_saveandnew);
        btn_clear = (Button) findViewById(R.id.btn_addnewdoucument_cancel);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_realtorsmile_attachment:
                //attachFile();
                if (flag) {
                    linearLayout_choosefile.setVisibility(View.VISIBLE);
                    flag = false;
                } else {
                    linearLayout_choosefile.setVisibility(View.INVISIBLE);
                    flag = true;
                }
                break;
            case R.id.tv_google_drive_attachment:
                linearLayout_choosefile.setVisibility(View.GONE);
                break;
            case R.id.btn_addnewdocument_choosefile:
                linearLayout_choosefile.setVisibility(View.VISIBLE);
                Intent intent = new Intent(this, FilePicker.class);
                startActivityForResult(intent, REQUEST_PICK_FILE);
                break;
            case R.id.radio_document_custom:
                if(flag) {
                    linear_customsharing.setVisibility(View.VISIBLE);
                    flag = false;
                }else{
                    linear_customsharing.setVisibility(View.GONE);
                    flag = true;
                }
                break;
            case R.id.iv_documentfolder:
                addFolder();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_PICK_FILE:

                    if (data.hasExtra(FilePicker.EXTRA_FILE_PATH)) {

                        selectedFile = new File
                                (data.getStringExtra(FilePicker.EXTRA_FILE_PATH));
                        edt_addnewdoucument_chosefile.setText(selectedFile.getPath());
                    }
                    break;
            }
        }
    }

    /*private void attachFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file*//*");
        startActivityForResult(intent,PICKFILE_RESULT_CODE);
    }*/
   /* public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    String FilePath = data.getData().getPath();
                    edt_addnewdoucument_chosefile.setText(FilePath);
                }
                break;
        }

    }*/

    /*private class RelatedtoAsyncTask2 extends AsyncTask<String, Void, String> {
           ProgressDialog  pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddNewDocumentActivity.this);
            pDialog.setMessage("Loading Details ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = params[0];
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(url);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                Log.v("RelatedTO  Response ", response);
                // String response = jsonResponse;


            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();
            relatedtolist2.clear();
            try {
                JSONArray jsonArray = new JSONArray(s);
                int noOfObjects = jsonArray.length();
                Log.v("Number of json Obj " + noOfObjects, "   pd Objects.....");
                // pd.dismiss();
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    String relatedto = jObj.getString("Related");
                    String relatedto_response_code = jObj.getString("ResponseCode");
                    // Log.v("ResponseCode",response_code);
                    relatedtolist2.add(relatedto);
                }

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(AddNewDocumentActivity.this,android.R.layout.simple_list_item_1,relatedtolist2);
                spinner_relatedto2.setAdapter(arrayAdapter2);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
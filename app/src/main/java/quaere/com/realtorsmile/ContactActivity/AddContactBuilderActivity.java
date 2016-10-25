package quaere.com.realtorsmile.ContactActivity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import quaere.com.realtorsmile.R;
import quaere.com.realtorsmile.SessionManager;


public class AddContactBuilderActivity extends Activity implements OnClickListener {
    EditText  firstNameEt, lastNameEt, companyEt, designationEt, emailEt, phoneEt, remarksEt, customVisibilityEt,sourceEt;
    EditText build_groupEt;
    Button builder_grp_btn, add_builder_source_btn, save_btn, save_n_newBtn, cancelBtn, upload_pic_Btn,back_btn;
    TextView profile_pic_path;
    public static Boolean show_dropdown = true;
    public static Boolean source_dropdown= true;
    String new_builder_group,new_source_item,userId,source_detail_url;
    ListView add_group_dropdown_list,source_list;
    ArrayAdapter<String> adapter;
    ArrayList<String> builder_group_item;
    ArrayList<String> builder_source_item;
    private static final int CAPTURE_IMAGE = 0;
    private static final int SELECT_PICTURE = 1;
    private static String  new_builder_group_text= "";
    ArrayAdapter source_detail_adapter;
    private String image_path,contactName,contact_response_code;
    List<String> title_list = new ArrayList<String>();
    List<String> contactNameArray = new ArrayList<String>();
    ArrayAdapter titleAdapter,sourceAdapter;
    Spinner titleSpin,source_details_spin;
    RadioButton public_rb, private_rb, myteam_rb, custom_rb;
    RadioGroup radGrp;
    int noOfObjects;
    PopupWindow sourcePopupWindow,builder_group_popup_window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       /* if (savedInstanceState != null) // Check that the object exists
            return(savedInstanceState);
        return super.onRetainNonConfigurationInstance();*/
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_builder_contact);

        build_groupEt = (EditText) findViewById(R.id.add_builder_groupEt);
        firstNameEt = (EditText) findViewById(R.id.add_builder_firstNameEt);
        lastNameEt = (EditText) findViewById(R.id.add_builder_lastNameEt);
        companyEt = (EditText) findViewById(R.id.add_builder_companyNameEt);
        designationEt = (EditText) findViewById(R.id.add_builder_designationEt);
        emailEt = (EditText) findViewById(R.id.add_builder_emailEt);
        phoneEt = (EditText) findViewById(R.id.add_builder_phoneEt);
        remarksEt = (EditText) findViewById(R.id.add_builder_remarksEt);
        customVisibilityEt = (EditText) findViewById(R.id.add_builder_cusotmVisibilityEt);
        sourceEt = (EditText) findViewById(R.id.add_builder_sourceEt);
        profile_pic_path = (TextView) findViewById(R.id.add_builder_uploadPicTv);


        builder_grp_btn = (Button) findViewById(R.id.add_builder_grouptBtn);
        add_builder_source_btn = (Button) findViewById(R.id.add_builder_sourceBtn);
        save_btn = (Button) findViewById(R.id.add_builder_saveBtn);
        save_n_newBtn = (Button) findViewById(R.id.add_builder_save_n_NewBtn);
       // back_btn = (Button) findViewById(R.id.add_builder_back_btn);
        cancelBtn = (Button) findViewById(R.id.add_builder_cancelBtn);
        upload_pic_Btn = (Button) findViewById(R.id.add_builder_uploadpicBtn);
        public_rb = (RadioButton) findViewById(R.id.add_builder_publicRb);
        private_rb = (RadioButton) findViewById(R.id.add_builder_privateRb);
        myteam_rb = (RadioButton) findViewById(R.id.add_builder_myTeamRb);
        custom_rb = (RadioButton) findViewById(R.id.add_builder_customRb);
        radGrp = (RadioGroup) findViewById(R.id.add_builder_radiogroup);

        titleSpin = (Spinner) findViewById(R.id.add_builder_title_spinner);
        source_details_spin = (Spinner) findViewById(R.id.add_builder_sourceDetailSpin);


        Toast.makeText(getApplicationContext(),"you email is "+userId,Toast.LENGTH_LONG).show();

        build_groupEt.setOnClickListener(this);
        builder_grp_btn.setOnClickListener(this);
        add_builder_source_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        save_n_newBtn.setOnClickListener(this);
        cancelBtn.setOnClickListener(this);
        upload_pic_Btn.setOnClickListener(this);
        sourceEt.setOnClickListener(this);
        //back_btn.setOnClickListener(this);

        builder_group_item = new ArrayList<String>();
        builder_source_item = new ArrayList<String>();


        builder_source_item.clear();
        builder_source_item.add("General");
        builder_source_item.add("Builder");
        builder_source_item.add("Vendor");
        builder_source_item.add("Client");
        builder_source_item.add("Agent");
        builder_source_item.add("Zafar");
        sourceAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, builder_source_item);

        SessionManager sessionManager = new SessionManager(this);
        HashMap<String, String> emailid = sessionManager.getUserDetails();
        //  data = emailid.get(SessionManager.KEY_EMAIL);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, builder_group_item);

        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if (id == R.id.add_builder_publicRb) {
                    customVisibilityEt.setVisibility(View.GONE);

                } else if (id == R.id.add_builder_privateRb) {
                    customVisibilityEt.setVisibility(View.GONE);

                } else if (id == R.id.add_builder_myTeamRb) {
                    customVisibilityEt.setVisibility(View.GONE);

                } else if (id == R.id.add_builder_customRb) {

                    customVisibilityEt.setVisibility(View.VISIBLE);
                }
            }
        });
        title_list.clear();
        title_list.add("Mr");
        title_list.add("Ms");
        title_list.add("Dr");
        title_list.add("Er");
        title_list.add("Lt");
        title_list.add("Sri");
        titleAdapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_text, title_list);
        //  fNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpin.setAdapter(titleAdapter);





       /* add_group_dropdown_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = ((TextView) view).getText().toString();
                new_builder_group_text = new_builder_group_text + item + ",";
                build_groupEt.setText(new_builder_group_text);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
            }
        });*/
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //Dropdown for add builder group
            case R.id.add_builder_groupEt:
                if (show_dropdown) {
                    Toast.makeText(getApplicationContext(), "Show dropdown true", Toast.LENGTH_SHORT).show();
                    builderGroupPopupList(view);
                    show_dropdown = false;
                } else {
                   builder_group_popup_window.dismiss();
                    show_dropdown = true;
                }

                break;
            //Dropdown for add source item
            case R.id.add_builder_sourceEt:
                if(source_dropdown) {
                    sourcePopupList(view);
                    source_dropdown = false;
                }
                else{
                 //   sourcePopupWindow.dismiss();
                    source_dropdown = true;
                }
                break;

            case R.id.add_builder_grouptBtn:
                addBuilderGroup();
                break;


            case R.id.add_builder_sourceBtn:
                addBuilderSource();
                break;

            case R.id.add_builder_uploadpicBtn:
                selectImage();
                break;

           /* case R.id.add_builder_back_btn:
                // onBackPressed();
                setResult(Activity.RESULT_CANCELED, new Intent(AddContactBuilderActivity.this, ContactsActivity.class));
                finish();
                break;*/
        }
    }

    private void builderGroupPopupList(View v) {
        LayoutInflater layoutInflater = (LayoutInflater) AddContactBuilderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.add_builder_group_popup_window, (ViewGroup) findViewById(R.id.builder_group_popup_window));
        View dummyview = (View)popupView.findViewById(R.id.group_dropdown_dummyView);

        dummyview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder_group_popup_window.dismiss();
            }
        });
        add_group_dropdown_list = (ListView) popupView.findViewById(R.id.add_builder_dropdown_list);
        builder_group_popup_window = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        add_group_dropdown_list.setAdapter(adapter);

        add_group_dropdown_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                new_builder_group_text = new_builder_group_text + item + ",";
                build_groupEt.setText(new_builder_group_text);
                Toast.makeText(getBaseContext(), item, Toast.LENGTH_LONG).show();
                builder_group_popup_window.dismiss();
            }
        });

        builder_group_popup_window.setOutsideTouchable(true);
        builder_group_popup_window.setTouchable(true);
        builder_group_popup_window.setBackgroundDrawable(new BitmapDrawable());
        builder_group_popup_window.update();
        builder_group_popup_window.showAsDropDown(v);
    }


    private void addBuilderGroup() {

        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.add_builder_group_dialog);

        final EditText contact_grpEt = (EditText) openDialog.findViewById(R.id.builder_group_dialogEt);
        Button save = (Button) openDialog.findViewById(R.id.builder_group_dialogSaveBtn);
        Button close = (Button) openDialog.findViewById(R.id.builder_group_dialogCloseBtn);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new_builder_group = contact_grpEt.getText().toString();
                builder_group_item.add(new_builder_group);
                adapter.notifyDataSetChanged();
                openDialog.dismiss();
                new_builder_group_text = new_builder_group_text + new_builder_group + ",";
                build_groupEt.setText(new_builder_group_text);
                Toast.makeText(getApplicationContext(), new_builder_group + " Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.TOP);
        openDialog.getWindow().getAttributes().verticalMargin = 0.17F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        openDialog.show();
    }

    // Source popup window as a dropdown view
    private void sourcePopupList(View v) {

        LayoutInflater layoutInflater = (LayoutInflater) AddContactBuilderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.add_builder_source_popup_window, (ViewGroup) findViewById(R.id.source_popup_window));
        source_list = (ListView) popupView.findViewById(R.id.add_builder_source_list);

        sourcePopupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,true);
        sourcePopupWindow.setContentView(popupView);
        sourcePopupWindow.showAsDropDown(v);
        sourcePopupWindow.setOutsideTouchable(true);
        sourcePopupWindow.setTouchable(true);
        sourcePopupWindow.setFocusable(true);


        sourcePopupWindow.setBackgroundDrawable(new BitmapDrawable());

        View dummyview = (View)popupView.findViewById(R.id.source_dropdown_dummyView);
        dummyview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                sourcePopupWindow.dismiss();
            }
        });

        source_list.setAdapter(sourceAdapter);
        source_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                String popUpItem = adapterView.getItemAtPosition(i).toString();

                if (popUpItem.equals("General") || popUpItem.equals("Agent") || popUpItem.equals("Builder") || popUpItem.equals("Vendor") || popUpItem.equals("Client")) {
                    source_details_spin.setVisibility(View.VISIBLE);
                    addBuilderSourceDetails(popUpItem);

                } else {
                    source_details_spin.setVisibility(View.GONE);
                }

                sourceEt.setText(popUpItem);
                sourcePopupWindow.dismiss();
            }
        });



    }

    private void addBuilderSourceDetails(String popUpItem) {
        //   Toast.makeText(getApplicationContext(), "inside addBuilderSourceDetails(String popUpItem):" + popUpItem, Toast.LENGTH_SHORT).show();
        source_detail_url = "http://rsmile.quaeretech.com/RealtorSmile.svc/SearchContact/" + popUpItem + "/ContactType/" + userId;

        new SourceDetailAsyncTask().execute();
    }


    private void selectImage() {
        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.add_builder_upload_pic_dialog);
        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        openDialog.getWindow().getAttributes().verticalMargin = 0.09F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        openDialog.show();

        final TextView galleryImgTv = (TextView) openDialog.findViewById(R.id.gallery_image);
        final TextView cameraImgTv = (TextView) openDialog.findViewById(R.id.camera_image);

        galleryImgTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, SELECT_PICTURE);
                openDialog.dismiss();

            }
        });

        cameraImgTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAPTURE_IMAGE);
                openDialog.dismiss();

            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE) {
            onCaptureImageResult(data);

        } else if (requestCode == SELECT_PICTURE) {
            Uri selectedImageUri = data.getData();
            // String uri = selectedImageUri.toString();
            image_path = getRealPathFromURI(selectedImageUri);
            System.out.println("Image Path : " + image_path);
            // Bitmap bitmap= BitmapFactory.decodeFile(selectedImagePath);
            // setImg.setImageBitmap(bitmap);
            //setImg.setImageURI(selectedImageUri);
            profile_pic_path.setText(image_path);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
        String path = (destination.getAbsolutePath().substring(destination.getAbsolutePath().lastIndexOf("\\") + 1));
        profile_pic_path.setText("");
        profile_pic_path.setText(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }




    private void addBuilderSource() {
        final Dialog openDialog = new Dialog(this);
        openDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        openDialog.setContentView(R.layout.add_builder_source_dialog);

        final EditText contact_grpEt = (EditText) openDialog.findViewById(R.id.builder_source_dialogEt);
        Button save = (Button) openDialog.findViewById(R.id.builder_source_dialogSaveBtn);
        Button close = (Button) openDialog.findViewById(R.id.builder_source_dialogCloseBtn);
        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog.dismiss();
            }
        });

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new_source_item = contact_grpEt.getText().toString();
                builder_source_item.add(new_source_item);
                sourceAdapter.notifyDataSetChanged();
                openDialog.dismiss();
                // new_builder_source_text= new_builder_source_text + new_source_item+",";
                sourceEt.setText(new_source_item);
                Toast.makeText(getApplicationContext(), new_source_item + " Saved Successfully", Toast.LENGTH_SHORT).show();
            }
        });


        // AlertDialog alertDialog = openDialog.create();
        Window window = openDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        openDialog.getWindow().getAttributes().verticalMargin = 0.2F;
        openDialog.getWindow().getAttributes().horizontalMargin = 0.09F;
        wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        openDialog.show();

    }

    private class SourceDetailAsyncTask extends AsyncTask<String,Void,String>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            contactNameArray.clear();
        }

        @Override
        protected String doInBackground(String... params) {
            String response = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet(source_detail_url);

            try {
                HttpResponse httpResponse = httpClient.execute(httppost);
                HttpEntity httpEntity = httpResponse.getEntity();
                response = EntityUtils.toString(httpEntity);
                // Log.v("Search  Response ", response);



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
                for (int j = 0; j < jsonArray.length(); j++) {

                    JSONObject jObj = jsonArray.getJSONObject(j);

                    contactName = jObj.getString("FirstName");
                    // Log.v("contactName",contactName);
                    contact_response_code = jObj.getString("ResponseCode");

                    //Adding detail to Array
                    contactNameArray.add(contactName);


                }

            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println(" Exception is caught here ......." + e.toString());
            }


            try {
                if (contact_response_code.equals("1")) {
                    System.out.println("respnse code 1............ inside condition");
                    source_detail_adapter = new ArrayAdapter<String>(AddContactBuilderActivity.this, android.R.layout.simple_list_item_1, contactNameArray);
                    //  source_detail_adapter = new SourceDetailAdapter(AddContactBuilderActivity.this,customListArray);
                    source_details_spin.setAdapter(source_detail_adapter);
                    //  source_detail_adapter.notifyDataSetChanged();
                    Toast toast = Toast.makeText(getApplicationContext(), "Total Records are : " + noOfObjects, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "No Records Founds ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 0);
                    toast.getView().setPadding(20, 20, 20, 20);
                    toast.getView().setBackgroundColor(Color.parseColor("#7CB342"));
                    toast.show();

                }

            } catch (Exception e) {
                Toast.makeText(AddContactBuilderActivity.this, "Connection  Failed ", Toast.LENGTH_LONG).show();

            }

        }
    }
}

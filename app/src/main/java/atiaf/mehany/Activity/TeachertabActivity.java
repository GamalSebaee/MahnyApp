package atiaf.mehany.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Customecalss.CustomViewPager;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.Workerimg;
import atiaf.mehany.Fragment.TeacherNotFragment;
import atiaf.mehany.Fragment.TeacherorderFragment;
import atiaf.mehany.Fragment.TeacherprofileFragment;
import atiaf.mehany.Fragment.TeachersettingFragment;
import atiaf.mehany.R;
import atiaf.mehany.Service.SendLocationService;

public class TeachertabActivity extends FragmentActivity {
    private TabLayout tabLayout;
    public static CustomViewPager viewPager;
    public static Context context ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workertab);
        context = this;
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_list_1);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_2);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_2);
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_more_2);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int numTab = tab.getPosition();
                if (numTab == 0) {
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_list_2);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_2);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_2);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_more_1);
                } else if (numTab == 1) {
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_list_2);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_2);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_1);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_more_2);
                } else if (numTab == 2) {
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_list_2);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_1);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_2);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_more_2);
                } else if (numTab == 3) {
                    tabLayout.getTabAt(3).setIcon(R.drawable.ic_list_1);
                    tabLayout.getTabAt(2).setIcon(R.drawable.ic_notifications_2);
                    tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_2);
                    tabLayout.getTabAt(0).setIcon(R.drawable.ic_more_2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        startService(new Intent(getApplicationContext(), SendLocationService.class));
        Gdata.refreshedToken = FirebaseInstanceId.getInstance().getToken();
        try {
            if (!Gdata.refreshedToken.equals("") && ! Gdata.refreshedToken.equals(null)) {
                sendLognReque();
            }
        }catch (NullPointerException e){

        }
        try {
            if (Gdata.teacher_opernum.equals("0")){
                final Dialog layou = new Dialog(TeachertabActivity.this);
                layou.requestWindowFeature(Window.FEATURE_NO_TITLE);
                layou.setContentView(R.layout.finish1);
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                int width = display.getWidth();
                layou.getWindow().setLayout((width * 19 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                final TextViewWithFont show = (TextViewWithFont) layou.findViewById(R.id.show);

                show.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(getApplicationContext(), TeacherseettingActivity.class);
                        startActivity(i);
                        layou.dismiss();
                    }
                });
                layou.show();
            }
        }catch (NullPointerException e){

        }

        viewPager.setCurrentItem(Gdata.pager);
    }
    public void sendLognReque() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher+"edit_token", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

//                    num.setText(obj.getString("notifications"));
//                    not_num.setText(obj.getString("notifications"));
//                    order_num.setText(obj.getString("current orders"));
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //  progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.teacher_id);
                params.put("token",  Gdata.refreshedToken);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TeachersettingFragment(), "");
        adapter.addFragment(new TeacherprofileFragment(), "");
        adapter.addFragment(new TeacherNotFragment(), "");
        adapter.addFragment(new TeacherorderFragment(), "");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                photo.setImageBitmap(bitmap);
                //Setting the Bitmap to ImageView
//
            } catch (IOException e) {
                e.printStackTrace();
            }
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            getSystemService(WINDOW_SERVICE);
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            final int width = options.outWidth;
            final int height = options.outHeight;
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            Log.e("xxx",cursor.getString(columnIndex));
            addimage(cursor.getString(columnIndex));
//            file = cursor.getString(columnIndex);
            cursor.close();
        }
    }
    public void sendLognRequest() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        JSONArray jsonArray = obj.getJSONObject("data").getJSONArray("all_image");
                        Gdata.teacherarray.clear();
                        if (jsonArray.length()==0){
                            Workerimg workerimg = new Workerimg();
                            workerimg.img = "";
                            Gdata.teacherarray.add(workerimg);
                        }else {
                            for (int z = 0 ; z<jsonArray.length();z++){
                                JSONObject jo = jsonArray.getJSONObject(z);
                                Workerimg workerim = new Workerimg();
                                workerim.img = jo.getString("img");
                                workerim.id = jo.getString("image_id");
                                Gdata.teacherarray.add(workerim);
                                if (z==jsonArray.length()-1){
                                    Workerimg workerimg = new Workerimg();
                                    workerimg.img = "";
                                    workerimg.id = "";
                                    Gdata.teacherarray.add(workerimg);
                                }
                            }
                        }
                        TeacherprofileFragment.productadapter.notifyDataSetChanged();
                        TeacherprofileFragment.list.smoothScrollToPosition(Gdata.teacherarray.size()-1);
                        TeacherprofileFragment.list.setScrollY(Gdata.array.size()-1 );
//                        Gdata.worker_job_id = obj.getJSONObject("data").getString("job_id");

//                        userId=obj.getString("id");


                        // Session Manager

                    } else {

                        String msg = obj.getString("msg");

                        Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    // _errorMsg.setText(e.getMessage());

                    e.printStackTrace();

                }
                //  progressDialog.dismiss();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getApplicationContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof ParseError) {
                            //TODO
                        }
                    }

                })

        {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();
                params.put("user_id", Gdata.teacher_id);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }
    public void addimage(String path) {
        Ion.with(TeachertabActivity.this)
                .load(Gdata.url_teacher + "upload_image_teacher")
                .setMultipartParameter("user_id",Gdata.teacher_id)
                .setMultipartParameter("lang", Locale.getDefault().getLanguage())
                .setMultipartFile("img", new File(path))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, final JsonObject result) {
                        if (result == null) {

                        } else {
                            try {
                                // JSON Object
                                JSONObject obj = new JSONObject(result.toString());

                                boolean status = obj.getBoolean("Success");

                                if (status) {

sendLognRequest();
                                } else {

                                    String msg = obj.getString("msg");

                                    Toast t = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                    Log.e("response ", msg + "");

                                }
                            } catch (JSONException ee) {
                                // TODO Auto-generated catch block
                                // _errorMsg.setText(e.getMessage());

                                ee.printStackTrace();

                            }
                        }
                    }
                });
    }
}

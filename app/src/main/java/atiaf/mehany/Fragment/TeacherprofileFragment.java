package atiaf.mehany.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import atiaf.mehany.Activity.MainActivity;
import atiaf.mehany.Activity.TeachereditActivity;
import atiaf.mehany.Adapter.Adapter_LevelSpinner;
import atiaf.mehany.Adapter.Adapter_SubjectSpinner;
import atiaf.mehany.Adapter.Photodapte;
import atiaf.mehany.Customecalss.TextViewWithFont;
import atiaf.mehany.Data.Country;
import atiaf.mehany.Data.Gdata;
import atiaf.mehany.Data.OutData;
import atiaf.mehany.R;
import de.hdodenhof.circleimageview.CircleImageView;

import static atiaf.mehany.Data.Gdata.user_id;
import static atiaf.mehany.R.layout.spinner;

public class TeacherprofileFragment extends Fragment {
    View v;
    ImageView settings ;
    FrameLayout edit, logout;
    CircleImageView img;
    TextViewWithFont name, job, sub, addstage, addsubject;
    public static RecyclerView list;
    SharedPreferences storedata;
    private static String filename = "mlogin";
    public static Photodapte productadapter;
    Dialog layou;
    Spinner spin, spin1;
    ArrayList<Country> stages = new ArrayList<>();
    ArrayList<Country> studies = new ArrayList<>();
    String stage = "", study = "", jobid = "";
    TextViewWithFont title;


    String levelId ;
    String subjectId ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (v == null) {
            v = inflater.inflate(R.layout.fragment_teacherprofile, container, false);
            Log.e("user_id", user_id +"" );
            edit = (FrameLayout) v.findViewById(R.id.edit);
            settings = (ImageView) v.findViewById(R.id.settings);
            logout = (FrameLayout) v.findViewById(R.id.logout);
            img = (CircleImageView) v.findViewById(R.id.img);
            name = (TextViewWithFont) v.findViewById(R.id.name);
            job = (TextViewWithFont) v.findViewById(R.id.job);
            sub = (TextViewWithFont) v.findViewById(R.id.sub);
            addstage = (TextViewWithFont) v.findViewById(R.id.addstage);
            addsubject = (TextViewWithFont) v.findViewById(R.id.addsubject);
            list = (RecyclerView) v.findViewById(R.id.list);
            name.setText(Gdata.teacher_fname + " " + Gdata.teacher_lname);
            job.setText(Gdata.teacher_stages);
            sub.setText(Gdata.teacher_subjects);
            Picasso.with(getContext()).load(Gdata.teacher_img).into(img, new Callback() {
                @Override
                public void onSuccess() {

                }
                @Override
                public void onError() {

                }
            });

           if( Gdata.teacher_id.equals("")){settings.setVisibility(View.GONE);}
            settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    PopupMenu popup = new PopupMenu(getActivity(),settings);
                    popup.getMenuInflater()
                            .inflate(R.menu.activity_main_actions, popup.getMenu());
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.edit:
                                    layou = new Dialog(getContext());
                                    layou.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    layou.setContentView(R.layout.addsubject);
                                    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                                    Display display = wm.getDefaultDisplay();
                                    int width = display.getWidth();
                                    layou.getWindow().setLayout((width * 19 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                                    ((Activity) getContext()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                                    spin = (Spinner) layou.findViewById(R.id.spin);
                                    spin1 = (Spinner) layou.findViewById(R.id.spin1);
                                    final TextViewWithFont yes = (TextViewWithFont) layou.findViewById(R.id.yes);
                                    final TextViewWithFont no = (TextViewWithFont) layou.findViewById(R.id.no);
                                    final TextViewWithFont title = (TextViewWithFont) layou.findViewById(R.id.title);
                                    title.setText(getString(R.string.edit_subject_stage));
                                    if (isNetworkAvailable()) {
                                        Log.e("555id", Gdata.teacher_id+"" );
                                        RequestParams arg = new RequestParams() ;
                                        AsyncHttpClient client = new AsyncHttpClient();
                                        arg.put("lang",getString(R.string.lang));
                                        arg.put("user_id",Gdata.teacher_id);
                                      client.post(Gdata.url_teacher+"get_level_user",arg,new AsyncHttpResponseHandler(){
                                          @Override
                                          public void onSuccess(int statusCode, String content) {
                                              super.onSuccess(statusCode, content);
                                              Log.e("5555555", content+"--" );

                                              Type fooType = new TypeToken<OutData>() {
                                              }.getType();
                                              final OutData success = new Gson().fromJson(content, fooType);
                                              Log.e("555555555", success.data +"" );
                                              final Adapter_LevelSpinner adp = new Adapter_LevelSpinner(getActivity(), success.data);
//                                              spin.post(new Runnable() {
//                                                  @Override
//                                                  public void run() {
//                                                      try {
//                                                          spin.setSelection(0);
//                                                          levelId = success.data.get(0).level_id ;
////                Log.e("pos", spinnerTxt + "");
//                                                      } catch (IndexOutOfBoundsException a) {
//                                                          a.printStackTrace();
//                                                      }
//                                                  }
//                                              });
                                              spin.setAdapter(adp);
//                                              levelId = success.data.get(0).level_id ;
                                              spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
//                                                getStadie();
                                                      levelId = success
                                                              .data.get(position).level_id ;
                                                      RequestParams arg = new RequestParams();
                                                      AsyncHttpClient client2 = new AsyncHttpClient();
                                                      arg.put("lang",getString(R.string.lang));
                                                      arg.put("user_id",Gdata.teacher_id);
                                                      arg.put("level_id",levelId);
                                                      client2.post("http://mhny.co/API/Teachers/get_subject_user",arg,new AsyncHttpResponseHandler(){
                                                          @Override
                                                          public void onSuccess(int statusCode, String content) {
                                                              super.onSuccess(statusCode, content);

                                                              Type fooType = new TypeToken<OutData>() {
                                                              }.getType();
                                                              final OutData success1 = new Gson().fromJson(content, fooType);
                                                              final Adapter_SubjectSpinner adp1 = new Adapter_SubjectSpinner(getActivity(), success1.data);
                                                              spin1.setAdapter(adp1);
                                                              spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                  @Override
                                                                  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                      subjectId = success1.data.get(position).subject_id ;

                                                                      yes.setOnClickListener(new View.OnClickListener() {
                                                                          @Override
                                                                          public void onClick(View view) {
                                                                              if (subjectId.equals("")) {
                                                                                  Handler handler = new Handler(Looper.getMainLooper());
                                                                                  handler.post(new Runnable() {
                                                                                      @Override
                                                                                      public void run() {
                                                                                          Toast t = null;
                                                                                          t = Toast.makeText(getContext(), R.string.plz_select_subject, Toast.LENGTH_LONG);
                                                                                          t.setGravity(Gravity.CENTER, 0, 0);
                                                                                          t.show();
                                                                                      }
                                                                                  });
                                                                              } else {
                                                                                  if (isNetworkAvailable()) {


                                                                                      AsyncHttpClient client3 = new AsyncHttpClient();
                                                                                      RequestParams arg = new RequestParams();
                                                                                      arg.put("lang",getString(R.string.lang));
                                                                                      arg.put("user_id",Gdata.teacher_id);
                                                                                      arg.put("subject_id",subjectId);

client3.post(Gdata.url_teacher+"delete_subject",arg,new AsyncHttpResponseHandler(){
    @Override
    public void onSuccess(int statusCode, String content) {
        super.onSuccess(statusCode, content);

        Type fooType = new TypeToken<OutData>() {
        }.getType();
         OutData success = new Gson().fromJson(content, fooType);

        Toast t = Toast.makeText(getContext(), success.msg, Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();

        if(success.Success){
            if (isNetworkAvailable()) {
                getdata();
            } else {
                Toast t1 = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                t1.setGravity(Gravity.CENTER, 0, 0);
                t1.show();
            }
         //  restart  fragment
        }
    }

    @Override
    public void onFailure(Throwable error, String content) {
        super.onFailure(error, content);

        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
        layou.dismiss();
    }
});

                                                                                  } else {
                                                                                      Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                                                                      t.setGravity(Gravity.CENTER, 0, 0);
                                                                                      t.show();
                                                                                  }
                                                                              }
                                                                          }
                                                                      });


                                                                  }

                                                                  @Override
                                                                  public void onNothingSelected(AdapterView<?> adapterView) {

                                                                  }
                                                              });
                                                          }

                                                          @Override
                                                          public void onFailure(Throwable error, String content) {
                                                              super.onFailure(error, content);
                                                              Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                                              t.setGravity(Gravity.CENTER, 0, 0);
                                                              t.show();
                                                              layou.dismiss();
                                                          }
                                                      });




                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> parent) {

                                                  }
                                              });

                                          }

                                          @Override
                                          public void onFailure(Throwable error, String content) {
                                              super.onFailure(error, content);
                                              Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                              t.setGravity(Gravity.CENTER, 0, 0);
                                              t.show();
                                              layou.dismiss();
                                          }
                                      });

                                    } else {
                                        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();
                                        layou.dismiss();
                                    }
                                    no.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            layou.dismiss();
                                        }
                                    });

                                    layou.show();





                                    break;
                            }
                            return true;
                        }
                    });

                    popup.show();

                }
            });



            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isNetworkAvailable()) {
                        sendLognRequest();
                    } else {
                        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), TeachereditActivity.class);
                    startActivity(i);
                }
            });
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            list.setLayoutManager(layoutManager);
            productadapter = new Photodapte(Gdata.teacherarray);
            list.setAdapter(productadapter);
            productadapter.notifyItemChanged(Gdata.teacherarray.size() - 1);
            list.smoothScrollToPosition(Gdata.teacherarray.size() - 1);
            list.setScrollY(Gdata.array.size() - 1);
            addstage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layou = new Dialog(getContext());
                    layou.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    layou.setContentView(R.layout.addsubject);
                    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width = display.getWidth();
                    layou.getWindow().setLayout((width * 19 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                    ((Activity) getContext()).getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    spin = (Spinner) layou.findViewById(R.id.spin);
                    spin1 = (Spinner) layou.findViewById(R.id.spin1);
                    spin1.setVisibility(View.GONE);
                    final TextViewWithFont yes = (TextViewWithFont) layou.findViewById(R.id.yes);
                    final TextViewWithFont no = (TextViewWithFont) layou.findViewById(R.id.no);
                    title = (TextViewWithFont) layou.findViewById(R.id.title);
                    title.setText(addstage.getText().toString());
                    if (isNetworkAvailable()) {
                        getStages();
                        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getStadies();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layou.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (study.equals("")) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast t = null;
                                        t = Toast.makeText(getContext(), getString(R.string.trr), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();

                                    }
                                });
                            } else {
                                if (isNetworkAvailable()) {
                                    add("add_subject");
                                } else {
                                    Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                }
                            }
                        }
                    });
                    layou.show();
                }
            });
            addsubject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    layou = new Dialog(getContext());
                    layou.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    layou.setContentView(R.layout.addsubject);
                    WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
                    Display display = wm.getDefaultDisplay();
                    int width = display.getWidth();
                    layou.getWindow().setLayout((width * 19 / 20), LinearLayout.LayoutParams.WRAP_CONTENT);
                    ((Activity) getContext()).getWindow().setSoftInputMode(
                            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    spin = (Spinner) layou.findViewById(R.id.spin);
                    spin1 = (Spinner) layou.findViewById(R.id.spin1);
                    final TextViewWithFont yes = (TextViewWithFont) layou.findViewById(R.id.yes);
                    final TextViewWithFont no = (TextViewWithFont) layou.findViewById(R.id.no);
                    final TextViewWithFont title = (TextViewWithFont) layou.findViewById(R.id.title);
                    title.setText(addsubject.getText().toString());
                    if (isNetworkAvailable()) {
                        getStage();
                        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                getStadie();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    } else {
                        Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                    }
                    no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            layou.dismiss();
                        }
                    });
                    yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (study.equals("")) {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast t = null;
                                        t = Toast.makeText(getContext(), getString(R.string.trr), Toast.LENGTH_LONG);
                                        t.setGravity(Gravity.CENTER, 0, 0);
                                        t.show();

                                    }
                                });
                            } else {
                                if (isNetworkAvailable()) {
                                    add("add_subject");
                                } else {
                                    Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                                    t.setGravity(Gravity.CENTER, 0, 0);
                                    t.show();
                                }
                            }
                        }
                    });
                    layou.show();
                }
            });
        }
        return v;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    public void sendLognRequest() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "sign_out", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        storedata = getContext().getSharedPreferences(filename, 0);
                        SharedPreferences.Editor edit = storedata.edit();
                        storedata.edit().clear().commit();
                        Intent i = new Intent(getContext(), MainActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        ((Activity) getContext()).finish();                        // Session Manager

                    } else {

                        String msg;
                        if (Locale.getDefault().getLanguage().equals("ar")) {
                            msg = "تأكد من ان البيانات صحيحة";
                        } else {
                            msg = "Make sure that data is correct";
                        }
                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
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
                        progressDialog.dismiss();

                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_level", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    stages.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("level_id");
                            countr.title = jo.getString("title");
                            stages.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder);
                        stages.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getContext(), android.R.layout.simple_spinner_dropdown_item, stages) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (stages.size() > 0) {
                                try {
                                    txtTitle.setText(stages.get(position).title);
                                    stage = stages.get(position).id;
                                    if (!stages.get(position).id.equals("")) {
                                        spin.setVisibility(View.GONE);
                                        spin1.setVisibility(View.VISIBLE);
                                        title.setText(addsubject.getText().toString());
                                        getStadies();
                                    }
                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (stages.size() > 0) {
                                txtTitle.setText(stages.get(position).title);

                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);
                    spin.setSelection(adapter.getCount());

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

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStadies() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_subject", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    studies.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("subject_id");
                            countr.title = jo.getString("title");
                            studies.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        if (jsonArray.length() == 0) {
                            Country count = new Country();
                            count.id = "";
                            count.title = getString(R.string.sendorder1);
                            studies.add(count);
                        }
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder1);
                        studies.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getContext(), android.R.layout.simple_spinner_dropdown_item, studies) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (studies.size() > 0) {
                                try {
                                    txtTitle.setText(studies.get(position).title);
                                    study = studies.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (studies.size() > 0) {
                                txtTitle.setText(studies.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(adapter);
                    spin1.setSelection(adapter.getCount());

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

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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
                params.put("lang", Locale.getDefault().getLanguage());
                params.put("subject_id", stage);
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_level", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    stages.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("level_id");
                            countr.title = jo.getString("title");
                            stages.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder);
                        stages.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder);
                        stages.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getContext(), android.R.layout.simple_spinner_dropdown_item, stages) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (stages.size() > 0) {
                                try {
                                    txtTitle.setText(stages.get(position).title);
                                    stage = stages.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (stages.size() > 0) {
                                txtTitle.setText(stages.get(position).title);

                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);
                    spin.setSelection(adapter.getCount());

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

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getStadie() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_subject", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");
                    studies.clear();
                    if (status) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            Country countr = new Country();
                            countr.id = jo.getString("subject_id");
                            countr.title = jo.getString("title");
                            studies.add(countr);
                        }
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        if (jsonArray.length() == 0) {
                            Country count = new Country();
                            count.id = "";
                            count.title = getString(R.string.sendorder1);
                            studies.add(count);
                        }
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {
                        Country countr = new Country();
                        countr.id = "";
                        countr.title = getString(R.string.sendorder1);
                        studies.add(countr);
                        Country count = new Country();
                        count.id = "";
                        count.title = getString(R.string.sendorder1);
                        studies.add(count);


                    }
                    ArrayAdapter<Country> adapter = new ArrayAdapter<Country>(getContext(), android.R.layout.simple_spinner_dropdown_item, studies) {

                        @Override
                        public View getView(int position, View convertView, ViewGroup parent) {

                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(spinner, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            final ImageView img = (ImageView) convertView.findViewById(R.id.image);
                            if (studies.size() > 0) {
                                try {
                                    txtTitle.setText(studies.get(position).title);
                                    study = studies.get(position).id;

                                } catch (IndexOutOfBoundsException e) {

                                }


                            }
                            return convertView;
                        }

                        @Override
                        public View getDropDownView(int position, View convertView, ViewGroup parent) {
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.textspin, null, false);
                            TextViewWithFont txtTitle = (TextViewWithFont) convertView.findViewById(R.id.title);
                            if (studies.size() > 0) {
                                txtTitle.setText(studies.get(position).title);
                            }
                            return convertView;
                        }

                        @Override
                        public int getCount() {
                            return super.getCount() - 1; // you dont display last item. It is used as hint.
                        }
                    };

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin1.setAdapter(adapter);
                    spin1.setSelection(adapter.getCount());

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

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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
                params.put("lang", Locale.getDefault().getLanguage());
                params.put("subject_id", stage);
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void add(String methodName) {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + methodName, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = (obj.getBoolean("Success"));


                    if (status) {
                        if (isNetworkAvailable()) {
                            getdata();
                        } else {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        }
                        String msg = (obj.getString("msg"));

                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");
                        // Session Manager

                    } else {

                        String msg = (obj.getString("msg"));

                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
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


                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            //TODO
                        } else if (error instanceof NetworkError) {
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
                params.put("subject_id", study);
                params.put("lang", Locale.getDefault().getLanguage());
                Log.e("loginParams", params.toString());

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }

    public void getdata() {
        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), getString(R.string.wait), getString(R.string.che), true);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Gdata.url_teacher + "get_data", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //     Toast.makeText(getContext(), response.toString(), Toast.LENGTH_LONG).show();
                Log.e("response ", response + "");


                // Hide Progress Dialog
                //progressDialog.dismiss();
                try {
                    // JSON Object
                    JSONObject obj = new JSONObject(response);

                    boolean status = obj.getBoolean("Success");

                    if (status) {
                        Gdata.teacher_subjects = "";
                        Gdata.teacher_stages = "";
//                        Gdata.teacher_job_id = obj.getJSONObject("data").getString("job_id");
                        for (int i = 0; i < obj.getJSONObject("data").getJSONArray("note").length(); i++) {
                            JSONObject j = obj.getJSONObject("data").getJSONArray("note").getJSONObject(i);
                            if (i == obj.getJSONObject("data").getJSONArray("note").length() - 1) {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title");
                                }
                                if (!Gdata.teacher_subjects.contains(j.getString("sub_title"))) {
                                    Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title");
                                }

                            } else {
                                if (!Gdata.teacher_stages.contains(j.getString("lev_title"))) {
                                    Gdata.teacher_stages = Gdata.teacher_stages + j.getString("lev_title") + " - ";
                                }
                                if (!Gdata.teacher_subjects.contains(j.getString("sub_title"))) {
                                    Gdata.teacher_subjects = Gdata.teacher_subjects + j.getString("sub_title") + " - ";
                                }

                            }
                        }
                        if (Gdata.teacher_subjects.substring(Gdata.teacher_subjects.length() - 2, Gdata.teacher_subjects.length() - 1).equals("-")) {
                            Gdata.teacher_subjects = Gdata.teacher_subjects.substring(0, Gdata.teacher_subjects.length() - 2);
                        }
                        if (Gdata.teacher_stages.substring(Gdata.teacher_stages.length() - 2, Gdata.teacher_stages.length() - 1).equals("-")) {
                            Gdata.teacher_stages = Gdata.teacher_stages.substring(0, Gdata.teacher_stages.length() - 2);
                        }
                        job.setText(Gdata.teacher_stages);
                        sub.setText(Gdata.teacher_subjects);
                        layou.dismiss();
//                        userId=obj.getString("id");


                        // Session Manager

                    } else {

                        String msg = obj.getString("msg");

                        Toast t = Toast.makeText(getContext(), msg, Toast.LENGTH_LONG);
                        t.setGravity(Gravity.CENTER, 0, 0);
                        t.show();
                        Log.e("response ", msg + "");

                    }
                    progressDialog.dismiss();
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

                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                        } else if (error instanceof AuthFailureError) {
                            //TODO
                        } else if (error instanceof ServerError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
                            t.setGravity(Gravity.CENTER, 0, 0);
                            t.show();
                            //TODO
                        } else if (error instanceof NetworkError) {
                            Toast t = Toast.makeText(getContext(), getString(R.string.inte), Toast.LENGTH_LONG);
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

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);
    }


}

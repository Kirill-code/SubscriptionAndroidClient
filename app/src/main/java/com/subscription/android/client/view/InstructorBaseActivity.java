package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.subscription.android.client.Api;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.VisitCardRecyclerViewAdapter;
import com.subscription.android.client.VisitGridItemDecoration;
import com.subscription.android.client.R;

import com.subscription.android.client.fragments.BottomSheetNavigationFragment;
import com.subscription.android.client.fragments.DateRangePickerFragment;
import com.subscription.android.client.model.DTO.VisitsDTO;
import com.subscription.android.client.model.Instructor;
import com.subscription.android.client.model.Subscription;
import com.subscription.android.client.print.PrinterActivity;

import java.io.EOFException;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstructorBaseActivity extends BaseActivity implements DateRangePickerFragment.OnDateRangeSelectedListener {

    private static final String TAG = InstructorBaseActivity.class.getName();

    RecyclerView recyclerView;
    BottomAppBar bottomAppBar;
    TextView adminBtn;
    Instructor intentInstructor=new Instructor();

    public Retrofit retrofitRx = new Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public Api apiRx = retrofitRx.create(Api.class);
    public FirebaseUser mUserInstr = FirebaseAuth.getInstance().getCurrentUser();

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_base);
        BaseActivity base=new BaseActivity();
        base.getToken();
        showProgressDialog();

//Разобраться с состоянием ответов
        getTokenLocaly(mUserInstr.getUid());

        FloatingActionButton fab=findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(InstructorBaseActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });
        //add position of current date or nearest
        //int scrollTo = ((View) childView.getParent().getParent()).getTop() + childView.getTop();


        //adminBtn=findViewById(R.id.app_bar_person);

        View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2AdminPage();
            }
        };
// Bars
        Toolbar myToolbar =  findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);
        bottomAppBar=findViewById(R.id.bottom_app_bar);
        bottomAppBar.replaceMenu(R.menu.bottomappbar_menu);
        bottomAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //open bottom sheet
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomSheetNavigationFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

        bottomAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.app_bar_add:
                        go2Print();
                        break;
                }
                return false;
            }
        });

    }


    private void getCurrentMonth(long instructorId) {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date MonthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date MonthLastDay = calendar.getTime();

      getVisitsInPeriod(instructorId,sdf.format(MonthFirstDay),sdf.format(MonthLastDay));
       // getVisitsInPeriodRx(1,sdf.format(MonthFirstDay),sdf.format(MonthLastDay));
    }

    public void getTokenLocaly(String uid){
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();
////////////////////////////delete this token assigmnet!
                            getInstructor(idToken,uid);

                        } else {
                            Log.e("CallbackException", task.getException().toString());
                        }
                    }
                });
    }
    public void getInstructor(String idtokenlocal, String uid ) {

        Call<Instructor> call = api.getInstructorByUid(idtokenlocal, uid);
        call.enqueue(new Callback<Instructor>() {
            @Override
            public void onResponse(Call<Instructor> call, Response<Instructor> response) {
                if(response!=null) {

                    Instructor tempInstructor=new Instructor();
                    tempInstructor.setId(response.body().getId());
                    tempInstructor.setName(response.body().getName());
                    tempInstructor.setSurname(response.body().getSurname());

                    //change to callbacks
                    getCurrentMonth(response.body().getId());
                }
            }

            @Override
            public void onFailure(Call<Instructor> call, Throwable t) {
                try {
                    throw t;
                } catch (ConnectException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.errorconnection),
                            Toast.LENGTH_SHORT).show();
                } catch (EOFException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                            Toast.LENGTH_SHORT).show();
                } catch (Throwable et) {
                    Log.e(TAG, et.getMessage());

                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.helpdesk),
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //old version will be changed by RxJava implementation
    private void getVisitsInPeriod(long instid,String dateStart, String dateEnd) {
        recyclerView=null;

        Call<List<List>> call = api.getMonthVisits(instid,dateStart,dateEnd);

        call.enqueue(new Callback<List<List>>() {
            @Override
            public void onResponse(Call<List<List>> call, Response<List<List>> response) {
                hideProgressDialog();
                //CAST PROBLEMS
                List<List> responseVisits = response.body();
                List<VisitsDTO> tempList=new ArrayList<>();
                for (int i=0;i<response.body().size();i++) {
                    VisitsDTO temp=new VisitsDTO();
                    temp.setDate(response.body().get(i).get(0).toString());
                    temp.setVisits_count((double) response.body().get(i).get(1));
                    temp.setInstr_id((double) response.body().get(i).get(2));
                    tempList.add(temp);
                }

                VisitCardRecyclerViewAdapter adapter = new VisitCardRecyclerViewAdapter(tempList);
                recyclerView = findViewById(R.id.recycler_view);
                NestedScrollView nestedScrollView=findViewById(R.id.nsv);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1, GridLayoutManager.VERTICAL, false));


                int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
                int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
                recyclerView.addItemDecoration(new VisitGridItemDecoration(largePadding, smallPadding));

                recyclerView.setAdapter(adapter);
//add scroll to position here
                /*recyclerView.post(() -> {
                    float y = recyclerView.getY() + recyclerView.getChildAt(2).getY();
                    nestedScrollView.smoothScrollTo(0,  (int) y);
                });*/
            }

            @Override
            public void onFailure(Call<List<List>> call, Throwable t) {
                try {
                    throw t;
                } catch (ConnectException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.errorconnection) ,
                            Toast.LENGTH_SHORT).show();
                } catch (TimeoutException ex) {
                    Log.e(TAG, ex.getMessage());
                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.timeout) ,
                            Toast.LENGTH_SHORT).show();
                } catch (Throwable et) {
                    Log.e(TAG, et.getMessage());

                    hideProgressDialog();
                    Toast.makeText(getApplicationContext(),getResources().getString(R.string.helpdesk) ,
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
    private  void getVisitsInPeriodRx(long instid,String dateStart, String dateEnd) {
        apiRx.getMonthVisitsRx(instid,dateStart,dateEnd)
                .toObservable()
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<List<List>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<List> list) {
                        Log.d(TAG,"onNext:");
                        System.out.println();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG,"onError",e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uptoolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.search:
                Toast.makeText(this, "Clicked Person", Toast.LENGTH_SHORT).show();
                break;
            case R.id.filter:
                DateRangePickerFragment dateRangePickerFragment= DateRangePickerFragment.newInstance(InstructorBaseActivity.this,false);
                dateRangePickerFragment.show(getSupportFragmentManager(),"datePicker");

                break;

        }
        return true;
    }


    private void go2Print() {
        Intent intent = new Intent(this, PrinterActivity.class);
        intent.putExtra("Instructor2Sub", intentInstructor);

        startActivity(intent);
    }

    private void go2AdminPage() {
        Intent intent = new Intent(this, AdminActivity.class);
        startActivity(intent);
    }
    private void updateSubscription(String uid) {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {
                            String idToken = task.getResult().getToken();

                            showProgressDialog();

                            Call<Subscription> call = api.getSubscriptionByUid(idToken,uid);
                            call.enqueue(new Callback<Subscription>() {
                                @Override
                                public void onResponse(Call<Subscription> call, Response<Subscription> response) {

                                    Call<Void> call2save = api.savevisit(response.body().getAssociatedUserId());

                                    call2save.enqueue(new Callback<Void>() {
                                        @Override
                                        public void onResponse(Call<Void> call2save, Response<Void> response) {
                                            hideProgressDialog();
                                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.scanSucsessful), Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<Void> call2save, Throwable t) {
                                            Log.e(TAG, t.getMessage());
                                            hideProgressDialog();
                                            Toast.makeText(getApplicationContext(), getResources().getText(R.string.repeatlater), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<Subscription> call, Throwable t) {
                                    try {
                                        throw t;
                                    } catch (ConnectException ex) {
                                        hideProgressDialog();
                                        Log.e(TAG, ex.getMessage());
                                        String[] visitedDates = {getResources().getString(R.string.errorconnection)};
                                     //   gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (EOFException ex) {
                                        Log.e(TAG, ex.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.emptylessons)};
                                       // gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));
                                    } catch (Throwable et) {
                                        Log.e(TAG, et.getMessage());
                                        hideProgressDialog();
                                        String[] visitedDates = {getResources().getString(R.string.helpdesk)};
//                                        gvMain.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.list_black_text, R.id.list_content, visitedDates));

                                    }
                                }
                            });

                        } else {
                            Log.e(TAG, task.getException().toString());
                        }
                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.i(TAG, "Cancelled scan");

            } else {
                System.out.println(result.getContents().substring(0, 7));
                if (result.getContents().substring(0, 8).equals("syryauid")) {
                    Log.i(TAG, "Scanned:" + result.getContents());
                    Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();
                    updateSubscription(result.getContents().substring(8));
                } else {
                    Toast.makeText(this, "Scan", Toast.LENGTH_SHORT).show();

                    Log.e(TAG, result.getContents());
                    Toast.makeText(getApplicationContext(), getResources().getText(R.string.qrscanerror),
                            Toast.LENGTH_SHORT).show();
                }


            }
        } else {
            // This is important, otherwise the result will not be passed to the fragment
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onDateRangeSelected(int startDay, int startMonth, int startYear, int endDay, int endMonth, int endYear) {
        if(startDay==endDay){
            Toast.makeText(getApplicationContext(),getResources().getString(R.string.samedate) ,
                    Toast.LENGTH_SHORT).show();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear,startMonth,startDay );
        Date startDate = calendar.getTime();


        calendar.set(endYear,endMonth,endDay );
        Date endDate = calendar.getTime();

        getVisitsInPeriod(1,sdf.format(startDate),sdf.format(endDate));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

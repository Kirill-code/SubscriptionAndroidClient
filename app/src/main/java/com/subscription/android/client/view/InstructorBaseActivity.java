package com.subscription.android.client.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import com.subscription.android.client.print.PrinterActivity;

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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InstructorBaseActivity extends BaseActivity implements DateRangePickerFragment.OnDateRangeSelectedListener {

    private static final String TAG = InstructorBaseActivity.class.getName();

    RecyclerView recyclerView;
    BottomAppBar bottomAppBar;
    TextView adminBtn;
    Instructor intentInstructor=new Instructor();

    List monthsList;


    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_base);
        showProgressDialog();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        Date MonthFirstDay = calendar.getTime();
        calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date MonthLastDay = calendar.getTime();


        getVisitsInPeriod(1,sdf.format(MonthFirstDay).toString(),sdf.format(MonthLastDay).toString());

        FloatingActionButton fab=findViewById(R.id.fab);

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
                        intentInstructor.setId(1);
                        intentInstructor.setName("TEST");
                        intentInstructor.setSurname("Rename it");
                        go2Print();
                        break;
                }
                return false;
            }
        });


        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(InstructorBaseActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.initiateScan();
            }
        });


    }



    private void getVisitsInPeriod(long instid,String dateStart, String dateEnd) {
        recyclerView=null;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Api api = retrofit.create(Api.class);

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

        //Start token verification
        /*FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUser.getIdToken(true)
                .addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                    public void onComplete(@NonNull Task<GetTokenResult> task) {
                        if (task.isSuccessful()) {

                            String idToken = task.getResult().getToken();
                            showProgressDialog();
                            // Send token to your backend via HTTPS

                        } else {
                            System.out.println( task.getException());
                        }
                    }
                });
*/

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
                } else {
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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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

package com.subscription.android.client.view;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.zxing.integration.android.IntentIntegrator;
import com.subscription.android.client.BaseActivity;
import com.subscription.android.client.VisitCardRecyclerViewAdapter;
import com.subscription.android.client.VisitEntry;
import com.subscription.android.client.VisitGridItemDecoration;
import com.subscription.android.client.R;
import com.subscription.android.client.model.Instructor;

public class InstructorBaseActivity extends BaseActivity {

    private static final String TAG = InstructorBaseActivity.class.getName();

    BottomAppBar bottomAppBar;
    TextView adminBtn;
    Instructor intentInstructor=new Instructor();


    protected void onCreate(Bundle savedInstanceState) {



        Log.i(TAG, "Activity started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructor_base);


        FloatingActionButton fab=findViewById(R.id.fab);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        NestedScrollView nestedScrollView=findViewById(R.id.nsv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        VisitCardRecyclerViewAdapter adapter = new VisitCardRecyclerViewAdapter(
                VisitEntry.initProductEntryList(getResources()));
        recyclerView.setAdapter(adapter);
        int largePadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing);
        int smallPadding = getResources().getDimensionPixelSize(R.dimen.shr_product_grid_spacing_small);
        recyclerView.addItemDecoration(new VisitGridItemDecoration(largePadding, smallPadding));

        //int scrollTo = ((View) childView.getParent().getParent()).getTop() + childView.getTop();
        recyclerView.post(() -> {
            float y = recyclerView.getY() + recyclerView.getChildAt(5).getY();
            nestedScrollView.smoothScrollTo(0,  (int) y);
        });

        //adminBtn=findViewById(R.id.app_bar_person);

       /* View.OnClickListener oclBtnOk = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go2AdminPage();
            }
        };*/
//        adminBtn.setOnClickListener(oclBtnOk);

        //bottomAppBar=findViewById(R.id.bar);
      setSupportActionBar(bottomAppBar);

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
/*

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.app_bar_add:
                intentInstructor.setId(1);
                intentInstructor.setName("TEST");
                intentInstructor.setSurname("Rename it");
                go2Print();
                break;
            case R.id.app_bar_person:
                Toast.makeText(this, "Clicked Person", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }

   */
@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottomappbar_menu,menu);
        return true;
    }/*

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
*/




}

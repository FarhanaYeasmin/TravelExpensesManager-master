package tour.icurious.travelexpensesmanager.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import tour.icurious.travelexpensesmanager.R;
import tour.icurious.travelexpensesmanager.dataModel.Tour;
import tour.icurious.travelexpensesmanager.database.TourManager;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter<String> adapter;
    private TourManager manager;
    private ArrayList<Tour> tours;
    private ArrayList<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        lv = (ListView) findViewById(R.id.tourList);
        manager = new TourManager(this);
        names = new ArrayList<>();

        refreshList();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(MainActivity.this, TourExpanses.class).putExtra("tourRowID", tours.get(i).getId()));
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                final int i = index;
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete!")
                        .setMessage("Are you sure?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                adapter.remove(names.get(i));
                                manager.deleteTour(tours.get(i).getId());
                                Snackbar.make(coordinatorLayout, "Tour deleted!", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("no", null);

                builder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        tours = manager.getTourList();
        names.clear();
        for (Tour t : tours) names.add(t.getTitle());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_createTour:
                startActivity(new Intent(MainActivity.this, NewTourActivity.class));
                return true;
           /* case R.id.action_exit:
                // Exit option clicked.
                return true;
            case R.id.action_settings:
                // Settings option clicked.
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

package tour.icurious.travelexpensesmanager.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

import tour.icurious.travelexpensesmanager.R;
import tour.icurious.travelexpensesmanager.dataModel.Member;
import tour.icurious.travelexpensesmanager.dataModel.Tour;
import tour.icurious.travelexpensesmanager.database.TourManager;

public class NewTourActivity extends AppCompatActivity {
    private ListView membersListView;
    private EditText et_title;
    private EditText et_description;
    private EditText et_budget;
    private EditText et_memberName;
    private TextView tv_goingDate;
    private TextView tv_returnDate;

    private ArrayAdapter<String> adapter;
    private TourManager manager;
    private ArrayList<String> names;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_tour);

        et_title = (EditText) findViewById(R.id.et_title);
        et_description = (EditText) findViewById(R.id.et_description);

        tv_goingDate = (TextView) findViewById(R.id.tv_goingDate);
        tv_returnDate = (TextView) findViewById(R.id.tv_return);

        et_budget = (EditText) findViewById(R.id.et_budget);
        et_memberName = (EditText) findViewById(R.id.memberName);

        membersListView = (ListView) findViewById(R.id.membersList);
        names = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, names);
        membersListView.setAdapter(adapter);

        membersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int index, long l) {
                final int i = index;
                AlertDialog.Builder builder = new AlertDialog.Builder(NewTourActivity.this)
                        .setTitle("Remove member?")
                        .setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int j) {
                                adapter.remove(names.get(i));
                                Toast.makeText(NewTourActivity.this, adapter.getItem(i) + " removed!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null);

                builder.create().show();
                return true;
            }
        });

        manager = new TourManager(this);

        final Calendar myCalendar = java.util.Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                long dateLong = myCalendar.get(Calendar.YEAR) * 10000 + (myCalendar.get(Calendar.MONTH) + 1) * 100 + myCalendar.get(Calendar.DATE);
                tv_goingDate.setText("" + dateLong);
            }
        };


        tv_goingDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        NewTourActivity.this,
                        date,
                        myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });

        final DatePickerDialog.OnDateSetListener retDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(java.util.Calendar.YEAR, year);
                myCalendar.set(java.util.Calendar.MONTH, monthOfYear);
                myCalendar.set(java.util.Calendar.DAY_OF_MONTH, dayOfMonth);

                long dateLong = myCalendar.get(Calendar.YEAR) * 10000 + (myCalendar.get(Calendar.MONTH) + 1) * 100 + myCalendar.get(Calendar.DATE);
                tv_returnDate.setText("" + dateLong);
            }
        };


        tv_returnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(
                        NewTourActivity.this,
                        retDate,
                        myCalendar.get(java.util.Calendar.YEAR), myCalendar.get(java.util.Calendar.MONTH),
                        myCalendar.get(java.util.Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_tour_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_saveData:
                saveData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveData() {
        String title = et_title.getText().toString().trim();
        String desc = et_description.getText().toString().trim();
        String budget = et_budget.getText().toString().trim();
        String goingDate = tv_goingDate.getText().toString().trim(); // TODO this should be taken from date variable
        String returnDate = tv_returnDate.getText().toString().trim(); // TODO this should be taken from date variable

        if (title.isEmpty()) {
            Toast.makeText(NewTourActivity.this, "Title can't be empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (names.size() == 0) {
            Toast.makeText(NewTourActivity.this, "At least one member must be added!", Toast.LENGTH_SHORT).show();
            return;
        }

        budget = budget.isEmpty() ? "0" : budget;
        Tour tour = new Tour(title, desc, goingDate, returnDate, Double.valueOf(budget));

        int tourRow_id = manager.createTour(tour); // inserting Tour To Database

        for (String name : names) {// add members to database
            manager.addMemberToTour(new Member(name, tourRow_id));
        }
        startActivity(new Intent(NewTourActivity.this, TourExpanses.class).putExtra("tourRowID", tourRow_id));
        finish();
    }

    public void addMember(View view) {
        String name = et_memberName.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(NewTourActivity.this, "Enter a name", Toast.LENGTH_SHORT).show();
            return;
        }
        adapter.add(name);
        et_memberName.getText().clear();
    }

}

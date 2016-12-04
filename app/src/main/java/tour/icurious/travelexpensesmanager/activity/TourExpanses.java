package tour.icurious.travelexpensesmanager.activity;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;

import tour.icurious.travelexpensesmanager.R;
import tour.icurious.travelexpensesmanager.dataModel.ExpField;
import tour.icurious.travelexpensesmanager.dataModel.ExpRowItem;
import tour.icurious.travelexpensesmanager.dataModel.Expense;
import tour.icurious.travelexpensesmanager.dataModel.Member;
import tour.icurious.travelexpensesmanager.database.TourManager;

public class TourExpanses extends AppCompatActivity {
    TableLayout table;
    TextView[][] txtGrid;
    int tourRow_id;
    EditText et_title;
    EditText et_amount;
    private int ROW_NUM;
    private int COL_NUM;
    private int counter;
    private ArrayList<Member> membersList;
    private ArrayList<ExpRowItem> expRowItemsList;
    private TourManager manager;
    private TableRow.LayoutParams llp;
    private DecimalFormat nf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_expanses);

        table = (TableLayout) findViewById(R.id.tableLayout);
        tourRow_id = getIntent().getIntExtra("tourRowID", 0);
        manager = new TourManager(this);
        nf = new DecimalFormat("#.##");


        generateView();
    }

    private void generateView() {
        counter = 0;
        table.removeAllViews();
        membersList = manager.getMembersByTourID(tourRow_id);
        expRowItemsList = manager.getExpRowItemsByTourID(tourRow_id);

        COL_NUM = membersList.size();
        ROW_NUM = expRowItemsList.size() > 0 ? expRowItemsList.size() / COL_NUM : 0;

        txtGrid = new TextView[ROW_NUM + 1][COL_NUM + 1];

        llp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT);
        llp.setMargins(3, 3, 3, 3);

        for (int row = 0; row <= ROW_NUM; row++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            createTableCol(row, tableRow);
        }
    }

    private void createTableCol(int row, TableRow tableRow) {
        for (int col = 0; col <= COL_NUM; col++) {
            final int FINAL_ROW = row;
            final int FINAL_COL = col;

            TextView txt = new TextView(this);

            if (row == 0) { // first row of the chart
                if (col == 0) { // null view (first item)
                    txt.setText("Sum");
                } else {
                    double totalExpenses = membersList.get(col - 1).getTotalExpenses();
                    if (totalExpenses == 0.0)  // title field or name of the members...
                        txt.setText(membersList.get(col - 1).getName());
                    else
                        txt.setText(membersList.get(col - 1).getName() + "\n" + nf.format(membersList.get(col - 1).getTotalExpenses()));
                }
                txt.setTypeface(null, Typeface.BOLD);
//                txt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                txt.setBackgroundResource(R.color.colorLightGreen);
            } else { // cost title and amounts row here
                if (col == 0) {
                    txt.setTypeface(null, Typeface.BOLD);
                    txt.setText(expRowItemsList.get(counter).getExpFields().getTitle()); // exp field names with total cost
                    txt.setBackgroundResource(R.color.colorDarkGray); // item colors
                } else {
                    txt.setText(nf.format(expRowItemsList.get(counter).getExpFields().getAmount()));
                    txt.setBackgroundResource(R.color.colorLightGray); // item colors
                    counter++;
                }
            }

            txt.setGravity(Gravity.CENTER);
            txt.setPadding(10, 25, 10, 25);
            txt.setLayoutParams(llp);
            txt.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    gridButtonClicked(FINAL_ROW, FINAL_COL);
                    return true;
                }
            });
            tableRow.addView(txt);
            txtGrid[row][col] = txt;
        }
    }

    private void gridButtonClicked(int row, int col) {
        final int fRow = row;
        final int fCol = col;

        if (row == 0) {
            if (col == 0) { // summery clicked
                AlertDialog.Builder builder = new AlertDialog.Builder(TourExpanses.this)
                        .setTitle("Summary")
                        .setMessage(manager.getTour(tourRow_id).toString())
                        .setPositiveButton("ok", null);

                builder.create().show();
            } else { // member name clicked

                final Dialog d = new Dialog(this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.add_cost);
                d.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = d.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                et_title = (EditText) d.findViewById(R.id.et_costTitle);
                et_amount = (EditText) d.findViewById(R.id.et_costAmount);

                et_title.setHint("Update name");
                et_title.setText(membersList.get(col - 1).getName());

                et_amount.setHint("Set deposit amount");
                et_amount.setText(nf.format(membersList.get(col - 1).getDeposit()));


                d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = et_title.getText().toString().trim();
                        String deposit = et_amount.getText().toString().trim();

                        if (name.isEmpty()) {
                            Toast.makeText(TourExpanses.this, "Name can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {

                            // todo member info update korte hobe
                            double totalExpenses = membersList.get(fCol - 1).getTotalExpenses();
                            if (totalExpenses == 0.0)
                                txtGrid[fRow][fCol].setText(name);
                            else
                                txtGrid[fRow][fCol].setText(name + "\n" + nf.format(membersList.get(fCol - 1).getTotalExpenses()));
                            d.dismiss();
                        }
                    }
                });
            }
        } else {
            if (col != 0) {
                final Dialog d = new Dialog(this);
                d.requestWindowFeature(Window.FEATURE_NO_TITLE);
                d.setContentView(R.layout.add_cost);
                d.show();
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                Window window = d.getWindow();
                lp.copyFrom(window.getAttributes());
                //This makes the dialog take up the full width
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                window.setAttributes(lp);


                et_title = (EditText) d.findViewById(R.id.et_costTitle);
                et_amount = (EditText) d.findViewById(R.id.et_costAmount);
                et_amount.setHint("New amount");

                et_title.setText("Old amount: " + txtGrid[row][col].getText());
                et_title.setFocusable(false);
                et_title.setFocusableInTouchMode(false);
                et_title.setClickable(false);


                d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amount = et_amount.getText().toString().trim();

                        if (amount.isEmpty()) {
                            Toast.makeText(TourExpanses.this, "Amount can't be empty!", Toast.LENGTH_SHORT).show();
                        } else {
                            txtGrid[fRow][fCol].setText(nf.format(amount));
                            d.dismiss(); // todo database e cost value change korte hobe
                        }
                    }
                });
            }
        }
    }

    public void fabClicked(View view) {

        final Dialog d = new Dialog(this);
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.setContentView(R.layout.add_cost);
        d.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = d.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);


        et_title = (EditText) d.findViewById(R.id.et_costTitle);
        et_amount = (EditText) d.findViewById(R.id.et_costAmount);


        d.findViewById(R.id.btn_addCost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String title = et_title.getText().toString().trim();
                String amount = et_amount.getText().toString().trim();

                if (title.isEmpty() || amount.isEmpty()) {
                    Toast.makeText(TourExpanses.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                } else {

                    double singleMembersAmount = Double.parseDouble(amount) / membersList.size();
                    int expFieldRowID = manager.addToExpField(new ExpField(title, singleMembersAmount));
                    for (Member m : membersList) {
                        Expense e = new Expense(tourRow_id, m.getId(), expFieldRowID);
                        manager.addExpenseToTour(e);
                        manager.calculateCostOfMembers(tourRow_id);
                    }
                    generateView();
                    d.dismiss();
                }
            }
        });
    }
}


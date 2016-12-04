package tour.icurious.travelexpensesmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import tour.icurious.travelexpensesmanager.dataModel.ExpField;
import tour.icurious.travelexpensesmanager.dataModel.ExpRowItem;
import tour.icurious.travelexpensesmanager.dataModel.Expense;
import tour.icurious.travelexpensesmanager.dataModel.Member;
import tour.icurious.travelexpensesmanager.dataModel.Tour;

public class TourManager {
    private DatabaseHelper mHelper;
    private SQLiteDatabase mDatabase;
    private String[] tourColumns = {
            DatabaseHelper.COL_ID,
            DatabaseHelper.COL_TITLE,
            DatabaseHelper.COL_DESCRIPTION,
            DatabaseHelper.COL_GOING_DATE,
            DatabaseHelper.COL_RETURN_DATE,
            DatabaseHelper.COL_BUDGET,
            DatabaseHelper.COL_TOTAL_EXPENSES};

    private String[] membersColumns = {
            DatabaseHelper.COL_ID,
            DatabaseHelper.COL_NAME,
            DatabaseHelper.COL_TOUR_ID,
            DatabaseHelper.COL_DEPOSIT,
            DatabaseHelper.COL_TOTAL_EXPENSES};


    public TourManager(Context context) {
        mHelper = new DatabaseHelper(context);
    }

    private void openDB() {
        mDatabase = mHelper.getWritableDatabase();
    }

    private void closeDB() {
        mHelper.close();
        mDatabase.close();
    }

    public int createTour(Tour tour) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, tour.getTitle());
        cv.put(DatabaseHelper.COL_DESCRIPTION, tour.getDescription());
        cv.put(DatabaseHelper.COL_GOING_DATE, tour.getGoingDate());
        cv.put(DatabaseHelper.COL_RETURN_DATE, tour.getReturnDate());
        cv.put(DatabaseHelper.COL_BUDGET, tour.getBudget());
        cv.put(DatabaseHelper.COL_TOTAL_EXPENSES, tour.getTotalExpenses());

        openDB();
        long inserted = mDatabase.insert(DatabaseHelper.TABLE_TOUR, null, cv);
        closeDB();

        return (int) inserted;
    }

    public Tour getTour(int id) { // get single contact from database
        Tour tour = new Tour();
        openDB();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOUR,
                tourColumns,
                DatabaseHelper.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            tour.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
            tour.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
            tour.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRIPTION)));
            tour.setGoingDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GOING_DATE)));
            tour.setReturnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RETURN_DATE)));
            tour.setBudget(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BUDGET))));
            tour.setTotalExpenses(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_EXPENSES))));
            cursor.close();
        }
        closeDB();
        return tour;
    }

    public ArrayList<Tour> getTourList() { // get list of tours from database
        openDB();
        ArrayList<Tour> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_TOUR, tourColumns, null, null, null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Tour tour = new Tour();
                tour.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
                tour.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
                tour.setDescription(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DESCRIPTION)));
                tour.setGoingDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_GOING_DATE)));
                tour.setReturnDate(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_RETURN_DATE)));
                tour.setBudget(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_BUDGET))));
                tour.setTotalExpenses(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_EXPENSES))));
                list.add(tour);
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDB();
        return list;
    }

    public boolean updateTour(int id, Tour tour) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, tour.getTitle());
        cv.put(DatabaseHelper.COL_DESCRIPTION, tour.getDescription());
        cv.put(DatabaseHelper.COL_GOING_DATE, tour.getGoingDate());
        cv.put(DatabaseHelper.COL_RETURN_DATE, tour.getReturnDate());
        cv.put(DatabaseHelper.COL_BUDGET, tour.getBudget());
        cv.put(DatabaseHelper.COL_TOTAL_EXPENSES, tour.getTotalExpenses());

        openDB();
        int updated = mDatabase.update(DatabaseHelper.TABLE_TOUR, cv, DatabaseHelper.COL_ID + " = " + id, null);
        closeDB();
        return updated > 0;
    }

    public void deleteTour(int id) {
        openDB();
        deleteExpFieldBtTourID(id);
        mDatabase.delete(DatabaseHelper.TABLE_TOUR, DatabaseHelper.COL_ID + " = " + id, null);
        mDatabase.delete(DatabaseHelper.TABLE_MEMBERS, DatabaseHelper.COL_TOUR_ID + " = " + id, null);
        mDatabase.delete(DatabaseHelper.TABLE_EXPENSES, DatabaseHelper.COL_TOUR_ID + " = " + id, null);
        closeDB();
    }

    private void deleteExpFieldBtTourID(int id) {
        // TODO Delete expFields data according to their tour id...
    }

    public void addMemberToTour(Member m) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_NAME, m.getName());
        cv.put(DatabaseHelper.COL_TOUR_ID, m.getTour_id());
        cv.put(DatabaseHelper.COL_DEPOSIT, m.getDeposit());
        cv.put(DatabaseHelper.COL_TOTAL_EXPENSES, m.getTotalExpenses());

        openDB();
        mDatabase.insert(DatabaseHelper.TABLE_MEMBERS, null, cv);
        closeDB();
    }

    public ArrayList<Member> getMembersByTourID(int tourID) {
        openDB();
        ArrayList<Member> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_MEMBERS,
                membersColumns,
                DatabaseHelper.COL_TOUR_ID + " =? ",
                new String[]{String.valueOf(tourID)},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Member m = new Member();
                m.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
                m.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME)));
                m.setTour_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOUR_ID))));
                m.setDeposit(Double.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DEPOSIT))));
                m.setTotalExpenses(Double.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_EXPENSES))));
                list.add(m);
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDB();
        return list;
    }

    private ExpField getExpFieldByID(String id) {
        openDB();
        ExpField e = new ExpField();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_EXP_FIELDS,
                new String[]{DatabaseHelper.COL_TITLE, DatabaseHelper.COL_AMOUNT},
                DatabaseHelper.COL_ID + " =? ",
                new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            e.setTitle(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TITLE)));
            e.setAmount(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_AMOUNT))));
            cursor.close();
        }
        closeDB();
        return e;

    }

    private Member getMemberByID(String id) {
        openDB();
        Member m = new Member();
        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_MEMBERS,
                membersColumns,
                DatabaseHelper.COL_ID + " =? ",
                new String[]{id},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            m.setId(cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COL_ID)));
            m.setName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_NAME)));
            m.setTour_id(Integer.valueOf(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOUR_ID))));
            m.setDeposit(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_DEPOSIT))));
            m.setTotalExpenses(Double.parseDouble(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_TOTAL_EXPENSES))));
            cursor.close();
        }
        closeDB();
        return m;
    }

    public int addToExpField(ExpField field) {

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TITLE, field.getTitle());
        cv.put(DatabaseHelper.COL_AMOUNT, field.getAmount());

        openDB();
        long inserted = mDatabase.insert(DatabaseHelper.TABLE_EXP_FIELDS, null, cv);
        closeDB();
        return (int) inserted;
    }

    public void addExpenseToTour(Expense e) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_TOUR_ID, e.getTour_id());
        cv.put(DatabaseHelper.COL_MEMBER_ID, e.getMember_id());
        cv.put(DatabaseHelper.COL_EXP_FIELD_ID, e.getExp_field_id());

        openDB();
        mDatabase.insert(DatabaseHelper.TABLE_EXPENSES, null, cv);
        closeDB();
    }

    public ArrayList<ExpRowItem> getExpRowItemsByTourID(int tourID) {
        openDB();
        ArrayList<ExpRowItem> list = new ArrayList<>();

        Cursor cursor = mDatabase.query(DatabaseHelper.TABLE_EXPENSES,
                new String[]{DatabaseHelper.COL_MEMBER_ID, DatabaseHelper.COL_EXP_FIELD_ID},
                DatabaseHelper.COL_TOUR_ID + " =? ",
                new String[]{String.valueOf(tourID)},
                null, null, null);

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Member m = getMemberByID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_MEMBER_ID)));
                ExpField e = getExpFieldByID(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_EXP_FIELD_ID)));

                list.add(new ExpRowItem(m, e));
            } while (cursor.moveToNext());
            cursor.close();
        }
        closeDB();
        return list;
    }

    public void calculateCostOfMembers(int id) {

    }

    /*
             private final String MY_QUERY = "SELECT * FROM table_a a INNER JOIN table_b b ON a.id=b.other_id WHERE b.property_id=?";
            db.rawQuery(MY_QUERY, new String[]{String.valueOf(propertyId)});

            Cursor curs = mDatabase.rawQuery("SELECT member_id, title, amount FROM expenses INNER JOIN exp_fields ON expenses.exp_field_id=exp_fields.id", new String[]{String.valueOf()});


            Cursor c = mDatabase.rawQuery("SELECT member_id, title, amount FROM expenses INNER JOIN exp_fields ON expenses.exp_field_id = exp_fields.id", null);
    */
}

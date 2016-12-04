package tour.icurious.travelexpensesmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {
    // DB name and version
    private static final String DATABASE_NAME = "tour_expenses_db";
    private static final int VERSION_NO = 1;

    // tour table and columns declaration...
    public static final String TABLE_TOUR = "tour";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "title";
    public static final String COL_DESCRIPTION = "description";
    public static final String COL_GOING_DATE = "going_date";
    public static final String COL_RETURN_DATE = "return_date";
    public static final String COL_BUDGET = "budget";
    public static final String COL_TOTAL_EXPENSES = "total_expenses";

    private static final String SQL_TOUR_TABLE = "CREATE TABLE " + TABLE_TOUR + " ( " +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_TITLE + " TEXT," +
            COL_DESCRIPTION + " TEXT," +
            COL_GOING_DATE + " TEXT," +
            COL_RETURN_DATE + " TEXT," +
            COL_BUDGET + " TEXT," +
            COL_TOTAL_EXPENSES + " TEXT)";


    // member table and columns declaration...
    public static final String TABLE_MEMBERS = "members";
    //    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_TOUR_ID = "tour_id";
    public static final String COL_DEPOSIT = "deposit";
    //    public static final String COL_TOTAL_EXPENSES = "total_expenses";

    private static final String SQL_MEMBER_TABLE = "CREATE TABLE " + TABLE_MEMBERS + " ( " +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_NAME + " TEXT," +
            COL_TOUR_ID + " TEXT," +
            COL_DEPOSIT + " TEXT," +
            COL_TOTAL_EXPENSES + " TEXT)";


    // exp_field table and columns declaration...
    public static final String TABLE_EXP_FIELDS = "exp_fields";
    //    public static final String COL_ID = "id";
    //    public static final String COL_TITLE = "title";
    public static final String COL_AMOUNT = "amount";

    private static final String SQL_EXP_FIELD_TABLE = "CREATE TABLE " + TABLE_EXP_FIELDS + " ( " +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_TITLE + " TEXT," +
            COL_AMOUNT + " TEXT)";


    // expenses table and columns declaration...
    public static final String TABLE_EXPENSES = "expenses";
    //    public static final String COL_ID = "id";
    //    public static final String COL_TOUR_ID = "tour_id";
    public static final String COL_MEMBER_ID = "member_id";
    public static final String COL_EXP_FIELD_ID = "exp_field_id";

    private static final String SQL_EXPENSES_TABLE = "CREATE TABLE " + TABLE_EXPENSES + " ( " +
            COL_ID + " INTEGER PRIMARY KEY, " +
            COL_TOUR_ID + " TEXT," +
            COL_MEMBER_ID + " TEXT," +
            COL_EXP_FIELD_ID + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NO);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TOUR_TABLE);
        sqLiteDatabase.execSQL(SQL_MEMBER_TABLE);
        sqLiteDatabase.execSQL(SQL_EXP_FIELD_TABLE);
        sqLiteDatabase.execSQL(SQL_EXPENSES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}

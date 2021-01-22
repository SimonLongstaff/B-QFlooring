package BQ.Flooring.ui.Order;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OrderDatabase extends SQLiteOpenHelper {


    public OrderDatabase(@Nullable Context context) {
        super(context, "OrderDatabase", null, 1);
    }

    //Table names
    public static final String ORDER_TABLE = "orderTable";

    //Column names
    public static final String UID = "UID";
    public static final String BARCODE = "Barcode";
    public static final String AMOUNT = "Amount";

    //Table Creation
    private static final String CREATE_ORDER_TABLE = "CREATE TABLE " + ORDER_TABLE + " (" + UID +
            " INTEGER PRIMARY KEY AUTOINCREMENT ," + BARCODE + " TEXT," + AMOUNT + " TEXT" + ")";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ORDER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ORDER_TABLE);
        onCreate(db);

    }

    public  void insertBarcode(String code, String num){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(BARCODE,code);
        cv.put(AMOUNT, num);
        db.insert(ORDER_TABLE,null,cv);
        db.close();

    }

    public void deleteBarcode(String code){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ORDER_TABLE, BARCODE + " = " + "'" + code + "'", null);
        db.close();
    }
}

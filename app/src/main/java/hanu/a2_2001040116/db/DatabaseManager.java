package hanu.a2_2001040116.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanu.a2_2001040116.MainActivity;
import hanu.a2_2001040116.models.Product;

public class DatabaseManager {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    private static final String table = "items";
    private static final String dbName = "mycart.db";

    public DatabaseManager(Context context) {
        this.helper = new DatabaseHelper(context);
        this.db = helper.getWritableDatabase();
    }

    public long insertAnItem(Product product, int quantity) {
        ContentValues values = new ContentValues();
        values.put("id", product.getId());
        values.put("name", product.getName());
        values.put("category", product.getCategory());
        values.put("thumbnail", product.getThumbnail());
        values.put("unitPrice", product.getUnitPrice());
        values.put("quantity", 1);
        return db.insert(table, null, values);
    }

    @SuppressLint("Range")
    public Map<Product, Integer> getAnItemById(int id) {
        Map<Product, Integer> map = new HashMap<>();
        String query = "SELECT * FROM "+ table +" WHERE id = ?";
        String[] values = new String[]{String.valueOf(id)};
        Cursor cursor = db.rawQuery(query, values);
        if(cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String category = cursor.getString(cursor.getColumnIndex("category"));
            String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
            long unitPrice = cursor.getLong(cursor.getColumnIndex("unitPrice"));
            int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));
            Product product = new Product(id, thumbnail, name, category, unitPrice);
            map.put(product, quantity);
            cursor.close();
        }
        return map;
    }

    public boolean updateAnItem(Product product, int quantity) {
        ContentValues values = new ContentValues();
        values.put("name", product.getName());
        values.put("category", product.getCategory());
        values.put("thumbnail", product.getThumbnail());
        values.put("unitPrice", product.getUnitPrice());
        values.put("quantity", quantity);
        String whereClause = "id = ?";
        String[] where = new String[]{String.valueOf(product.getId())};

        int num = db.update(table, values, whereClause, where);
        return num > 0;
    }

    @SuppressLint("Range")
    public List<Map<Product, Integer>> getAllItems() {
        String[] columns = {"id", "name", "category", "thumbnail", "unitPrice", "quantity"};
        List<Map<Product, Integer>> items = new ArrayList<>();
        Map<Product, Integer> map = new HashMap<>();
        Cursor cursor = db.query(table, columns, null, null, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String category = cursor.getString(cursor.getColumnIndex("category"));
                String thumbnail = cursor.getString(cursor.getColumnIndex("thumbnail"));
                long unitPrice = cursor.getLong(cursor.getColumnIndex("unitPrice"));
                int quantity = cursor.getInt(cursor.getColumnIndex("quantity"));

                Product product = new Product(id, thumbnail, name, category, unitPrice);
                map.put(product, quantity);
                items.add(map);
                map = new HashMap<>();
            } while (cursor.moveToNext());
            cursor.close();
        }
        return items;
    }

    public boolean deleteAnItemById(int id) {
        String whereClause = "id = ?";
        String[] where = new String[]{String.valueOf(id)};
        int num = db.delete(table, whereClause, where);
        return num > 0;
    }
}

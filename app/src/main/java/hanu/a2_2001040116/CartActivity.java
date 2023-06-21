package hanu.a2_2001040116;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hanu.a2_2001040116.adapters.CallBackInCartAdapter;
import hanu.a2_2001040116.adapters.ProductInCartAdapter;
import hanu.a2_2001040116.db.DatabaseManager;
import hanu.a2_2001040116.models.Product;

public class CartActivity extends AppCompatActivity implements CallBackInCartAdapter {

    DatabaseManager databaseManager;
    List<Map<Product, Integer>> items;
    RecyclerView recyclerView;
    TextView total;
    ProductInCartAdapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toast.makeText(this, "Switch to cart", Toast.LENGTH_SHORT).show();
        databaseManager = new DatabaseManager(this);
        total = findViewById(R.id.total);
        recyclerView = findViewById(R.id.recycler_view_in_cart);
        items = databaseManager.getAllItems();
        refreshTotal();
        adapter = new ProductInCartAdapter(items, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void handlePlus(int position) {
        Map<Product, Integer> item = items.get(position);
        Product product = (Product) item.keySet().toArray()[0];
        item.replace(product, item.get(product) + 1);
        databaseManager.updateAnItem(product, item.get(product));
        refreshTotal();
    }

    @Override
    public void handleMinus(int position) {
        Map<Product, Integer> item = items.get(position);
        Product product = (Product) item.keySet().toArray()[0];
        if (item.get(product) > 1) {
            item.replace(product, item.get(product) - 1);
            databaseManager.updateAnItem(product, item.get(product));
        } else {
            items.remove(item);
            databaseManager.deleteAnItemById(product.getId());
        }
        refreshTotal();
    }

    public void refreshTotal() {
        long total = 0;
        for (Map map : items) {
            Product product = (Product) map.keySet().toArray()[0];
            total += product.getUnitPrice() * (int) map.get(product);
        }
        this.total.setText("đ " + String.valueOf(total));
    }
}
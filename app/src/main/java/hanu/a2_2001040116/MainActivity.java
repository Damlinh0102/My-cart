package hanu.a2_2001040116;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import hanu.a2_2001040116.adapters.CallBackAdapter;
import hanu.a2_2001040116.adapters.ProductAdapter;
import hanu.a2_2001040116.db.DatabaseManager;
import hanu.a2_2001040116.models.Product;
import hanu.a2_2001040116.models.ProductDataCallBack;

public class MainActivity extends AppCompatActivity implements CallBackAdapter, ProductDataCallBack {
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    List<Product> products;
    ImageButton search_icon;
    EditText search_word;
    DatabaseManager dbmanager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbmanager = new DatabaseManager(this);
        recyclerView = findViewById(R.id.recyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        search_word = findViewById(R.id.search_word);
        search_icon = findViewById(R.id.search_icon);
        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(search_word.getText().toString());
            }
        });
        ImageButton x_icon = findViewById(R.id.x_icon);
        x_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_word.setText(null);
                displayProducts(products);
            }
        });

        Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
        Constants.executor.execute(new Runnable() {
            @Override
            public void run() {
                List<Product> productList = new ArrayList<>();
                String jsonString = fetchFromAPI();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(jsonString != null) {
                            System.err.println("Error");
                        }
                        try {
                            JSONArray jsonArray = new JSONArray(jsonString);
                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                int id = json.getInt("id");
                                String name = json.getString("name");
                                String thumbnail = json.getString("thumbnail");
                                String category = json.getString("category");
                                long unitPrice = json.getLong("unitPrice");

                                productList.add(new Product(id, thumbnail, name, category, unitPrice));
                                products = productList;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onProductDataLoaded(products);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.cart) {
            Toast.makeText(this, "Switch to cart", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, CartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    public String fetchFromAPI() {
        String urlString = "https://hanu-congnv.github.io/mpr-cart-api/products.json";
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scanner sc = null;
        try {
            sc = new Scanner(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuffer result = new StringBuffer();
        String line;
        while (sc.hasNext()) {
            line = sc.nextLine();
            result.append(line);
        }
        return result.toString();
    }
    @Override
    public void onClickCart(int id) {
        for(int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if(product.getId() == id) {
                Map<Product, Integer> map = dbmanager.getAnItemById(product.getId());
                if(!map.isEmpty()) {
                    if(map.get(product) != null) {
                        dbmanager.updateAnItem(product, map.get(product) + 1);
                    }
                } else {
                    dbmanager.insertAnItem(product, 1);
                }
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void displayProducts(List<Product> products) {
        productAdapter = new ProductAdapter(products, this);
        recyclerView.setAdapter(productAdapter);
    }
    public void search(String keywordName) {
        List<Product> searched_products = new ArrayList<>();
        for(Product product: products) {
            if(product.getName().toLowerCase().contains(keywordName.toLowerCase())) {
                searched_products.add(product);
            }
        }
        displayProducts(searched_products);
    }

    @Override
    public void onProductDataLoaded(List<Product> products) {
        displayProducts(products);
    }
}
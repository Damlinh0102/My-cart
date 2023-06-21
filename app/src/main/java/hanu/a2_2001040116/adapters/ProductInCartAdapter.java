package hanu.a2_2001040116.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import hanu.a2_2001040116.Constants;
import hanu.a2_2001040116.R;
import hanu.a2_2001040116.models.Product;

public class ProductInCartAdapter extends RecyclerView.Adapter<ProductInCartAdapter.ProductInCartHolder>{

    private List<Map<Product, Integer>> items;
    private CallBackInCartAdapter callBackInCartAdapter;

    public ProductInCartAdapter(List<Map<Product, Integer>> items, CallBackInCartAdapter callBackInCartAdapter) {
        this.items = items;
        this.callBackInCartAdapter = callBackInCartAdapter;
    }

    @NonNull
    @Override
    public ProductInCartAdapter.ProductInCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.item_product_in_cart, parent, false);
        return new ProductInCartAdapter.ProductInCartHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductInCartAdapter.ProductInCartHolder holder, int position) {
        Map<Product, Integer> item = items.get(position);
        holder.bind(item);
        Product product = (Product) item.keySet().toArray()[0];
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackInCartAdapter.handleMinus(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackInCartAdapter.handlePlus(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ProductInCartHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView price;
        TextView sumPrice;
        TextView quantity;
        ImageButton plus;
        ImageButton minus;
        public ProductInCartHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView);
            name = itemView.findViewById(R.id.name_in_cart);
            price = itemView.findViewById(R.id.price_in_cart);
            sumPrice = itemView.findViewById(R.id.sumPrice);
            quantity = itemView.findViewById(R.id.quantity);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
        }

        public void bind(Map<Product, Integer> item) {
            Product product = (Product) item.keySet().toArray()[0];
            Handler handler = HandlerCompat.createAsync(Looper.getMainLooper());
            Constants.executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(product.getThumbnail());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                image.setImageBitmap(bitmap);
                            }
                        });
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            name.setText(product.getName());
            price.setText(String.valueOf(product.getUnitPrice()) + "đ");
            int quant = item.get(product);
            quantity.setText(String.valueOf(quant));
            sumPrice.setText(String.valueOf((int) (quant * product.getUnitPrice())));
        }
        public void update(Map<Product, Integer> item) {
            Product product = (Product) item.keySet().toArray()[0];
            int quant = item.get(product);
            quantity.setText(String.valueOf(quant));
            sumPrice.setText(String.valueOf((int) (quant * product.getUnitPrice())));
        }
    }
}

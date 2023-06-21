package hanu.a2_2001040116.adapters;

import android.annotation.SuppressLint;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import hanu.a2_2001040116.Constants;
import hanu.a2_2001040116.R;
import hanu.a2_2001040116.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder>{
    private List<Product> products;
    private CallBackAdapter callBackAdapter;

    public ProductAdapter(List<Product> products, CallBackAdapter callBackAdapter) {
        this.products = products;
        this.callBackAdapter = callBackAdapter;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View viewItem = layoutInflater.inflate(R.layout.item_product, parent, false);
        return new ProductHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, @SuppressLint("RecyclerView") int position) {
        Product product = products.get(position);
        holder.bind(product);
        holder.iconCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBackAdapter.onClickCart(product.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;
        private TextView price;
        private ImageButton iconCart;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            iconCart = itemView.findViewById(R.id.iconCart);
        }

        @SuppressLint("SetTextI18n")
        public void bind(Product product) {
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
            price.setText(String.valueOf(product.getUnitPrice()));
        }
    }
}

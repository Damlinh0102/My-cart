package hanu.a2_2001040116.models;

import androidx.annotation.Nullable;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String thumbnail;
    private String name;
    private String category;
    private long unitPrice;

    public Product(int id, String thumbnail, String name, String category, long unitPrice) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.name = name;
        this.category = category;
        this.unitPrice = unitPrice;
    }

    public int getId() {
        return id;
    }


    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(long unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public boolean equals(@Nullable Object ob) {
        Product product = (Product) ob;
        return id == product.getId() && name.equals(product.getName()) && category.equals(product.getCategory())
                && thumbnail.equals(product.getThumbnail()) && unitPrice == product.getUnitPrice();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }
}

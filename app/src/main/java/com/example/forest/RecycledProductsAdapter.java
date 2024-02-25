
package com.example.forest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;

public class RecycledProductsAdapter extends RecyclerView.Adapter<RecycledProductsAdapter.ViewHolder> {

    private List<RecycledProduct> productList;
    private OnProductClickListener onProductClickListener;

    public RecycledProductsAdapter(List<RecycledProduct> productList, OnProductClickListener onProductClickListener) {
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecycledProduct product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView productName, productQuantity, productPrice, location;
        ImageView productImage;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productNameText);
            productQuantity = itemView.findViewById(R.id.productQuantityText);
            productPrice = itemView.findViewById(R.id.productPriceText);
            location = itemView.findViewById(R.id.productLocationText);
            productImage = itemView.findViewById(R.id.productImageUrl);
            itemView.setOnClickListener(this);
        }

        public void bind(RecycledProduct product) {
            productName.setText(product.getProductName());
            productQuantity.setText(product.getProductQuantity());
            productPrice.setText(product.getProductPrice());
            location.setText(product.getProductLocation());
            Glide.with(productImage)
                    .load(product.getProductImage())
                    .into(productImage);
        }

        @Override
        public void onClick(View view) {
            if (onProductClickListener != null) {
                onProductClickListener.onProductClick(getAdapterPosition());
            }
        }
    }

    public interface OnProductClickListener {
        void onProductClick(int position);
    }
}

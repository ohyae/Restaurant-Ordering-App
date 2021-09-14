package com.example.madfinal;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {
    LinkedList<Product> mProductList;
    LayoutInflater mInflater;
    Context ct;

    public ProductListAdapter(Context context, LinkedList<Product> ProductList){

        mInflater = LayoutInflater.from(context);
        this.mProductList = ProductList;
        ct=context;

    }

    public ProductListAdapter(Runnable runnable, LinkedList<String> productList) {

    }

    public class MyViewHolder extends  RecyclerView.ViewHolder {
        public TextView product_title;
        public TextView product_price;
        public TextView product_qty;
        public TextView product_id;
        public ImageView btn_dec;
        public ImageView btn_inc;



        public MyViewHolder(@NonNull final View itemView) {
            super(itemView);
            product_id = itemView.findViewById(R.id.product_id);
            product_title = itemView.findViewById(R.id.product_title);
            product_price = itemView.findViewById(R.id.product_price);
            product_qty = itemView.findViewById(R.id.product_qty);
            btn_dec = itemView.findViewById(R.id.btn_dec);
            btn_inc = itemView.findViewById(R.id.btn_inc);


        }
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.product_layout,parent,false);
        return new MyViewHolder(mItemView);

    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Product mCurrent = mProductList.get(position);
        holder.product_id.setText(String.valueOf(mCurrent.getId()));
        holder.product_qty.setText("0");
        holder.product_title.setText(mCurrent.getTitle());
        holder.product_price.setText(String.format("%.2f", mCurrent.getPrice()));

       if (ct.getClass().getSimpleName().equals("PaymentActivity")) {
            holder.btn_inc.setVisibility(View.GONE);
            holder.btn_dec.setVisibility(View.GONE);
            holder.product_qty.setVisibility(View.GONE);
            }


            holder.btn_inc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(holder.product_qty.getText().toString());
                    quantity = quantity + 1;
                    holder.product_qty.setText(quantity + "");
                    float productPrice = Float.parseFloat(holder.product_price.getText().toString());
                    int prodID = Integer.parseInt(holder.product_id.getText().toString());
                    OrderActivity.countOrder(productPrice, quantity, prodID);
                }
            });
            holder.btn_dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int quantity = Integer.parseInt(holder.product_qty.getText().toString());
                    if (quantity > 0) {
                        quantity = quantity - 1;

                        holder.product_qty.setText(quantity + "");
                        float productPrice = Float.parseFloat(holder.product_price.getText().toString());
                        int prodID = Integer.parseInt(holder.product_id.getText().toString());
                        OrderActivity.countOrder1(productPrice, quantity, prodID);
                    }

                }
            });
        }

    @Override
    public int getItemCount() {
        return mProductList.size();

    }

}


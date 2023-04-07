package com.example.sandbox;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    CatergoryDataModel[] data;
    CategoryAdapter(CatergoryDataModel[] data){
        this.data=data;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View listItem=inflater.inflate(R.layout.categories_item_design,parent,false);
        ViewHolder viewHolder=new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, int position) {
        int pos=position;
        final CatergoryDataModel myListData=data[position];
        holder.tv.setText(data[position].getDescription());
        holder.iv.setImageResource(data[position].getImage());
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(v.getContext(),CategoryGameDescription.class);
                i.putExtra("category",""+data[pos].getDescription());
                v.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv;
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv = itemView.findViewById(R.id.CategoryImage);
            this.tv = itemView.findViewById(R.id.CategoryDescription);
        }
    }
}

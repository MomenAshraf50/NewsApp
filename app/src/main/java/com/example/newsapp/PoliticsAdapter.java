package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PoliticsAdapter extends RecyclerView.Adapter<PoliticsAdapter.PoliticsHolder> {
    List<Article> list ;
    Context context;


    public PoliticsAdapter(List<Article> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PoliticsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new PoliticsHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PoliticsHolder holder, int position) {
        Article article = list.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewTime.setText(article.getPublishedAt());
        if (article.getUrlToImage()!=null){
            String url = article.getUrlToImage().toString();
            Picasso.with(context).load(url)
                    .into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,NewsViewer.class);
                intent.putExtra("url",article.getUrl());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class PoliticsHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textViewTitle,textViewTime;
        public PoliticsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.news_time_tv);
            textViewTitle = itemView.findViewById(R.id.news_title_tv);
            imageView = itemView.findViewById(R.id.item_image_view);
        }
    }
}

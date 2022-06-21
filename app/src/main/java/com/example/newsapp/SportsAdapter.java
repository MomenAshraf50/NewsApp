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

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.SportsHolder> implements Filterable {
    List<Article> list ;
    Context context;
    List<Article> filteredArticles;

    public SportsAdapter(List<Article> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SportsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SportsHolder(LayoutInflater.from(parent.getContext())
        .inflate(R.layout.news_items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SportsHolder holder, int position) {
        Article article = list.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewTime.setText(article.getPublishedAt());
        if (article.getUrlToImage()!=null){
            String url = article.getUrlToImage().toString();
            Picasso.with(context).load(url).into(holder.imageView);
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

    @Override
    public Filter getFilter() {
        Filter filter =new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                if (constraint.toString().isEmpty()){
                    filteredArticles = list;
                }else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article article: filteredArticles){
                        if (article.getTitle().toString().contains(constraint.toString().toLowerCase())){
                            filteredList.add(article);
                        }
                    }
                    filteredArticles = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredArticles;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                list.clear();
                list.addAll((Collection<? extends Article>) results);
                notifyDataSetChanged();
            }
        };
        return filter;
    }


    class SportsHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewTitle,textViewTime;
        public SportsHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewTime = itemView.findViewById(R.id.news_time_tv);
            textViewTitle = itemView.findViewById(R.id.news_title_tv);
            imageView = itemView.findViewById(R.id.item_image_view);
        }

    }
}

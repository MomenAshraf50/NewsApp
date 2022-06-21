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

public class SearchAdapter extends RecyclerView.Adapter<SearchHolder> implements Filterable {
    List<Article> articleList;
    Context context;
    List<Article> articlesAll;



    public SearchAdapter(List<Article> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
        this.articlesAll = new ArrayList<>(articleList);

    }

    @NonNull
    @NotNull
    @Override
    public SearchHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SearchHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchHolder holder, int position) {
        Article article = articleList.get(position);
        holder.textViewTitle.setText(article.getTitle());
        holder.textViewTime.setText(article.getPublishedAt());
        if (article.getUrlToImage() != null) {
            String url = article.getUrlToImage().toString();
            Picasso.with(context).load(url)
                    .into(holder.imageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsViewer.class);
                intent.putExtra("url", article.getUrl());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Article> filteredArticles = new ArrayList<>();
                if (constraint.toString().isEmpty()) {
                    filteredArticles.addAll(articlesAll);
                } else {
                    List<Article> filteredList = new ArrayList<>();
                    for (Article article : articlesAll) {
                        if (article.getTitle().contains(constraint.toString().toLowerCase())) {
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
                articleList.clear();
                articleList.addAll((Collection<? extends Article>) results.values);
                notifyDataSetChanged();
            }
        };
        return filter;
    }



}

    class SearchHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle,textViewTime;
        ImageView imageView;
        public SearchHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.news_title_tv);
            textViewTime = itemView.findViewById(R.id.news_time_tv);
            imageView = itemView.findViewById(R.id.item_image_view);
        }
}


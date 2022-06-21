package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    SearchView searchView;
    RecyclerView recyclerViewSearch;
    SearchAdapter searchAdapter;
    String q = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
    searchView= findViewById(R.id.search_activity);
    recyclerViewSearch = findViewById(R.id.search_rv);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText);
                q = newText;
                return false;
            }
        });
        SearchRetrofitClient.getService().getNews(q)
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        NewsResponse newsResponse = response.body();
                        searchAdapter= new SearchAdapter(newsResponse.getArticles(),SearchActivity.this);
                        recyclerViewSearch.setAdapter(searchAdapter);
                        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        Toast.makeText(SearchActivity.this, errorMessage, Toast.LENGTH_SHORT).show();

                    }
                });


    }

}
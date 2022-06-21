package com.example.newsapp.ui.technology;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.NewsResponse;
import com.example.newsapp.R;
import com.example.newsapp.RetrofitClient;
import com.example.newsapp.TechnologyAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TechnologyNewsFragment extends Fragment {
    RecyclerView recyclerViewTechnology;
    TechnologyAdapter technologyAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_technology_news,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewTechnology = view.findViewById(R.id.technology_rv);

        RetrofitClient.getService().getNews("technology")
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful() && response.body()!=null){
                            NewsResponse newsResponse = response.body();
                            technologyAdapter = new TechnologyAdapter(newsResponse.getArticles(),getContext());
                            recyclerViewTechnology.setAdapter(technologyAdapter);
                            recyclerViewTechnology.setLayoutManager(new LinearLayoutManager(getContext()));
                        }
                    }

                    @Override
                    public void onFailure(Call<NewsResponse> call, Throwable t) {
                        String errorMessage = t.getMessage();
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
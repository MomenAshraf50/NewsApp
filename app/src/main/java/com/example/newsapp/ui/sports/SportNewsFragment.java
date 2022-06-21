package com.example.newsapp.ui.sports;

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
import com.example.newsapp.SportsAdapter;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SportNewsFragment extends Fragment {
    RecyclerView recyclerViewSports;
    SportsAdapter sportsAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sport_news,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewSports = view.findViewById(R.id.sports_rv);

        RetrofitClient.getService().getNews("sports")
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            NewsResponse newsResponse = response.body();
                            sportsAdapter = new SportsAdapter(newsResponse.getArticles(),getContext());
                            recyclerViewSports.setAdapter(sportsAdapter);
                            recyclerViewSports.setLayoutManager(new LinearLayoutManager(getContext()));
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
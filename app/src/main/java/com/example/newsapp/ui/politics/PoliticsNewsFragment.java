package com.example.newsapp.ui.politics;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsapp.NewsResponse;
import com.example.newsapp.PoliticsAdapter;
import com.example.newsapp.R;
import com.example.newsapp.RetrofitClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.jetbrains.annotations.NotNull;

import java.util.zip.Inflater;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PoliticsNewsFragment extends Fragment {
    RecyclerView recyclerViewPolitics;
    PoliticsAdapter politicsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_politics_news,container,false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerViewPolitics = view.findViewById(R.id.home_rv);
        RetrofitClient.getService().getNews("politics")
                .enqueue(new Callback<NewsResponse>() {
                    @Override
                    public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                        if (response.isSuccessful()&&response.body()!=null){
                            NewsResponse newsResponse = response.body();
                            politicsAdapter = new PoliticsAdapter(newsResponse.getArticles(),getContext());
                            recyclerViewPolitics.setAdapter(politicsAdapter);
                            recyclerViewPolitics.setLayoutManager(new LinearLayoutManager(getContext()));

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
package com.example.amir.scancer.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amir.scancer.R;
import com.example.amir.scancer.adapters.NewsAdapter;
import com.example.amir.scancer.databinding.FragmentDashboardBinding;
import com.example.amir.scancer.models.News;
import com.example.amir.scancer.models.NewsResponse;
import com.example.amir.scancer.retrofit.NewsInterface;

import java.util.ArrayList;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class DashboardFragment extends Fragment {
    private NewsAdapter newsAdapter;
    private ArrayList<News> news;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private static final String URL = "https://newsapi.org/";
    private static final String API_KEY = "ba46babbd9f644e9b890d03fc380f9e8";

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        news = new ArrayList<>();
        progressBar=(ProgressBar)root.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) root.findViewById(R.id.recycler_view_news);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        
        getNews();



        return root;
    }

    private void getNews() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsInterface newsInterface = retrofit.create(NewsInterface.class);

        Call<NewsResponse> call = newsInterface.getLatestNews("techcrunch" , API_KEY);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body()!=null){
                    news = new ArrayList<>(response.body().news);
                    newsAdapter = new NewsAdapter(news , getActivity().getBaseContext());
                    recyclerView.setAdapter(newsAdapter);
                }

            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {

                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity().getBaseContext(),"Oops! Something went wrong!",Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
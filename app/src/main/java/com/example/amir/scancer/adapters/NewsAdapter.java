package com.example.amir.scancer.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.widget.ImageViewCompat;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.amir.scancer.MainActivity;
import com.example.amir.scancer.R;
import com.example.amir.scancer.models.News;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private ArrayList<News> news=new ArrayList<>();
    private Context context;

    public NewsAdapter(ArrayList<News> news,  Context context){
        this.context = context;
        this.news = news;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull NewsAdapter.ViewHolder holder, int position) {

        final String newsTitle = news.get(position).getTitle();
        final String newsImageUrl = news.get(position).getUrlToImage();
        final String newsUrl = news.get(position).getUrl();

        holder.title.setText(newsTitle);
        Picasso.get().load(newsImageUrl).into(holder.imageUrl);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(newsUrl));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return news.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView imageUrl;
        private CardView cardView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.news_title);
            imageUrl = (ImageView) itemView.findViewById(R.id.news_image);
            cardView = (CardView) itemView.findViewById(R.id.cardview_news_row);
        }
    }
}

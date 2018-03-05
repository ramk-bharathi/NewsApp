package me.ramk.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.ramk.newsapp.handlers.VolleyHandler;
import me.ramk.newsapp.helpers.VolleyHelper;
import me.ramk.newsapp.models.News;

public class MainActivity extends AppCompatActivity {
//    private ListView listView;
    public List<News> newsArticlesList;
//    public CustomListAdaptor customListAdaptor;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsArticlesList = new ArrayList<>();
        getNewsByChannel();
//        listView = (ListView) findViewById(R.id.base_list);
//        customListAdaptor = new CustomListAdaptor();
//        listView.setAdapter(customListAdaptor);
        recyclerView = (RecyclerView) findViewById(R.id.base_list);
        recyclerView.setHasFixedSize(true);
        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
    }

    public void getNewsByChannel() {
        try {
            RequestQueue requestQueue = VolleyHelper.getInstance(this.getApplicationContext()).getRequestQueue();

            Map<String, String> requestParams = new HashMap<>();
            requestParams.put("channel_id", String.valueOf(134));
            requestParams.put("iso_language_code", "kn");
            requestParams.put("limit", String.valueOf(10));
            requestParams.put("category", "News");

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Accept", "application/json");

            VolleyHandler<News[]> volleyHandler = new VolleyHandler<News[]>(Request.Method.POST, "http://medianews.service.wowstream.in/news/article/get/subcategory", News[].class, requestParams, requestHeaders, new Response.Listener<News[]>() {
                @Override
                public void onResponse(News[] response) {
                    newsArticlesList = Arrays.asList(response);
//                    customListAdaptor.notifyDataSetChanged();
                    rvAdapter = new NewsRecyclerAdapter(newsArticlesList);
                    recyclerView.setAdapter(rvAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(MainActivity.class.getName(), "onErrorResponse: ", error);
                }
            });
            volleyHandler.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyHelper.getInstance(this).addToRequestQueue(volleyHandler);
        } catch (Exception e) {
            Log.e(MainActivity.class.getName(), "method error: ", e);
        }
    }

    public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.ViewHolder> {
        List<News> newsArticles;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView newsHeadline;
            public ImageView newsThumbnail;
            public View layout;
            public ViewHolder(View v) {
                super(v);
                layout = v;
                newsHeadline = (TextView) v.findViewById(R.id.news_headline);
                newsThumbnail = (ImageView) v.findViewById(R.id.news_thumbnail);
            }
        }

        public NewsRecyclerAdapter(List<News> newsArticles) {
            this.newsArticles = newsArticles;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.news_list, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final News newsArticle = newsArticles.get(position);
            holder.newsHeadline.setText(newsArticle.getHeadline());
            Picasso.with(getApplicationContext()).load(newsArticle.getPhoto_url()).into(holder.newsThumbnail);
            holder.newsHeadline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, newsArticle.getShort_description(), Toast.LENGTH_LONG).show();;
                }
            });
        }

        @Override
        public int getItemCount() {
            return newsArticles.size();
        }
    }

//    public class CustomListAdaptor extends BaseAdapter {
//
//        @Override
//        public int getCount() {
//            return newsArticlesList.size();
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return null;
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return 0;
//        }
//
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            try {
//                convertView = getLayoutInflater().inflate(R.layout.news_list, null);
//                ImageView newsThumbnail = (ImageView) convertView.findViewById(R.id.news_thumbnail);
//                TextView newsHeadline = (TextView) convertView.findViewById(R.id.news_headline);
//                //TextView newsBrief = (TextView) convertView.findViewById(R.id.news_brief);
//
//                News article = newsArticlesList.get(position);
//                Picasso.with(getApplicationContext()).load(article.getPhoto_url()).into(newsThumbnail);
//                newsHeadline.setText(article.getHeadline());
//                //newsBrief.setText(article.getShort_description());
//            } catch (Exception e) {
//                Log.e(MainActivity.class.getName(), "custom adaptor error: ", e);
//            }
//
//            return convertView;
//        }
//    }
}

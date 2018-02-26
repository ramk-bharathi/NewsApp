package me.ramk.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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
    private ListView listView;
    public List<News> newsArticlesList;
    public CustomListAdaptor customListAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsArticlesList = new ArrayList<>();
        getNewsByChannel();
        listView = (ListView) findViewById(R.id.base_list);
        customListAdaptor = new CustomListAdaptor();
        listView.setAdapter(customListAdaptor);
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
                    customListAdaptor.notifyDataSetChanged();
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

    public class CustomListAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return newsArticlesList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            try {
                convertView = getLayoutInflater().inflate(R.layout.news_list, null);
                ImageView newsThumbnail = (ImageView) convertView.findViewById(R.id.news_thumbnail);
                TextView newsHeadline = (TextView) convertView.findViewById(R.id.news_headline);
                TextView newsBrief = (TextView) convertView.findViewById(R.id.news_brief);

                News article = newsArticlesList.get(position);
                Picasso.with(getApplicationContext()).load(article.getPhoto_url()).into(newsThumbnail);
                newsHeadline.setText(article.getHeadline());
                newsBrief.setText(article.getShort_description());
            } catch (Exception e) {
                Log.e(MainActivity.class.getName(), "custom adaptor error: ", e);
            }

            return convertView;
        }
    }
}

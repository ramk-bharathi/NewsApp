package me.ramk.newsapp;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import me.ramk.newsapp.handlers.VolleyHandler;
import me.ramk.newsapp.helpers.VolleyHelper;
import me.ramk.newsapp.models.News;

public class ArticleActivity extends AppCompatActivity {
    private TextView articleHeadline;
    private TextView articleShortDescription;
    private TextView articleContent;
    private ImageView articleThumbnail;
    private int articleId;
    private News article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        article = new News();
        Intent intent = getIntent();
        articleId = intent.getIntExtra("articleId", 0);
        articleHeadline = (TextView) findViewById(R.id.article_headline);
        articleShortDescription = (TextView) findViewById(R.id.article_short_description);
        articleThumbnail = (ImageView) findViewById(R.id.article_cover_img);
        articleContent = (TextView) findViewById(R.id.article_content);
        getArticle();
    }

    public void getArticle() {
        try{
            RequestQueue requestQueue = VolleyHelper.getInstance(this.getApplicationContext()).getRequestQueue();

            Map<String, String> requestParam = new HashMap<>();
            requestParam.put("news_id", String.valueOf(articleId));

            Map<String, String> requestHeaders = new HashMap<>();
            requestHeaders.put("Accept", "application/json");

            VolleyHandler<News> volleyHandler = new VolleyHandler<News>(Request.Method.POST, "http://medianews.service.wowstream.in/news/article/get/id", News.class, requestParam, requestHeaders, new Response.Listener<News>() {
                @Override
                public void onResponse(News response) {
                    article = response;
                    Picasso.with(getApplicationContext()).load(article.getPhoto_url()).into(articleThumbnail);
                    articleHeadline.setText(article.getHeadline());
                    articleShortDescription.setText(article.getShort_description());
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        articleContent.setText(Html.fromHtml(article.getContent(),Html.FROM_HTML_MODE_COMPACT));
                    } else if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        articleContent.setText(Html.fromHtml(article.getContent()));
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(ArticleActivity.class.getName(), "getArticle-Volley: ", error);
                }
            });
            volleyHandler.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleyHelper.getInstance(this).addToRequestQueue(volleyHandler);
        } catch (Exception e) {
            Log.e(ArticleActivity.class.getName(), "getArticle: ", e);
        }
    }


}

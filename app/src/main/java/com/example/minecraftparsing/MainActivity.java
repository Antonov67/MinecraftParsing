package com.example.minecraftparsing;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    final String MINECRAFT_SHOP_URL = "https://shop.minecraft.net/collections/all";
    final String MINECRAFT_LOGO = "https://cdn.shopify.com/s/files/1/0266/4841/2351/files/minecraft-logo-presented-white.png?v=1630007272";

    TextView textTitle;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> items;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTitle = findViewById(R.id.textTitle);
        listView = findViewById(R.id.list);
        webView = findViewById(R.id.webView);

        webView.loadUrl(MINECRAFT_LOGO);

        webView.setInitialScale(70);



        items = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);


        ParsingThread parsingThread = new ParsingThread();
        parsingThread.execute();
    }

    //внутренний класс потока, в котором происходит парсинг
    class ParsingThread extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {

            Document document = null;

            try {
                document = Jsoup.connect(MINECRAFT_SHOP_URL).get();

                Elements elements = document.getElementsByClass("grid-product__meta");
                for (Element element : elements){
                    items.add(element.child(0).text() + " " + element.child(1).text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
            return document.title();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textTitle.setText(s);
            adapter.notifyDataSetChanged();
        }
    }

}
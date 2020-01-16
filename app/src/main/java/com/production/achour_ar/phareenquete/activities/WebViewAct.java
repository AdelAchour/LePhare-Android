package com.production.achour_ar.phareenquete.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.production.achour_ar.phareenquete.R;
import com.production.achour_ar.phareenquete.managers.Constants;

import static com.production.achour_ar.phareenquete.managers.FirebaseManager.getValueString;

public class WebViewAct extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    private String URL_WEB;
    private ImageView backButton, refreshButton, chromeButton;
    private SwipeRefreshLayout swipeLayout;
    private FirebaseFirestore db;
    private final String TAG = "WebViewAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_act);

        initView();
        setListener();
        getLink();

    }

    private void setListener() {
        backButton.setOnClickListener(this);
        refreshButton.setOnClickListener(this);
        chromeButton.setOnClickListener(this);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLink();
            }
        });
    }

    private void getArguments() {
        URL_WEB = getIntent().getStringExtra(Constants.URL_ENQUETE);
    }

    private void populateWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
        });
        webView.loadUrl(URL_WEB);
        swipeLayout.setRefreshing(false);
    }

    private void initView() {
        db = FirebaseFirestore.getInstance();

        chromeButton = findViewById(R.id.chromeButton);
        webView = findViewById(R.id.webView);
        backButton = findViewById(R.id.backButton);
        refreshButton = findViewById(R.id.refreshButton);
        swipeLayout = findViewById(R.id.swipeLayout);
    }

    private void getLink() {
        final DocumentReference docRef = db.collection("links").document("thelink");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    URL_WEB = getValueString(snapshot.get("url"));
                    populateWebView();

                } else {
                    Log.d(TAG, "Current data: null");
                    Snackbar.make(findViewById(android.R.id.content),
                            "Erreur. Impossible d'ouvrir le site.", Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.backButton:
                finish();
                break;

            case R.id.refreshButton:
                swipeLayout.setRefreshing(true);
                getLink();
                break;

            case R.id.chromeButton:
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_WEB));
                    startActivity(browserIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(findViewById(android.R.id.content),
                            "Une erreur s'est produite.", Snackbar.LENGTH_LONG).show();
                }
                break;
        }
    }
}

package com.production.achour_ar.phareenquete.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.production.achour_ar.phareenquete.R;
import com.production.achour_ar.phareenquete.dialogs.DialogAlertLogout;
import com.production.achour_ar.phareenquete.managers.Constants;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import static com.production.achour_ar.phareenquete.managers.FirebaseManager.getValueString;

public class HomeAct extends AppCompatActivity implements View.OnClickListener {

    private CardView enqueteCard;
    private ArrayList<String> picsBG;
    private ImageView picBG;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private final String TAG = "HomeAct";
    private ProgressBar progressBar;
    private String URL_SITE;
    private ImageView refreshButton;
    private boolean linkUp = false;
    private TextView linkTV;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private ImageView menuBtn;
    public static Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_act);

        initView();
        setListener();
        setImageBGRandomly();
        getCurrentUser();
        navigationListener();

    }

    private void getCurrentUser() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!= null){
            Log.d(TAG, "getCurrentUser: SIGNED IN - "+currentUser.getUid());
            getLink();
        }
        else {
            Snackbar.make(findViewById(android.R.id.content),
                    "Une erreur s'est produite. Déconnectez-vous et réessayez.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void getLink() {
        final DocumentReference docRef = db.collection("links").document("thelink");
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if (snapshot != null && snapshot.exists()) {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Current data: " + snapshot.getData());
                    URL_SITE = getValueString(snapshot.get("url"));
                    linkUp = true;
                    linkTV.setVisibility(View.VISIBLE);
                    linkTV.setText("Lien de l'enquête :\n"+ URL_SITE);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Lien de l'enquête récupéré avec succès.", Snackbar.LENGTH_SHORT).show();

                } else {
                    progressBar.setVisibility(View.GONE);
                    Log.d(TAG, "Current data: null");
                    linkUp = false;
                    linkTV.setVisibility(View.GONE);
                    Snackbar.make(findViewById(android.R.id.content),
                            "Impossible de récupérer le lien de l'enquête. Appuyez sur rafraichir.", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        menuBtn = findViewById(R.id.menuBtn);
        linkTV = findViewById(R.id.linkTV);
        refreshButton = findViewById(R.id.refreshButton);
        progressBar = findViewById(R.id.progressBar);
        enqueteCard = findViewById(R.id.enqueteCard);
        picBG = findViewById(R.id.picBGHome);

        handler = new HandlerHome();

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        mDrawerLayout = findViewById(R.id.drawer_layout);
    }

    private void setListener() {
        refreshButton.setOnClickListener(this);
        enqueteCard.setOnClickListener(this);
        menuBtn.setOnClickListener(this);
    }

    private void openWebSite() {
        if (linkUp){
            startActivity(new Intent(HomeAct.this, WebViewAct.class));
        }
        else {
            Snackbar.make(findViewById(android.R.id.content),
                    "Le lien n'a pas été récupéré. Appuyez sur rafraichir.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void setImageBGRandomly() {
        picsBG = new ArrayList<>();
        picsBG.add("home01");
        picsBG.add("home02");
        picsBG.add("home03");
        picsBG.add("home04");

        int picId = getResources().getIdentifier(picsBG.get(new Random().nextInt(4)), "drawable", getPackageName());

        if (picId != 0) {
            picBG.setImageResource(picId);
        }
    }

    private void navigationListener() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.nav_logout:
                        DialogAlertLogout dialog = new DialogAlertLogout();
                        dialog.showDialog(HomeAct.this);
                        break;

                    case R.id.nav_about_app:
                        startActivity(new Intent(HomeAct.this, AboutApp.class));
                        break;
                }

                mDrawerLayout.closeDrawers();
                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here
                return true;
            }
        });
    }

    private void logout() {
        mAuth.signOut();

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged: Signed In:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG,"onAuthStateChanged: Signed Out");
                    startActivity(new Intent(HomeAct.this, LoginAct.class));
                    finish();
                }
            }
        });
    }

    private class HandlerHome extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.LOGOUT:
                    logout();
                    break;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setImageBGRandomly();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.enqueteCard:
                openWebSite();
                break;

            case R.id.refreshButton:
                progressBar.setVisibility(View.VISIBLE);
                linkTV.setVisibility(View.GONE);
                getCurrentUser();
                break;

            case R.id.menuBtn:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
                    /* drawer is open */
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                else{
                    /* drawer is closed */
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                break;

        }
    }
}

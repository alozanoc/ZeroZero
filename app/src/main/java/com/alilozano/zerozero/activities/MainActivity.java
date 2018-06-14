package com.alilozano.zerozero.activities;


import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alilozano.zerozero.R;
import com.alilozano.zerozero.ZeroSharedPreferences;
import com.alilozano.zerozero.fragments.CreateTweetFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RC_SIGN_IN_AUTHUI = 1;
    private ZeroSharedPreferences pref;
    private FirebaseDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        pref = new ZeroSharedPreferences(this);
        setContentView(R.layout.activity_main);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        btnCerrarSesion.setOnClickListener(this);
        Button btnPrueba = findViewById(R.id.btnPrueba);
        btnPrueba.setOnClickListener(this);

        ListView listView = findViewById(R.id.listViewTimeline);
        DatabaseReference timelineRef = db.getReference("timeline");
        /*FirebaseListOptions<Map> options = new FirebaseListOptions.Builder<Map>().setLayout(R.layout.tweet_layout)
                .setQuery(timelineRef, Map.class)
                .build();

        ListAdapter adapter = new FirebaseListAdapter<Map>(options) {
            protected void populateView(View view, Map timeline, int v) {
                ((TextView) view.findViewById(R.id.txtTweet)).setText(String.valueOf(timeline.get("content")));
            }
        };
        listView.setAdapter(adapter);
        */

    }

    @Override
    protected void onResume() {
        super.onResume();
        String uid = pref.getString(pref.KEY_UID_LOGUED_USER);
        if(uid == null){
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.PhoneBuilder().build());

            // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN_AUTHUI);
        } else {
            Toast.makeText(this, "Usuario logueado: " + uid, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCerrarSesion:
                cerrarSesion();
                break;
            case R.id.btnPrueba:
                prueba();
                break;
        }

    }

    public void prueba(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        DialogFragment dialogFragment = new CreateTweetFragment();
        dialogFragment.show(ft, "dialog");
    }
    public void cerrarSesion(){
        pref.setString(pref.KEY_UID_LOGUED_USER, null);
        onResume();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RC_SIGN_IN_AUTHUI:
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    pref.setString(pref.KEY_UID_LOGUED_USER, user.getUid());
                    DatabaseReference usersRef = db.getReference("users");
                    DatabaseReference loguedUserRef = usersRef.child(user.getUid());
                    loguedUserRef.child("name").setValue(user.getDisplayName());
                    loguedUserRef.child("email").setValue(user.getEmail());
                    loguedUserRef.child("photo_url").setValue(user.getPhotoUrl());

                    Toast.makeText(this, user.getDisplayName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "No se logueo", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}

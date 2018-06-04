package com.alilozano.zerozero.fragments;


import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alilozano.zerozero.R;
import com.alilozano.zerozero.ZeroSharedPreferences;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class CreateTweetFragment extends DialogFragment {
    private FirebaseDatabase db;
    private ZeroSharedPreferences pref;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance();
        pref = new ZeroSharedPreferences(this.getActivity());

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_create_tweet, container,
                false);
        final TextView txtTweet = v.findViewById(R.id.txtTweet);
        v.findViewById(R.id.btnGuardar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = pref.getString(pref.KEY_UID_LOGUED_USER);
                DatabaseReference timelineRef = db.getReference("timeline");
                DatabaseReference userTimelineRef = timelineRef.child(uid);
                DatabaseReference tweet = userTimelineRef.push();

                tweet.child("content").setValue(txtTweet.getText().toString());
                CreateTweetFragment.this.dismiss();
            }
        });
        return v;
    }

}

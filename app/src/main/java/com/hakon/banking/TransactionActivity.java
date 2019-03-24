package com.hakon.banking;

import android.content.Intent;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.media.VolumeShaper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {
    private static final String TAG = "TransactionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        Intent i = getIntent();

        ArrayList<Transaction>transactions = i.getExtras().getParcelableArrayList("transactions");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setAdapter(new RecyclerViewAdapter(transactions, this));
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        } else { // Two columns in landscape mode
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        }
    }
}

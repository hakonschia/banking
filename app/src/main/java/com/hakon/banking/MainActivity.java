package com.hakon.banking;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    // UI elements
    private TextView mLblBalance;
    private Button mBtnTransactions;
    private Button mBtnTransfer;
    private View mMainView;

    // Java types
    private Integer mBalance;
    private Integer mBalanceCent;
    private ArrayList<Transaction> mTransactions;

    // Private static constants
    private static final int REQUEST_TRANSFER = 1;
    private static final String TAG = "MainActivity";

    // Public static constants
    public static final String BALANCE_FORMAT = "%d.%02d";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
        final SharedPreferences userSettings = this.getSharedPreferences("user_settings", 0);
        if (userSettings.getString("theme", "NIGHT_MODE").equals("NIGHT_MODE")) {
            setTheme(R.style.DarkMode);
            Log.d(TAG, "onCreate: DARKMODE");
        } else {
            Log.d(TAG, "onCreate: LIGHTMODE");
            setTheme(R.style.LightMode);
        }
        */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.initViews();

        if (savedInstanceState == null) { // No state to be restored, create new balance
            mTransactions = new ArrayList<>();

            mBalance = new Random().nextInt(21) + 90; // Generate the whole part (90-110)
            mBalanceCent = new Random().nextInt(100);

            if (mBalance == 110) { // Remove the cents if the whole part is 110
                mBalanceCent = 0;
            }

            mTransactions.add(
                    new Transaction(mBalance, mBalanceCent, mBalance, mBalanceCent, "Arne Rofinn")
            );
        } else {
            mBalance = savedInstanceState.getInt("mBalance");
            mBalanceCent = savedInstanceState.getInt("mBalanceCent");
            mTransactions = savedInstanceState.getParcelableArrayList("mTransactions");
        }

        this.setBalanceText();

        // TODO: Create a cool thing to add more friends
        final String[] friends = new String[]{"Per", "Pål", "Espen Askeladd"};


        /* --------- Event listeners --------- */
        mBtnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TransferActivity.class);

                i.putExtra("balance", mBalance);
                i.putExtra("balanceCent", mBalanceCent);
                i.putExtra("friends", friends);

                startActivityForResult(i, REQUEST_TRANSFER);
            }
        });

        mBtnTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TransactionActivity.class);

                i.putExtra("transactions", mTransactions);

                startActivity(i);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TRANSFER) { // Returned from TransferActivity
            if (resultCode == RESULT_OK) {
                Integer amnt = data.getIntExtra("balance", 0);
                Integer amntCent = data.getIntExtra("balanceCent", 0);
                String sentTo = data.getStringExtra("sentTo");

                mBalanceCent -= amntCent;
                mBalance -= amnt;

                if (mBalanceCent < 0) {
                    mBalance--;
                    mBalanceCent += 100;
                }

                this.mTransactions.add(
                        new Transaction(amnt, amntCent, mBalance, mBalanceCent, sentTo)
                );

                this.setBalanceText();

                Snackbar sb = Snackbar.make(mMainView, "Transaction approved.", Snackbar.LENGTH_LONG);
                View sbView = sb.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.darkDarkGray));
                sb.show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("mBalance", mBalance);
        outState.putInt("mBalanceCent", mBalanceCent);
        outState.putParcelableArrayList("transactions", mTransactions);
    }

    /**
     * Sets the lbl_balance text view to what the current mBalance is
     */
    private void setBalanceText() {
        this.mLblBalance.setText(String.format(Locale.getDefault(),
                MainActivity.BALANCE_FORMAT, mBalance, mBalanceCent));
    }

    /**
     * Initializes all UI elements
     */
    private void initViews() {
        mBtnTransactions = findViewById(R.id.btn_transactions);
        mBtnTransfer = findViewById(R.id.btn_transfer);
        mLblBalance = findViewById(R.id.lbl_balance);
        mMainView = findViewById(R.id.main_view);
    }
}
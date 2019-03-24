package com.hakon.banking;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Locale;

public class TransferActivity extends AppCompatActivity {
    // UI elements
    private Button mBtnPay;
    private Spinner mSpnrNames;
    private TextView mLblAmountCheck;
    private TextView mLblBalanceAvailable;
    private EditText mTxtAmount;

    private static final String TAG = "TransferActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        this.initViews();

        mBtnPay.setEnabled(false);

        final Intent i = getIntent();
        final int balance = i.getExtras().getInt("balance");
        final int balanceCent = i.getExtras().getInt("balanceCent");
        final double totalBalance = Double.parseDouble(balance + "." + balanceCent);

        final String[] friends = i.getExtras().getStringArray("friends");
        ArrayAdapter<String> spnrNamesAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                friends
        );
        mSpnrNames.setAdapter(spnrNamesAdapter);

        mLblBalanceAvailable.setText(String.format(Locale.getDefault(),
                "Balance available: €" + MainActivity.BALANCE_FORMAT, balance, balanceCent));

        mTxtAmount.setHint(String.format(Locale.getDefault(),
                MainActivity.BALANCE_FORMAT, balance, balanceCent));

        mTxtAmount.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence src, int start, int end,
                                               Spanned spanned, int start2, int end2) {

                        /*
                        not really true so stay tuned my guys

                           start = start of the text
                           end = end of the text BEFORE what was input

                           spanned = old text

                           start2 = start of the text AFTER what was input
                           end2 = end of the text
                        */


                        String before = spanned.subSequence(0, start2).toString(); // What is before the new input
                        String after = spanned.subSequence(end2, spanned.length()).toString(); // What is after the new input
                        String changed = src.subSequence(start, end).toString(); // What was newly input

                        String newText = before + changed + after;

                        // 0 or many digits, preceded by an optional ".xx"
                        if (!newText.matches("[0-9]*(\\.[0-9]{0,2})?")) {
                            return "";
                        }
                        return null;

                        /* filter works by returning what should replace the input, so returning
                        an empty string means to discard the input, while null means to accept it */
                    }
                }
        });


        /* --------- Event listeners --------- */
        // Change the pay button based on the input in txt_amount
        mTxtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                String errorMessage = "";
                double inputValue; // The number entered

                mBtnPay.setEnabled(false); // Disabled by default
                mTxtAmount.setTextColor(Color.RED);

                try {
                    inputValue = Double.parseDouble(text);

                    if (inputValue > totalBalance) {
                        errorMessage = "Your balance is too low.";
                    } else if (inputValue == 0) {
                        errorMessage = "Please enter a number.";
                    } else if (inputValue < 0) { // just in case someone manages to enter a negative number xDDD
                        errorMessage = "how did u even manage that that shouldnt be possible";
                    } else { // Input valid
                        mTxtAmount.setTextColor(Color.WHITE);
                        mBtnPay.setEnabled(true);
                    }
                } catch (NumberFormatException e) {
                    Log.d(TAG, "onTextChanged: Error parsing input");
                    e.printStackTrace();
                }

                mLblAmountCheck.setText(errorMessage);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { // The amount is only valid if the button is enabled, no need for error checking here
                String amount = mTxtAmount.getText().toString();
                String[] parts = amount.split("\\."); // Split the number based on the decimal place
                int amnt = 0;
                int amntCent = 0;

                try { // ".x" causes a NumberFormatException, because splitting ".x" gives an array of "" and "x"
                    amnt = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    Log.d(TAG, "onClick: Error parsing whole number");
                    e.printStackTrace();
                }

                try { // If the user typed "x", not "x.x" an exception will be thrown
                    amntCent = Integer.parseInt(parts[1]);
                    if (parts[1].length() == 1) { // One digit entered, multiply with 10 (.2 = 20 cents)
                        amntCent *= 10;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.d(TAG, "onClick: Error parsing decimal");
                    e.printStackTrace();
                }

                i.putExtra("balance", amnt);
                i.putExtra("balanceCent", amntCent);
                i.putExtra("sentTo", mSpnrNames.getSelectedItem().toString());

                setResult(RESULT_OK, i);
                finish();
            }
        });

    }

    /**
     * Initializes all UI elements
     */
    private void initViews() {
        mBtnPay = findViewById(R.id.btn_pay);
        mSpnrNames = findViewById(R.id.spnr_names);
        mLblAmountCheck = findViewById(R.id.lbl_amount_check);
        mLblBalanceAvailable = findViewById(R.id.lbl_balanceAvailable);
        mTxtAmount = findViewById(R.id.txt_amount);
    }
}
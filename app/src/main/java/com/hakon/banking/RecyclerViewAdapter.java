package com.hakon.banking;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static android.content.Context.VIBRATOR_SERVICE;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Transaction> mTransactions;
    private Context mContext;


    public RecyclerViewAdapter(ArrayList<Transaction> transactions, Context context) {
        mTransactions = transactions;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.transaction_listitem,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Transaction t = mTransactions.get(position);

        holder.tvSentTo.setText(t.getSentTo());
        holder.tvAmount.setText(String.format(Locale.getDefault(),
                MainActivity.BALANCE_FORMAT, t.getAmnt(), t.getAmntCent()));
        holder.tvBalanceAfter.setText(String.format(Locale.getDefault(),
                MainActivity.BALANCE_FORMAT, t.getAmntAfter(), t.getAmntAfterCent()));

        holder.tvTransactionDate.setText(t.getTime());

        final TypedArray images = mContext.getResources().obtainTypedArray(R.array.images);

        // TODO: Make nice profile picture thats actually to the person
        holder.imgProfilePicture.setImageResource(images.getResourceId(
                new Random().nextInt(images.length()),
                0)
        );

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Vibrator archVibe = (Vibrator) mContext.getSystemService(VIBRATOR_SERVICE);
                archVibe.vibrate(VibrationEffect.createOneShot(
                        500,
                        VibrationEffect.DEFAULT_AMPLITUDE
                        )
                );

                Toast.makeText(
                        mContext,
                        String.format(Locale.getDefault(), "%s " + MainActivity.BALANCE_FORMAT,
                                t.getSentTo(), t.getAmnt(), t.getAmntCent()),
                        Toast.LENGTH_LONG
                ).show();

                return true; // idk what this means tbh
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }


    /**
     * ViewHolder is responsible for displaying the views of the items in the list
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout parentLayout;
        private TextView tvTransactionDate;
        private TextView tvSentTo;
        private TextView tvAmount;
        private TextView tvBalanceAfter;
        private ImageView imgProfilePicture;

        public ViewHolder(View itemView) {
            super(itemView);

            this.initViews();
        }

        /**
         * Initializes all UI elements
         */
        private void initViews() {
            this.parentLayout = itemView.findViewById(R.id.parent_layout);
            this.tvTransactionDate = itemView.findViewById(R.id.tv_transactiondate);
            this.tvSentTo = itemView.findViewById(R.id.tv_sentTo);
            this.tvAmount = itemView.findViewById(R.id.tv_amount);
            this.tvBalanceAfter = itemView.findViewById(R.id.tv_balanceAfter);
            this.imgProfilePicture = itemView.findViewById(R.id.img_profilePicture);
        }
    }
}

package com.hakon.banking;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Transaction implements Parcelable {
    // Java types
    //private LocalDateTime date;
    private String time;
    private Integer amnt;
    private Integer amntCent;
    private Integer amntAfter;
    private Integer amntAfterCent;
    private String sentTo;

    // Constants
    private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm:ss");


    /**
     * Default constructor
     * Date is set automatically
     */
    public Transaction() {
       // this.date = LocalDateTime.now();
        this.time = LocalDateTime.now().format(dateFormat);
        this.amnt = 0;
        this.amntCent = 0;
        this.amntAfter = 0;
        this.amntAfterCent = 0;
        this.sentTo = "";
    }

    /**
     * Date is set automatically
     * @param amnt          the whole amount of the transaction
     * @param amntCent      the cent amount of the transaction
     * @param amntAfter     the whole balance after the transaction
     * @param amntAfterCent the cent amount after the transaction
     * @param sentTo        who the transaction was to
     */
    public Transaction(Integer amnt, Integer amntCent,
                       Integer amntAfter, Integer amntAfterCent, String sentTo) {

        //this.date = LocalDateTime.now();
        this.time = LocalDateTime.now().format(dateFormat);
        this.amnt = amnt;
        this.amntCent = amntCent;
        this.amntAfter = amntAfter;
        this.amntAfterCent = amntAfterCent;
        this.sentTo = sentTo;
    }

    /**
     * @return the time the transaction was done
     */
    public String getTime() {
        return time;
    }

    /**
     * @return who the transaction was sent to
     */
    public String getSentTo() {
        return sentTo;
    }

    /**
     * @return the whole amount of the transaction
     */
    public Integer getAmnt() {
        return amnt;
    }

    /**
     * @return the cent amount of the transaction
     */
    public Integer getAmntCent() {
        return amntCent;
    }

    /**
     * @return the whole balance amount after the transaction
     */
    public Integer getAmntAfter() {
        return amntAfter;
    }

    /**
     * @return the cent balance after the transaction
     */
    public Integer getAmntAfterCent() {
        return amntAfterCent;
    }

    /**
     * Sets an object from a parcel
     * The order the values are set in this constructor MUST match the order of writeToParcel()
     * @param in the parcel to read data from
     */
    protected Transaction(Parcel in) {
        if (in.readByte() == 0) {
            amnt = null;
        } else {
            amnt = in.readInt();
        }
        if (in.readByte() == 0) {
            amntCent = null;
        } else {
            amntCent = in.readInt();
        }
        if (in.readByte() == 0) {
            amntAfter = null;
        } else {
            amntAfter = in.readInt();
        }
        if (in.readByte() == 0) {
            amntAfterCent = null;
        } else {
            amntAfterCent = in.readInt();
        }
        sentTo = in.readString();
        time = in.readString();
    }

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Writes the object to a parcel
     * The order here MUST match the order in the constructor Transaction(Parcel)
     * @param parcel the parcel to write to
     * @param i something
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        intWriteToParcelHelper(parcel, amnt);
        intWriteToParcelHelper(parcel, amntCent);
        intWriteToParcelHelper(parcel, amntAfter);
        intWriteToParcelHelper(parcel, amntAfterCent);

        parcel.writeString(sentTo);
        parcel.writeString(time);
    }

    /**
     * Helper function to write a value to a parcel
     * @param parcel the parcel to write to
     * @param value the value to write
     */
    private void intWriteToParcelHelper(Parcel parcel, Integer value) {
        if(value == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(value);
        }
    }
}

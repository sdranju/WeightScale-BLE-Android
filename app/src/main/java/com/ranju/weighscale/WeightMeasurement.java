package com.ranju.weighscale;

import com.welie.blessed.BluetoothBytesParser;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT16;
import static com.welie.blessed.BluetoothBytesParser.FORMAT_UINT8;

import android.util.Log;

import androidx.annotation.NonNull;

public class WeightMeasurement implements Serializable {
    private static final String TAG = "WeightMeasurement";

    public final double weight;
    public final WeightUnit unit;
    public final Date timestamp;
    public Integer userID;
    public Integer BMI;
    public Integer height;

    public WeightMeasurement(byte[] byteArray) {
        BluetoothBytesParser parser = new BluetoothBytesParser(byteArray);

        // Parse flag byte
        final int flags = parser.getUInt8();
        unit = ((flags & 0x01) > 0) ? WeightUnit.Pounds : WeightUnit.Kilograms;
        //Log.i(TAG, "WeightMeasurement - unit: " + unit);
        final boolean timestampPresent = (flags & 0x02) > 0;
        final boolean userIDPresent = (flags & 0x04) > 0;
        final boolean bmiAndHeightPresent = (flags & 0x08) > 0;

        // Get and parse weight value
        double weightMultiplier = (unit == WeightUnit.Kilograms) ? 0.005 : 0.01;
        weight = parser.getUInt16() * weightMultiplier;
        //Log.i(TAG, "WeightMeasurement - weight: " + weight);

        // Get timestamp if present
        if (timestampPresent) {
            timestamp = parser.getDateTime();
        } else {
            timestamp = Calendar.getInstance().getTime();
        }

        // Get user ID if present
        if (userIDPresent) {
            userID = parser.getUInt8();
        }

        // Get BMI and Height if present
        if (bmiAndHeightPresent) {
            BMI = parser.getUInt16();
            height = parser.getUInt16();
        }

    }

    @NonNull
    @Override
    public String toString() {
        // format our data
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String formattedTimestamp = timestamp != null ? df.format(timestamp) : "null";
        return String.format("%.2f %s, user %d, BMI %d, height %d at (%s)", weight,
                unit == WeightUnit.Kilograms ? "kg" : "lb", userID, BMI, height, formattedTimestamp);
    }

}

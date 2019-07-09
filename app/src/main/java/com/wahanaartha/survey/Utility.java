package com.wahanaartha.survey;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static com.wahanaartha.survey.admin.AdminAddSurveyQuestionActivity.QuestionType;

public class Utility {

    public static SimpleDateFormat getFormat() {
        return new SimpleDateFormat("yyyy-MMMM-dd");
    }

    private static QuestionType[] questionTypes = {
            QuestionType.MultipleChoice,
            QuestionType.CheckList,
            QuestionType.SingleTextBox};

    private static String[] answersString = {
            "Belum",
            "Sudah",
            "Akan"
    };

    public static ArrayList<String> getMonths() {
        ArrayList<String> months = new ArrayList<>();

        months.add("Januari");
        months.add("Februari");
        months.add("Maret");
        months.add("April");
        months.add("Mei");
        months.add("Juni");
        months.add("Juli");
        months.add("Agustus");
        months.add("September");
        months.add("Oktober");
        months.add("November");
        months.add("Desember");

        return months;
    }

    public static String bitmapToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap base64ToBitmap(String string) {
        byte[] bytes = Base64.decode(string, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public static byte[] base64ToBytes(String string) {
        return Base64.decode(string, Base64.DEFAULT);
    }

    public static Bitmap scaleBitmap(Bitmap bm, int maxWidth, int maxHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();


        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            float ratio = (float) height / maxHeight;

            height = (int)(height / ratio);
            width = (int)(width / ratio);
        }


        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }

    public static String getDPI(double density) {
        if (density == 0.75) {
            return "ldpi";
        }else if (density == 1.0) {
            return "mdpi";
        }else if (density == 1.5) {
            return "hdpi";
        }else if (density == 2.0) {
            return "xhdpi";
        }else if (density == 3.0) {
            return "xxhdpi";
        }else {
            return "xxxhdpi";
        }
    }
}

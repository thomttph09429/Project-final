package com.app.projectfinal.utils;


import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.projectfinal.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;

public class ValidateForm {
    /**
     * check if it's email format
     * <pre>
     *     author:ThomTT
     *     date: 21/07/2022
     * </pre>
     *
     * @param email
     * @return
     */
    public static boolean isEmail(String email) {
        String checkEmail = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (email.matches(checkEmail)) {
            return true;
        }
        return false;
    }

    /**
     * validate for pass word, greater than 8 characters
     * <pre>
     *     author: ThomTT
     *     date 21/07/2022
     * </pre>
     *
     * @param pass
     * @return
     */
    public static boolean validatePassword(String pass) {

        String patterns = "^(?=.*[A-Z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        if (pass.matches(patterns) && pass.length()>=8 && pass.length()<=16){
                return true;
        }
        return false;
    }

    /**
     * check re-enter pass word
     * <pre>
     *     author: ThomTT
     *     date 21/07/2022
     * </pre>
     *
     * @param pass
     * @param rePass
     * @return
     */
    public static boolean checkRePassWord(String pass, String rePass) {
        if (pass.equals(rePass)) {
            return true;
        }
        return false;
    }

    /**
     * check if it's phone number format
     * <pre>
     *     author: ThomTT
     *     date 21/07/2022
     * </pre>
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        String checkPhoneNumber = "^[0-9]{10}$";
        if (phoneNumber.matches(checkPhoneNumber)) {
            return true;
        }
        return false;
    }

    /**
     * convert to currency
     *
     * @param value
     * @return
     */
    public static String getDecimalFormattedString(String value) {
        StringTokenizer lst = new StringTokenizer(value, ".");
        String str1 = value;
        String str2 = "";
        if (lst.countTokens() > 1) {
            str1 = lst.nextToken();
            str2 = lst.nextToken();
        }
        String str3 = "";
        int i = 0;
        int j = -1 + str1.length();
        if (str1.charAt(-1 + str1.length()) == '.') {
            j--;
            str3 = ".";
        }
        for (int k = j; ; k--) {
            if (k < 0) {
                if (str2.length() > 0)
                    str3 = str3 + "." + str2;
                return str3;
            }
            if (i == 3) {
                str3 = "," + str3;
                i = 0;
            }
            str3 = str1.charAt(k) + str3;
            i++;
        }

    }

    /**
     * capitalize first character
     * <pre>
     *     author:ThomTT
     *     date:01/08/2022
     * </pre>
     *
     * @param text
     * @return
     */
    public static String capitalizeFirst(String text) {
        String textAfter = "";
        if (text.length() > 0) {
            textAfter = text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();

        }
        return textAfter;

    }

    /**
     * check user name more than 8 characters
     * <pre>
     *     author:ThomTT
     *     date:03/08/2022
     * </pre>
     * @param name
     * @return
     */
    public static boolean isName(String name) {
        if (name.length() >= 8) {
            return true;
        }
        return false;
    }
    /**
     * get date
     * <pre>
     *     author:ThomTT
     *     date:03/08/2022
     * </pre>
     *  @return
     */

    public static String getDateAndTime() {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd hh:mm a");
        String dateAndTime = formatter.format(date);
        return dateAndTime;
    }
    public  static int getPriceToInt(String priceOld){
        int newPrice= Integer.parseInt(priceOld.replace(",",""));
        return  newPrice;

    }


}

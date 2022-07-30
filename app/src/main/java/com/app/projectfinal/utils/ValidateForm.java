package com.app.projectfinal.utils;


public class ValidateForm {
    /**
     * check if it's email format
     * <pre>
     *     author:ThomTT
     *     date: 21/07/2022
     * </pre>
     * @param email
     * @return
     */
    public static   boolean isEmail(String email) {
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
     * @param pass
     * @return
     */
    public   static  boolean validatePassword(String pass){
        if (pass.length()>=8) {
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
     * @param pass
     * @param rePass
     * @return
     */
    public   static  boolean checkRePassWord(String pass, String rePass){
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
     * @param phoneNumber
     * @return
     */
    public static boolean isPhoneNumber(String phoneNumber) {
        String checkPhoneNumber = "^[0-9]{9,10}$";
        if (phoneNumber.matches(checkPhoneNumber)) {
            return true;
        }
        return false;
    }


}

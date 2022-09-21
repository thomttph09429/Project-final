package com.app.projectfinal.utils;

public class Constant {
    public static final String HTTP = "https://e2e4-117-7-236-58.ap.ngrok.io/";

    public static final String REGISTER = HTTP+"register";
    public static final String LOGIN = HTTP+"login";
    public static final String PRODUCTS = HTTP+"stores/products";
    public static final String ADD_PRODUCTS = HTTP+"stores/products";
    public static final String ADD_STORES = HTTP+"stores";
    public static final String CATEGORY = HTTP+"categories";
    public static final String GET_UNIT = HTTP+"units";
    public static final String UPDATE_USER =HTTP+ "users";
    public static final String ADDRESS = HTTP+ "addresses";
    public static final String ORDER = HTTP+ "orders";
    public static final String TOTAL_ORDER = HTTP+ "orders/total";
    public static final String CHANGE_PASS = HTTP+ "users/update-password";
    public static final String STATISTICAL = HTTP+ "stores/statistical";

    public static final int PICK_IMAGE_REQUEST = 100;
    public static final int MY_CAMERA_UPDATE_COVERPHOTO = 200;

    //product
    public static final String NAME_PRODUCT = "productName";
    public static final String PRICE_PRODUCT = "price";
    public static final String IMAGE1_PRODUCT = "image1";
    public static final String STORE_NAME_PRODUCT = "storeName";
    public static final String CATEGORY_NAME = "categoryName";
    public static final String DESCRIPTION_PRODUCT = "description";
    public static final String QUANTITY_PRODUCT = "quantity";
    public static final String UNIT_ID_PRODUCT = "unitId";
    public static final String STORE_ID_PRODUCT = "storeId";
    public static final String CATEGORY_ID = "categoryId";
    public static final String NAME = "name";
    public static final String ID_PRODUCT = "id";
    public static final String TOTAL = "total";
    public static final String UNIT_NAME = "unitName";
    public static final String TOTAL_PRICE = "totalPrice";
    public static final String BUY_NOW = "buyNow";

    //user
    public static final String PHONE = "phone";
    public static final String ROLE = "role";
    public static final String DATE_OF_BIRTH = "dateOfBirth";
    public static final String EMAIL = "email";
    public static final String AVATAR = "avatar";


    //sign up store
    public static final String NAME_STORE = "name";
    public static final String DESCRIPTION_STORE = "description";
    public static final String LINK_SUPPORT_STORE = "linkSupport";
    public static final String USER_ID = "userId";
    public static final String IS_ACTIVE = "isActive";

    //unit
    public static final String NAME_UNIT = "name";
    public static final String ID_UNIT = "id";


    //sharedpreference
    public static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    public static final String USER_ID_SAVE = "USER_ID";
    public static final String STORE_ID = "STORE_ID";
    public static final String USER_NAME_SAVE = "USER_NAME";
    public static final String STORE_NAME = "STORE_NAME";
    public static final String PHONE_NUMBER = "PHONE_NUMBER";
    public static final String ROLE_SAVE = "ROLE_SAVE";
    public static final String TOKEN = "TOKEN";

    //pass data
    public static final String SENT_PHONE_NUMBER = "SENT_PHONE_NUMBER";
    public static final String SEND_NAME = "SEND_NAME";
    public static final String SEND_PASS = "SEND_PASS";
    //chat
    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;
    //db
    public static final String TABLE_NAME = "CARD";
    public static final String TABLE_DB = "Cart.db";
    //address
    public static final String PROVINCE = "https://provinces.open-api.vn/api/?depth=1";
    public static final String CODE_PROVINCE = "PROVINCE";
    public static final String CODE_DISTRICT = "DISTRICT";

    public static final String PROVINCE_NAME = "PROVINCE_NAME";
    public static final String DISTRICT_NAME = "DISTRICT_NAME";
    public static final String WARD_NAME = "WARD_NAME";
    //order
    public static final String STATUS = "status";



}

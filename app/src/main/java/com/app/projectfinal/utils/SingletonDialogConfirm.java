package com.app.projectfinal.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.view.Window;
import android.widget.Toast;

import com.app.projectfinal.R;
import com.app.projectfinal.activity.MainActivity;

public class SingletonDialogConfirm {
    static TextToSpeech t1;
    private static SingletonDialogConfirm ourInstance = new SingletonDialogConfirm();
    private Context appContext;
    private SingletonDialogConfirm() { }
    public static Context get() {
        return getInstance().getContext();
    }
    public static synchronized SingletonDialogConfirm getInstance() {
        return ourInstance;
    }
    public void init(Context context) {
        if (appContext == null) {
            this.appContext = context;
        }
    }
    private Context getContext() {
        return appContext;
    }

}

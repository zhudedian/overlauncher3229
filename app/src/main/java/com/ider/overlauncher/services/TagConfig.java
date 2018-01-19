package com.ider.overlauncher.services;

import android.os.Parcelable;

public abstract class TagConfig implements Parcelable {
    public static int STATUS_APP = 1;
    public static int STATUS_PIC = 2;

    public String tag;
    public int status;

    public abstract boolean isAvailable();

}

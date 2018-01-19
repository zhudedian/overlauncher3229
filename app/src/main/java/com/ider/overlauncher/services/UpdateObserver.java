package com.ider.overlauncher.services;



public interface UpdateObserver {

    public void updateConfig(TagConfig config);
    public void shortcutInstalled(String pkgname);
    public String getItemTag();
    public void allDrawedOver();

}

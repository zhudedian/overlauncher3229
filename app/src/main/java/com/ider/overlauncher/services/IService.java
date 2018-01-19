package com.ider.overlauncher.services;



import java.util.ArrayList;

/**
 * Created by ider-eric on 2016/11/23.
 */

public interface IService {



    public void requestSuccess(ArrayList<TagConfig> configs);

    public void requestLater(int delay);

}

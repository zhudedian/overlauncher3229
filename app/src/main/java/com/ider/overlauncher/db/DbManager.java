package com.ider.overlauncher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ider.overlauncher.applist.Application;
import com.ider.overlauncher.applist.ApplicationUtil;
import com.ider.overlauncher.model.DaoMaster;
import com.ider.overlauncher.model.DaoSession;
import com.ider.overlauncher.model.PackageHolder;
import com.ider.overlauncher.model.PackageHolderDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ider-eric on 2017/2/22.
 */

public class DbManager {

    private Context context;
    private String dbName = "app.db";
    private DaoMaster.DevOpenHelper helper;

    private static DbManager dbManager;
    private DbManager(Context context) {
        this.context = context;
    }

    public static DbManager getInstance(Context context) {
        synchronized (DbManager.class) {
            if(dbManager == null) {
                dbManager = new DbManager(context);
            }
        }
        return dbManager;
    }



    private SQLiteDatabase getReadableDatabase() {
        if(helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return helper.getReadableDatabase();
    }

    private SQLiteDatabase getWritableDatabase() {
        if(helper == null) {
            helper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        return helper.getWritableDatabase();
    }

    public void insertPackage(PackageHolder holder) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            try {
                dao.insert(holder);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void insertPackages(ArrayList<PackageHolder> holders) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            try {
            dao.insertInTx(holders);}
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public List<PackageHolder> queryPackages() {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            QueryBuilder<PackageHolder> queryBuilder = dao.queryBuilder();
            return queryBuilder.list();
        }
    }
    public List<ServerApp> queryPackages(String tag) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            QueryBuilder<PackageHolder> queryBuilder = dao.queryBuilder();
            List<PackageHolder> list= queryBuilder.list();
            List<ServerApp> listtag = new ArrayList<>();
            for (PackageHolder ph:list) {
                if(ph.getTag().equals(tag)){
                    Application app = ApplicationUtil.doApplication(context,ph.getPackageName());
                    if(app!=null) {
                        listtag.add(ph);
                    }else{
                        removePackage(ph);
                    }
                }

            }
            return listtag;
        }
    }

    public void removePackage(PackageHolder holder) {
        synchronized (DbManager.class) {
            DaoMaster master = new DaoMaster(getWritableDatabase());
            DaoSession session = master.newSession();
            PackageHolderDao dao = session.getPackageHolderDao();
            try {
            dao.delete(holder);
            }catch (Exception e){
            e.printStackTrace();
        }
        }
    }

}

package com.raye7.news.data.db;

import android.content.Context;
import android.util.Log;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmManager {
    private static final String TAG = RealmManager.class.getName();

    private Realm realm;
    private Context context;
    private static RealmManager instance = null;

    public RealmManager(Context context) {
        this.context = context;
        Realm.init(this.context);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name(context.getPackageName() + ".realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        realm.setDefaultConfiguration(config);
        realm = Realm.getInstance(config);
    }

    public static synchronized RealmManager getInstance(Context context){
        if(null == instance) instance = new RealmManager(context);
        return instance;
    }

    public Realm getRealm(){
        return this.realm;
    }

    public void saveNews(final ArticleDBModel itemPOJO){
        try{
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealm(itemPOJO);
                }
            });
        } catch (Exception e){
            Log.i(TAG, "saveNews: " + e.toString());
        }
    }

    public RealmResults<ArticleDBModel> getDeliveryItems(){
        return realm.where(ArticleDBModel.class).findAll();
    }
}
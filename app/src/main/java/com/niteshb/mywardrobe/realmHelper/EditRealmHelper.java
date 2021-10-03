package com.niteshb.mywardrobe.realmHelper;

import com.niteshb.mywardrobe.models.ItemModel;

import io.realm.Realm;

public class EditRealmHelper {

    public static void setFavourite(String itemId, boolean isFavourite){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ItemModel model = realm.where(ItemModel.class).equalTo("id", itemId).findFirst();
                    if(model!=null){
                        model.setFavourite(isFavourite);
                        model.setDateModified(System.currentTimeMillis());
                        model.setSyncRequired(true);
                    }
                }
            });
        }
    }

}

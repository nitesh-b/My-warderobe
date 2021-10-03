package com.niteshb.mywardrobe.services;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.niteshb.mywardrobe.models.ItemModel;

import io.realm.Realm;
import io.realm.RealmObject;

public class BackgroundTask implements Runnable {

    public static final String TAG = "BackgroundTask";

    @Override
    public void run() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            FirebaseFirestore.getInstance()
                    .collection("UserData")
                    .document(user.getUid())
                    .collection("items")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error != null){
                                Log.d(TAG, "onEvent: Error occured");
                            }else{
                                for(DocumentChange change: value.getDocumentChanges()){
                                    switch (change.getType()){
                                        case ADDED:
                                        case MODIFIED:
                                            update(change.getDocument().toObject(ItemModel.class));
                                            break;

                                        case REMOVED:
                                            delete(change.getDocument().toObject(ItemModel.class));
                                            break;

                                    }
                                }
                            }
                        }
                    });

        }




    }

    private void update(ItemModel model){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                 //   ItemModel object = realm.where(ItemModel.class).equalTo("id", model.getId()).findFirst();
                            model.setSyncRequired(false);
                            realm.insertOrUpdate(model);
                }
            });
        }

    }

    private void delete(ItemModel model){
        try(Realm realm = Realm.getDefaultInstance()){
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    ItemModel object = realm.where(ItemModel.class).equalTo("id", model.getId()).findFirst();
                    if(object!= null){
                        object.deleteFromRealm();
                    }
                }
            });
        }
    }
}

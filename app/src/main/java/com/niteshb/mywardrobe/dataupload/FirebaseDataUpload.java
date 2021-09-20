package com.niteshb.mywardrobe.dataupload;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.niteshb.mywardrobe.R;
import com.niteshb.mywardrobe.models.ItemModel;

import java.util.Objects;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class FirebaseDataUpload extends AppCompatDialogFragment {
private ItemModel model;
private Context mContext;
private ProgressBar mProgressBar;
private TextView mTextView;
private StorageReference storageReference;
private FirebaseAuth mAuth;
private FirebaseFirestore db;
private Uri capturedImageUri;
private AlertDialog.Builder builder;

private FirebaseDataUploadListener uploadListener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.uploading_dialog,null);
        builder.setView(view)
                .setTitle("Uploading your info...");
        builder.setCancelable(false);
        mProgressBar =view.findViewById(R.id.uploading_progressbar);
        mTextView = view.findViewById(R.id.uploading_textview);
        upload();
        return builder.create();
    }

    public FirebaseDataUpload(Context mContext, ItemModel model, Uri capturedImageUri) {
        this.mContext = mContext;
        this.model = model;
        this.capturedImageUri = capturedImageUri;
        db = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference("ImageFolder");
        mAuth = FirebaseAuth.getInstance();

    }

    private void upload() {

        Log.d(TAG, "onCreate: User email: "+ mAuth.getCurrentUser().getEmail());
        final String currentTime = String.valueOf(System.currentTimeMillis());
        final long currentDateTime = System.currentTimeMillis();
       if(capturedImageUri != null){
            final StorageReference fileReference = storageReference.child(mAuth.getCurrentUser().getEmail()+ "/" + currentTime +"."+"jpg");

            UploadTask uploadTask = fileReference.putFile(capturedImageUri);
            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            StorageMetadata metadata = taskSnapshot.getMetadata();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    model.setImageReference(uri.toString());  //get the download url
                                    model.setDateAdded(currentDateTime);
                                    model.setDateModified(currentDateTime);
                                    model.setId(UUID.randomUUID().toString());
                                    firestoreUpload(model);
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress =  (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                            mProgressBar.setProgress((int) progress);
                           mTextView.setText("Please Wait. We are uploading your data.");

                        }
                    });
       }else{
           Toast.makeText(mContext, "No image Found", Toast.LENGTH_SHORT).show();
       }

    }

    /*Get File extension ---- if selected from file chooser*/
    private String getFileExtension(Uri uri){
        ContentResolver resolver = mContext.getContentResolver();
        MimeTypeMap map= MimeTypeMap.getSingleton();
        return map.getExtensionFromMimeType(resolver.getType(uri));
    }

    private void firestoreUpload(ItemModel model) {
        db.collection("UserData")
                .document(mAuth.getUid())
                .collection("items")
                .document(model.getId())
                .set(model)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(mContext, "Successful", Toast.LENGTH_SHORT).show();
                        mTextView.setTextSize(20);
                        mTextView.setText("Done!");
                        mProgressBar.setVisibility(View.GONE);
                        uploadListener.uploadSuccess(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext, "Could not write on Firestore", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            uploadListener = (FirebaseDataUploadListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement FirebaseDataUploadListener");
        }
    }

    public interface FirebaseDataUploadListener{
        void uploadSuccess(Boolean complete);
}
}

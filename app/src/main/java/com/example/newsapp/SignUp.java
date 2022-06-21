package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {
    private final String PROFILE_IMAGE ="userProfileImage";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    TextInputEditText editTextEmail,editTextPassword,editTextRePassword,editTextPhoneNumber,editTextUserName;
    CircleImageView profileImage;
    Uri profileImageUri;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextEmail = findViewById(R.id.sign_up_email_et);
        editTextPassword = findViewById(R.id.sign_up_password_et);
        editTextRePassword=findViewById(R.id.sign_up_re_password_et);
        editTextUserName = findViewById(R.id.sign_user_name_et);
        editTextPhoneNumber = findViewById(R.id.sign_phone_number_et);
        profileImage = findViewById(R.id.sign_up_profile_image);
    }

    public void signUp(View view) {
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String rePassword = editTextRePassword.getText().toString();
        String userName= editTextUserName.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        if (profileImageUri == null){
            Toast.makeText(this, "Please select profile image"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (email.isEmpty()&&password.isEmpty()&&rePassword.isEmpty()&&userName.isEmpty()&&phoneNumber.isEmpty()){
            Toast.makeText(this, "Please fill all data"
                    , Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isEmailValid(email)){
            editTextEmail.setError("ex: example@domain.com");
            return;
        }
        if (password.length()<8){
            editTextPassword.setError("Password must be at least 8 characters");
            return;
        }

        if (!password.equals(rePassword)){
            editTextRePassword.setError("The two passwords are not marching");
            return;
        }
        userData = new UserData();
        userData.setUserName(userName);
        userData.setPhoneNumber(phoneNumber);
        createAccountWithEmailAndPassword(email,password);
    }

    private void createAccountWithEmailAndPassword(String email, String password) {
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            uploadProfileImage();
                        }else{
                            showErrorMessage(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    public void openGallery(View view) {
        CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result =CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                profileImageUri = result.getUri();
                profileImage.setImageURI(profileImageUri);
            }else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadProfileImage(){
        storageReference.child(PROFILE_IMAGE).child(auth.getCurrentUser().getUid())
                .putFile(profileImageUri)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            downloadProfileImage();
                            Toast.makeText(SignUp.this, "Profile Image Uploaded"
                                    , Toast.LENGTH_SHORT).show();
                        }else {
                            showErrorMessage(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }

    private void downloadProfileImage(){
        storageReference.child(PROFILE_IMAGE).child(auth.getCurrentUser().getUid())
                .getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Uri> task) {
                if (task.isSuccessful()){
                    String profileImageUrl = task.getResult().toString();
                    userData.setProfileImageUrl(profileImageUrl);
                    setUserDataToCloud();
                }else {
                    showErrorMessage(Objects.requireNonNull(task.getException()));
                }
            }
        });
    }

    private void setUserDataToCloud(){
        fireStore.collection("users")
                .document(auth.getCurrentUser().getUid())
                .set(userData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(SignUp.this, "Account Created"
                                    , Toast.LENGTH_SHORT).show();
                            finish();
                        }else {
                            showErrorMessage(Objects.requireNonNull(task.getException()));
                        }
                    }
                });
    }
    private boolean isEmailValid(String email) {
        final Pattern EMAIL_REGEX = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", Pattern.CASE_INSENSITIVE);
        return EMAIL_REGEX.matcher(email).matches();
    }



    private void showErrorMessage(Exception e)
    {
        String errorMessage = e.getMessage();
        Toast.makeText(SignUp.this, errorMessage,
                Toast.LENGTH_SHORT).show();
    }


}
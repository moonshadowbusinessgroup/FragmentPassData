package com.hadimusthafa.fragmentpassdata;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment1 extends Fragment {

    View view;
    AppCompatButton submit;
    CircleImageView imageViewProfilePicture;
    AppCompatEditText nameE, emailE, genderE, statusE;
    byte[] byteArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_1, container, false);
        imageViewProfilePicture = view.findViewById(R.id.iv_profile);
        submit = view.findViewById(R.id.submit);
        nameE = view.findViewById(R.id.nameE);
        emailE = view.findViewById(R.id.emailE);
        genderE = view.findViewById(R.id.genderE);
        statusE = view.findViewById(R.id.statusE);

        imageViewProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseProfilePicture();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passData();
            }
        });
        return view;
    }

    private void passData() {
        Bundle bundle = new Bundle();
        String name = nameE.getText().toString();
        String email = emailE.getText().toString();
        String gender = genderE.getText().toString();
        String status = statusE.getText().toString();

        if (!name.isEmpty() && !email.isEmpty() && !gender.isEmpty() && !status.isEmpty()) {
            bundle.putString("name", name);
            bundle.putString("email", email);
            bundle.putString("gender", gender);
            bundle.putString("status", status);
            bundle.putByteArray("image", byteArray);

            if (byteArray != null) {
                Fragment2 fragment2 = new Fragment2();
                fragment2.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, fragment2).commit();
            } else {
                Toast.makeText(getContext(), "Set your image", Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(getContext(), "Enter full details", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseProfilePicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.image_pic_dialog, null);
        builder.setCancelable(false);
        builder.setView(dialogView);

        AppCompatImageView imageViewADPPCamera = dialogView.findViewById(R.id.iv_camera);

        final AlertDialog alertDialogProfilePicture = builder.create();
        alertDialogProfilePicture.show();

        imageViewADPPCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions()) {
                    takePictureFromCamera();
                    alertDialogProfilePicture.dismiss();
                }
            }
        });
    }


    private void takePictureFromCamera() {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePicture.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePicture, 1);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmapImage = (Bitmap) bundle.get("data");
                    imageViewProfilePicture.setImageBitmap(bitmapImage);

                    ByteArrayOutputStream bStream = new ByteArrayOutputStream();
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, bStream);
                    byteArray = bStream.toByteArray();
                }
                break;
        }
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            int cameraPermission = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA);
            if (cameraPermission == PackageManager.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 20);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            takePictureFromCamera();
        } else
            Toast.makeText(getContext(), "Permission not Granted", Toast.LENGTH_SHORT).show();
    }
}
package com.hadimusthafa.fragmentpassdata;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment2 extends Fragment {

    View view;
    CircleImageView imageViewProfilePicture;
    AppCompatTextView nameT, emailT, genderT, statusT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_2, container, false);
        imageViewProfilePicture = view.findViewById(R.id.iv_profile);
        nameT = view.findViewById(R.id.nameT);
        emailT = view.findViewById(R.id.emailT);
        genderT = view.findViewById(R.id.genderT);
        statusT = view.findViewById(R.id.statusT);
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            Bitmap bmp;
            byte[] byteArray = bundle.getByteArray("image");
            bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);

            imageViewProfilePicture.setImageBitmap(bmp);

            nameT.setText("Name : " + bundle.getString("name"));
            emailT.setText("Email : " + bundle.getString("email"));
            genderT.setText("Gender : " + bundle.getString("gender"));
            statusT.setText("Status : " + bundle.getString("status"));
        }
        return view;
    }
}
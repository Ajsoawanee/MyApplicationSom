package ajsom.dru.ac.th.myapplicationsom.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.Replaceable;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.jibble.simpleftp.SimpleFTP;

import java.io.File;

import ajsom.dru.ac.th.myapplicationsom.R;

/**
 * Created by Master on 2/1/2018.
 */

public class RegisterFragment extends Fragment{

    private Uri uri;
    private ImageView imageView;
    private String pathImageString, nameImageString, nickNameString,
            userString, passwordString;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        Avata Controller
        avataController();


    }  //Main Method

    private void avataController() {
        imageView = getView().findViewById(R.id.imAvata);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Please Choose App"), 1);

            }
        });
    } // avata

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            if (resultCode == getActivity().RESULT_OK) {

//                Replace Image On ImageView
                uri = data.getData();
                Bitmap bitmap = BitmapFactory
                        .decodeStream(getActivity().getContentResolver()
                                .openInputStream(uri));
                imageView.setImageBitmap(bitmap);

//                Find Path and Name Image Choosed
                String[] strings = new String[]{MediaStore.Images.Media.DATA};
                Cursor cursor = getActivity().getContentResolver().query(uri,strings,
                        null, null, null);

                if (cursor != null) {

                    cursor.moveToFirst();
                    int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    pathImageString = cursor.getString(index);

                } else {

                    pathImageString = uri.getPath();

                }

                Log.d("1FebV1", "Payh ==>" +pathImageString);

//                Upload Image to Server
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy
                        .Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                SimpleFTP simpleFTP = new SimpleFTP();
                simpleFTP.connect("ftp.swiftcodingthai.com",
                        21, "dru@swiftcodingthai.com","Abc12345");
                simpleFTP.bin();
                simpleFTP.cwd("Image");
                simpleFTP.stor(new File(pathImageString));
                simpleFTP.disconnect();





            } else {

                Toast.makeText(getActivity(), "Please Choose Image", Toast.LENGTH_SHORT).show();

            }



        } catch (Exception e) {
            e.printStackTrace();

        }

    } // onActivityResult

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        return view;
    }
}// Main Class

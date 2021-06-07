package com.example.amir.scancer.ui.scan;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.amir.scancer.MainActivity;
import com.example.amir.scancer.R;
import com.example.amir.scancer.databinding.FragmentHomeBinding;
import com.example.amir.scancer.models.Model;
import com.example.amir.scancer.retrofit.RetrofitInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;

public class HomeFragment extends Fragment implements View.OnClickListener {


    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int INTENT_REQUEST_CODE = 100;

    private static final int CAMERA_REQUEST_CODE = 101;

    public static final String URL = "http://192.168.0.190:8000";

    private Button mBtImageSelect;
    private Button mBtImageTake;
    private ProgressBar mProgressBar;

    private TextView resultTitle;
    private TextView result;
    private ImageView resultIcon;

    private String chance;


    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        mBtImageSelect = root.findViewById(R.id.btn_select_image);

        mProgressBar = root.findViewById(R.id.progress);
        mBtImageTake = root.findViewById(R.id.btn_take_image);

        resultTitle = root.findViewById(R.id.scan_result_title);
        result = root.findViewById(R.id.scan_result);
        resultIcon = root.findViewById(R.id.result_icon);

        mBtImageSelect.setOnClickListener(this);
        mBtImageTake.setOnClickListener(this);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select_image:{

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

                intent.setType("image/*");

                try {
                    startActivityForResult(intent, INTENT_REQUEST_CODE);

                } catch (ActivityNotFoundException e) {

                    e.printStackTrace();
                }


            }
            break;
            case R.id.btn_take_image:{

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);

            }
            break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == INTENT_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                try {

                    InputStream is = getActivity().getContentResolver().openInputStream(data.getData());

                    uploadImage(getBytes(is));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (requestCode == CAMERA_REQUEST_CODE){
            if(resultCode == RESULT_OK){

                onCaptureImageResult(data);


            }


        }

    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        int bytes = thumbnail.getByteCount();
        ByteBuffer buffer = ByteBuffer.allocate(bytes);
        thumbnail.copyPixelsToBuffer(buffer);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.PNG, 100, baos);

        InputStream is = new ByteArrayInputStream(baos.toByteArray());
        //InputStream is = new ByteArrayInputStream(buffer.array());
        try {
            mProgressBar.setVisibility(View.VISIBLE);
            uploadImage(getBytes(is));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 2048;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }

    private void uploadImage(byte[] imageBytes) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);

        MultipartBody.Part body = MultipartBody.Part.createFormData("image", "image.jpg", requestFile);
        Call<Model> call = retrofitInterface.uploadImage(body);
        resultIcon.setVisibility(View.GONE);
        resultTitle.setVisibility(View.GONE);
        result.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Model>() {
            @Override
            public void onResponse(Call<Model> call, retrofit2.Response<Model> response) {

                mProgressBar.setVisibility(View.GONE);

                if (response.isSuccessful()) {

                    //Response responseBody = response.body();
                    //mBtImageShow.setVisibility(View.VISIBLE);
                    //mImageUrl = URL + responseBody.;
                    chance =response.body().getMessage();
                    //Snackbar.make(getActivity().findViewById(R.id.content), response.body().getMessage(),Snackbar.LENGTH_SHORT).show();
                    resultIcon.setVisibility(View.VISIBLE);
                    resultTitle.setVisibility(View.VISIBLE);
                    result.setVisibility(View.VISIBLE);
                    result.setText(chance);
                    result.append(" درصد ");

                } else {

                    ResponseBody errorBody = response.errorBody();

                    Gson gson = new Gson();

                    try {

                        Response errorResponse = gson.fromJson(errorBody.string(), Response.class);
                        Snackbar.make(getActivity().findViewById(R.id.content), response.errorBody().string(),Snackbar.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Model> call, Throwable t) {

                mProgressBar.setVisibility(View.GONE);
                Snackbar.make(getActivity().findViewById(R.id.content), t.getLocalizedMessage(),Snackbar.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: "+t.getLocalizedMessage());
            }
        });
    }

}
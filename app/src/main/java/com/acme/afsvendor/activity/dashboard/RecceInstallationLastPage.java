package com.acme.afsvendor.activity.dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.acme.afsvendor.R;
import com.acme.afsvendor.databinding.ActivityRecceInstallationLastPageBinding;
import com.acme.afsvendor.utility.RoundRectCornerImageView;
import com.acme.afsvendor.viewmodel.APIreferenceclass;
import com.acme.afsvendor.viewmodel.ApiInterface;

import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RecceInstallationLastPage extends AppCompatActivity implements ApiInterface {
    private ActivityRecceInstallationLastPageBinding binding;

    int userid;
    String projectid;
    String logintoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= DataBindingUtil.setContentView(this, R.layout.activity_recce_installation_last_page);

        //initializing
        userid= 0;
        projectid= "";
        logintoken= "";

        try{
            userid= getIntent().getIntExtra("userid", 0);
            projectid= getIntent().getStringExtra("projectid");
            logintoken= getIntent().getStringExtra("logintoken");

        }catch (Exception e){
            Log.d("tag232", e.toString());
        }

        //Start install button
        binding.btnTaskCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //TODO api call

            }
        });



        Log.d("whichclass", "RecceInstallationDashboard");
        APIreferenceclass api = new APIreferenceclass(logintoken, this, userid, projectid, 0);

    }

    void implementUI(String response) {

        try {
            JSONObject jsonobj1 = new JSONObject(response);
            JSONObject jsonobj = new JSONObject(jsonobj1.getString("data"));

            binding.tvAddSiteDetail.setText(jsonobj.getString("retail_name"));
            binding.etSiteNo.setText(jsonobj.getString("asm_name"));
            binding.tvStartDate.setText(jsonobj.getString("state"));
            binding.tvEndDate.setText(jsonobj.getString("district"));
            binding.tvLocation.setText(jsonobj.getString("city"));
            binding.tvLongitude.setText(jsonobj.getString("length"));
            binding.tvWidth.setText(jsonobj.getString("width"));
            binding.tvHeight.setText(jsonobj.getString("date"));
            binding.tvTotalArea.setText(jsonobj.getString("owner_name"));
            binding.tvMediaType.setText(jsonobj.getString("email"));
            binding.tvIllumination.setText(jsonobj.getString("mobile"));
            binding.tvLatitude1.setText(jsonobj.getString("lat"));
            binding.tvLongitude1.setText(jsonobj.getString("long"));
            binding.tvLatitude.setText(jsonobj.getString("location"));

            RoundRectCornerImageView tvImage = findViewById(R.id.ivCampaignImage);
            if (jsonobj.getString("image1") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationLastPage.LoadImageAsyncTask(tvImage).execute("https://acme.warburttons.com/" + jsonobj.getString("image1"));
            }

            RoundRectCornerImageView tvImage1 = findViewById(R.id.ivCampaignImage1);
            if (jsonobj.getString("owner_signature") != null) {
                Log.d("tg2", "image code executing");
                new RecceInstallationLastPage.LoadImageAsyncTask(tvImage1).execute("https://acme.warburttons.com/" + jsonobj.getString("owner_signature"));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class LoadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private RoundRectCornerImageView imageView;

        LoadImageAsyncTask(RoundRectCornerImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String imageUrl = params[0];
            try {
                URL url = new URL(imageUrl);
                return BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
                // Handle error case
            }
        }
    }

    @Override
    public void onResponseReceived(String response) {

        Log.d("response", response);
        try {

            JSONObject jsonobj1 = new JSONObject(response);
            if (jsonobj1.getString("message").equals("Data fetched successfully!")) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        implementUI(response);
                    }
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
    }

package com.example.creditcard_custom;

import androidx.annotation.NonNull;
import  androidx.biometric.BiometricManager.Authenticators;
import  androidx.biometric.BiometricManager;


import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.creditcard_custom.databinding.ActivityMainBinding;
import com.example.creditcard_custom.utils.CreditCard;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

public class MainActivityTest extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {

    List<CreditCard> list_creditCard = new ArrayList<>();
    private ActivityMainBinding binding;
    private boolean test = false;

    //for fingerprint athentification
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    CardFrontAdapter frontAdapter;
    CardFrontFragment front;
    CardBackFragment back;
    boolean front_or_back = true;
    List<Boolean> show_hide = new ArrayList<>();
    //for slide view
    ViewPager2 viewPager2;
    static int c = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        executor = ContextCompat.getMainExecutor(this);
        biometricAuthentification();


        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();


        front = new CardFrontFragment();
        back = new CardBackFragment();

        int limit = retriveData();



        frontAdapter = new CardFrontAdapter(this);
        binding.pager.setOffscreenPageLimit(limit);
        for(int i=0; i<limit; ++i){
            frontAdapter.createFragment(i);
            show_hide.add(false);
        }

        binding.pager.setAdapter(frontAdapter);

        binding.pager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                int postion = binding.pager.getCurrentItem();
                if(c != 0){
                    if(!show_hide.get(postion)){
                        binding.btnShow.setText("Show");
                    }
                    else{
                        binding.btnShow.setText("Hide");
                    }
                }



            }
        });

        binding.btnDelete.setOnClickListener((event)->{
            if(list_creditCard.size() > 0){
                int position = binding.pager.getCurrentItem();
                list_creditCard.remove(position);
                frontAdapter.removeFragment(position);
                frontAdapter.notifyDataSetChanged();
            }
        });

        binding.btnShow.setOnClickListener((event)->{
            c++;
            int position = binding.pager.getCurrentItem();
            binding.btnShow.setClickable(false);
            if(!show_hide.get(position)){
                biometricPrompt.authenticate(promptInfo);
                show_hide.set(position, true);
                binding.btnShow.setText("Hide");
            }else{
                show_hide.set(position, false);
                binding.btnShow.setText("Show");
            }
            //int id = (int)frontAdapter.getItemId(binding.pager.getCurrentItem());
            if(list_creditCard.size() > 0){
                frontAdapter.showInfo(list_creditCard.get(position), position, !show_hide.get(position));
            }
            binding.btnShow.setClickable(true);
        });

        binding.btnNext.setOnClickListener((event)->{
            int position = binding.pager.getCurrentItem();
            int size = frontAdapter.replaceFragment(position);



            Toast.makeText(MainActivityTest.this, "size: "+size, Toast.LENGTH_SHORT).show();
            frontAdapter.notifyDataSetChanged();


        });


    }

    public int retriveData(){
        list_creditCard.add(new CreditCard("Hamdaoui Tayeb", "447", "4234 8528 6544 2348", "12/8"));
        list_creditCard.add(new CreditCard("Hamdaoui Tayeb", "786", "6546 4315 5874 3525", "3/22"));
        list_creditCard.add(new CreditCard("Hamdaoui Tayeb", "112", "9152 5463 4654 8746", "5/2"));

        return list_creditCard.size();
    }


    public void showInfo(String cardNumber, int vcc, String expDate, Fragment front, Fragment back){
        Bundle bundle = new Bundle();
        bundle.putString("name", cardNumber);
        bundle.putInt("vcc", vcc);
        bundle.putString("expDate", expDate);

        getSupportFragmentManager().beginTransaction().
                add(R.id.fragment_container, CardFrontFragment.class, bundle)
                .commit();
    }


    @Override
    public void onBackStackChanged() {
        if(binding.pager.getCurrentItem() == 0){
            super.onBackPressed();
        }else{
            binding.pager.setCurrentItem(binding.pager.getCurrentItem()-1);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void biometricAuthentification(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(Authenticators.BIOMETRIC_STRONG | Authenticators.DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // Prompts the user to create credentials that your app accepts.
/*                final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                        Authenticators.BIOMETRIC_STRONG | Authenticators.DEVICE_CREDENTIAL);
                startActivityForResult(enrollIntent, REQUEST_CODE);*/

                break;
        }



        biometricPrompt = new BiometricPrompt(MainActivityTest.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}

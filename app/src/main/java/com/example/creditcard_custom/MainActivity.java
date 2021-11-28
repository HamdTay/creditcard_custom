package com.example.creditcard_custom;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import com.example.creditcard_custom.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends FragmentActivity implements FragmentManager.OnBackStackChangedListener {


    static int count = 0;
    private ActivityMainBinding binding;
    private boolean test = false;
    CardFrontFragment front;
    CardBackFragment back;
    boolean front_or_back = true;

    //for slide view
    ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        front = new CardFrontFragment();
        back = new CardBackFragment();




        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .add(R.id.fragment_container, front)
                    .commit();
        }else{
            //TODO: check what (getFragmentManager().getBackStackEntryCount() > 0) is for?
            test = (getFragmentManager().getBackStackEntryCount() > 0);
        }
        //what does this do?
        getSupportFragmentManager().addOnBackStackChangedListener(this);

//        front = (CardFrontFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_container);
/*        binding.fragmentContainer.setOnClickListener((event)->{
            flipCard();
        });*/

        binding.btnNext.setOnClickListener((event)->{
            //showInfo("1234 5678 9456 4574", count++, "12/6", front, back);
            if(front_or_back){
                front.binding.tvMemberName.setText("Hamdaoui Tayeb");
                front_or_back = false;
                if(back.binding != null) {
                    back.binding.tvCvv.setText("445");
                }
            }else{
                front.getTvName().setText("Card Holder's Name");
                if(back.binding != null){
                    back.binding.tvCvv.setText("***");
                }
                front_or_back = true;
            }
        });



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

    private void flipCard() {
/*        if (test) {
            getFragmentManager().popBackStack();
            return;
        }
        // Flip to the back.
        //setCustomAnimations(int enter, int exit, int popEnter, int popExit)

        test = true;*/

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in,
                        R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in,
                        R.animator.card_flip_left_out)
                .replace(R.id.pager, back)
                .addToBackStack(null)
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
}


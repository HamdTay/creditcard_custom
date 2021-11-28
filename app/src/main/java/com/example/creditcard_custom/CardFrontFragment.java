package com.example.creditcard_custom;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creditcard_custom.databinding.FragmentCardFrontBinding;
import com.example.creditcard_custom.utils.FontTypeChange;

import static com.example.creditcard_custom.utils.CreditCardUtils.AMEX;
import static com.example.creditcard_custom.utils.CreditCardUtils.DISCOVER;
import static com.example.creditcard_custom.utils.CreditCardUtils.MASTERCARD;
import static com.example.creditcard_custom.utils.CreditCardUtils.NONE;
import static com.example.creditcard_custom.utils.CreditCardUtils.VISA;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardFrontFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardFrontFragment extends Fragment  {

    TextView tvNumber;
    TextView tvName;
    TextView tvValidity;
    ImageView ivType;

    FontTypeChange fontTypeChange;

    FragmentCardFrontBinding binding;



    public CardFrontFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CardFrontFragment newInstance(int position) {
        CardFrontFragment fragment = new CardFrontFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCardFrontBinding.inflate(inflater, container, false);
        View view = binding.getRoot();


        
        fontTypeChange = new FontTypeChange(getActivity());
        binding.tvCardNumber.setTypeface(fontTypeChange.get_fontface(3));
        binding.tvMemberName.setTypeface(fontTypeChange.get_fontface(3));

        //bind class properties to their bind view
        tvNumber = binding.tvCardNumber;
        tvName = binding.tvMemberName;
        tvValidity = binding.tvValidity;
        ivType = binding.ivType;

/*        view.setOnClickListener((event)->{
            //Toast.makeText(getActivity(), "Long click", Toast.LENGTH_LONG).show();

        });*/

/*        try{
            if(!requireArguments().isEmpty()){
                String name = requireArguments().getString("name");
                if(name != null){
                    binding.tvMemberName.setText(name);
                }

            }
        }catch(Exception e){

        }*/


        return view;
    }

    public TextView getTvNumber() {
        return tvNumber;
    }

    public void setTvNumber(TextView tvNumber) {
        this.tvNumber = tvNumber;
    }

    public TextView getTvName() {
        return tvName;
    }

    public void setTvName(TextView tvName) {
        this.tvName = tvName;
    }

    public TextView getTvValidity() {
        return tvValidity;
    }

    public void setTvValidity(TextView tvValidity) {
        this.tvValidity = tvValidity;
    }

    public ImageView getIvType() {
        return ivType;
    }

    public void setCardType(int type) {
        switch(type)
        {
            case VISA:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_visa));
                break;
            case MASTERCARD:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_mastercard));
                break;
            case AMEX:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_amex));
                break;
            case DISCOVER:
                ivType.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_discover));
                break;
            case NONE:
                ivType.setImageResource(android.R.color.transparent);
                break;

        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }
}
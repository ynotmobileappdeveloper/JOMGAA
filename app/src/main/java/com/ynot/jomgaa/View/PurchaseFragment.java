package com.ynot.jomgaa.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ynot.jomgaa.Adapter.PurchaseAdapter;
import com.ynot.jomgaa.R;


public class PurchaseFragment extends Fragment {

    RecyclerView purchase;
    PurchaseAdapter purchaseAdapter;
    Toolbar toolbar;
    MainActivity mainActivity;

    public PurchaseFragment(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_purchase, container, false);
        purchase=view.findViewById(R.id.purchase);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        purchase.setLayoutManager(linearLayoutManager);
        purchaseAdapter=new PurchaseAdapter(getActivity());
        final AppCompatActivity activity = (AppCompatActivity) view.getContext();
        activity.getSupportActionBar().hide();
        toolbar=view.findViewById(R.id.purchasetool);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.onBackPressed();
            }
        });
        purchase.setAdapter(purchaseAdapter);
        return view;
    }
}
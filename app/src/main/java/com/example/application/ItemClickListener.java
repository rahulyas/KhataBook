package com.example.application;

import androidx.cardview.widget.CardView;

import com.example.application.Model.Items;

public interface ItemClickListener {
    void onClick(Items notes);
    void onLongClick(Items notes, CardView cardView);
}

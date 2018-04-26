package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import java.io.IOException;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    private TextView mKnownAsTextView;
    private TextView mDescriptionTextView;
    private TextView mOriginTextView;
    private TextView mIngredientsTextView;

    private TextView mKnownAsLabelTextView;
    private TextView mOriginLabelTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mKnownAsTextView = findViewById(R.id.also_known_tv);
        mDescriptionTextView = findViewById(R.id.description_tv);
        mOriginTextView = findViewById(R.id.origin_tv);
        mIngredientsTextView = findViewById(R.id.ingredients_tv);

        mKnownAsLabelTextView = findViewById(R.id.label_also_known_tv);
        mOriginLabelTextView = findViewById(R.id.label_origin_tv);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];

        Sandwich sandwich = null;

        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        if(!sandwich.getAlsoKnownAs().isEmpty()) {
            mKnownAsTextView.setText(TextUtils.join("\n", sandwich.getAlsoKnownAs()));
        } else {
            mKnownAsLabelTextView.setVisibility(View.GONE);
            mKnownAsTextView.setVisibility(View.GONE);
        }

        if(!TextUtils.isEmpty(sandwich.getPlaceOfOrigin())) {
            mOriginTextView.setText(sandwich.getPlaceOfOrigin());
        } else {
            mOriginLabelTextView.setVisibility(View.GONE);
            mOriginTextView.setVisibility(View.GONE);
        }

        mDescriptionTextView.setText(sandwich.getDescription());
        mIngredientsTextView.setText(TextUtils.join("\n", sandwich.getIngredients()));
    }
}

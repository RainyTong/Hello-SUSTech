package com.example.xuversion.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.xuversion.R;

public class MyImageButton extends RelativeLayout{

    private TextView textView;

    /**
     * Constructor with one parameter
     * @param context the context
     */
    public MyImageButton(Context context){
        super(context);
    }

    /**
     * Constructor with two parameter: context and attributeSet
     * @param context context for my button
     * @param attributeSet attributeSet for my button
     */
    public MyImageButton(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        LayoutInflater.from(context).inflate(R.layout.btn_image_and_text, this, true);
        textView = findViewById(R.id.img_button_text);
    }

    /**
     * use to set MyImageButton's text
     * @param text the button text
     */
    public void setText(String text) {
        textView.setText(text);
    }

}

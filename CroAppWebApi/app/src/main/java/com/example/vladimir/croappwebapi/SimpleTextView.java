package com.example.vladimir.croappwebapi;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Vladimir on 4/2/2018.
 */

public class SimpleTextView extends TextView
{
    private static final float DEFAULT_TEXT_SIZE= (float) 12.0;
    private static float textSize=DEFAULT_TEXT_SIZE;

    public SimpleTextView(Context context)
    {
        super(context);
        this.setTextSize(textSize);
    }

    public SimpleTextView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.setTextSize(textSize);
    }

    public SimpleTextView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.setTextSize(textSize);
    }

    public static void setGlobalSize(float size)
    {
        textSize=size;
    }

    public static float getGlobalSize()
    {
        return textSize;
    }
}

package com.ericlau.testrecyclerviewandcardview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.ericlau.testrecyclerviewandcardview.R;

/**
 * Created by Eric.Lau on 16/3/7.
 * <p/>
 * RecyclerView的FooterView，简单的展示一个TextView
 */
public class Footer extends RelativeLayout {

    public Footer(Context context) {
        super(context);
        init(context);
    }

    public Footer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public Footer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {

        inflate(context, R.layout.footer, this);
    }
}

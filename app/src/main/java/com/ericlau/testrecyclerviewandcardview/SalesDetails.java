package com.ericlau.testrecyclerviewandcardview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class SalesDetails extends AppCompatActivity {

    TextView title_detail,content_detail;
    ImageView img_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_details);
        setTitle("Discount");
        //显示系统Actionbar的返回按钮
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        Bundle bundle = getIntent().getExtras();
        title_detail.setText(bundle.getString("Title"));
        content_detail.setText(bundle.getString("Content"));
        int imgRes = bundle.getInt("Photo");
        img_detail.setImageResource(imgRes);

    }

    private void initView() {
        title_detail = (TextView)findViewById(R.id.tv_detail_title);
        content_detail = (TextView)findViewById(R.id.tv_detail_content);
        img_detail = (ImageView)findViewById(R.id.img_sales);
    }

    //菜单返回上一页
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
        }
        return false;
    }
}

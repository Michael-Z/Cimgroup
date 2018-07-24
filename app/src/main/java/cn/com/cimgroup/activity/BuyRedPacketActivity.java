package cn.com.cimgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.R;
import cn.com.cimgroup.adapter.RedPacketAdapter;
import cn.com.cimgroup.frament.GridFragment;
import cn.com.cimgroup.view.NotScrollGridView;

/**
 * Created by small on 16/1/10.
 * 购买红包;
 */
public class BuyRedPacketActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GridFragment fragment = new GridFragment();
        fragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(android.R.id.content, fragment).commit();
    }



//    private NotScrollGridView gridView;
//    private TextView noticeText,titleView,rightTextView;
//    private ImageView backButton;
//    private RedPacketAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_buy_redpacket);
//
//        findViewById();
//        initData();
//    }
//
//
//    private void findViewById(){
//        gridView = (NotScrollGridView) findViewById(R.id.gridView_buy);
//        noticeText = (TextView) findViewById(R.id.red_packet_notice);
//        titleView = (TextView) findViewById(R.id.title_view);
//        backButton = (ImageView) findViewById(R.id.left_button);
//        rightTextView = (TextView) findViewById(R.id.right_textview);
//
//        rightTextView.setVisibility(View.VISIBLE);
//        rightTextView.setText("我的红包");
//        titleView.setText("购买红包");
//        noticeText.setText(Html.fromHtml(getResources().getString(R.string.red_packet_notice)));
//        backButton.setOnClickListener(this);
//        rightTextView.setOnClickListener(this);
//    }
//
//
//    private void initData(){
//        adapter = new RedPacketAdapter(this,null);
//        gridView.setAdapter(adapter);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.left_button:
//                onBackPressed();
//                break;
//            case R.id.right_textview:
//                Intent intent = new Intent(this,MessageCenterActivity.class);
//                intent.putExtra(MessageCenterActivity.TYPE,MessageCenterActivity.REDPACKET);
//                startActivity(intent);
//            default:
//                break;
//        }
//    }
}

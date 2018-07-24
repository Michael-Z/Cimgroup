package cn.com.cimgroup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.com.cimgroup.BaseActivity;
import cn.com.cimgroup.R;

public class TextDetailActivity extends BaseActivity implements OnClickListener {
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_detail);
		initView();
	}
	
	private void initView(){
		((TextView) findViewById(R.id.textView_title_word)).setText("详情");
		Intent intent = getIntent();
		TextView title = (TextView) findViewById(R.id.textView_text_title);
		TextView time = (TextView) findViewById(R.id.textView_text_time);
		TextView content = (TextView) findViewById(R.id.textView_text_content);
		title.setText(intent.getStringExtra("title"));
		time.setText(intent.getStringExtra("time"));
		content.setText(intent.getStringExtra("content"));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.textView_title_back:
			finish();
			break;

		default:
			break;
		}
	}
}

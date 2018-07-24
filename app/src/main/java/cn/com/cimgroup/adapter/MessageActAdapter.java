package cn.com.cimgroup.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.List;

import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.MessageObj;

/**
 * Created by small on 16/1/18.
 */
public class MessageActAdapter extends MessageInsideAdapter {


    public MessageActAdapter(Context context, List data) {
        super(context, data);
    }

    @Override
    public int getItemResource() {
        return R.layout.item_message_act;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView titleView = (TextView) holder.getView(R.id.act_title_view);
        ImageView imageView = (ImageView) holder.getView(R.id.act_image_view);
        TextView timeView = (TextView) holder.getView(R.id.act_time_view);


        MessageObj msg = mList.get(position);
        titleView.setText(msg.titile);
        timeView.setText(msg.timeQuJian);
        ImageLoader.getInstance().displayImage(msg.imgUrl, imageView);


        return convertView;
    }
}

package cn.com.cimgroup.banner;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import cn.com.cimgroup.R;
import cn.com.cimgroup.bean.ImageObj;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImagePagerAdapter extends RecyclingPagerAdapter implements OnClickListener {

	private Context context;
	private List<ImageObj> imageIdList;
	private DisplayImageOptions options;

	private int size;
	private boolean isInfiniteLoop;
	VpItemClickListener mItemListener;

	
	public List<ImageObj> getImageIdList() {
		return imageIdList;
	}

	
	public void setImageIdList(List<ImageObj> imageIdList) {
		this.imageIdList = imageIdList;
	}

	public ImagePagerAdapter(Context context, List<ImageObj> imageIdList, DisplayImageOptions options) {
		this.context = context;
		this.imageIdList = imageIdList;
		if (imageIdList!=null&&imageIdList.size()!=0) {
			this.size = imageIdList.size();
		}
		
		this.options = options;
		isInfiniteLoop = false;
	}

	public void setVpItemClickListener(VpItemClickListener listener) {
		if (listener != null) {
			mItemListener = listener;
		}
	}
	public void setDatas(List<ImageObj> imageIdList, DisplayImageOptions options){
		this.imageIdList = imageIdList;
		this.size = imageIdList.size();
		this.options = options;
		isInfiniteLoop = false;
	}

	@Override
	public int getCount() {
		if (imageIdList==null) {
			return 0;
		}
		return isInfiniteLoop ? Integer.MAX_VALUE : imageIdList.size();
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(int position, View view, ViewGroup container) {
		ViewHolder holder = null;
		if (view == null) {
			holder = new ViewHolder();
			view = View.inflate(context, R.layout.item_image_pager, null);
			// view = holder.imageView = new ImageView(context);
			holder.imageView = (ImageView) view.findViewById(R.id.img_pager);
			view.setTag(holder);
			holder.imageView.setOnClickListener(this);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		ImageObj imageInfo = imageIdList.get(getPosition(position));
		
		holder.imageView.setTag(imageInfo);
//		if (position == 1) {
//			holder.imageView.setImageResource(R.drawable.hall_image_1);
//		} else {
//			holder.imageView.setImageResource(R.drawable.hall_image_2);
//		}
		ImageLoader.getInstance().displayImage(imageInfo.getTurnImgUrl(), holder.imageView, options);
//		holder.imageView.setImageResource(imageIdList.get(getPosition(position)));
		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}

	public interface VpItemClickListener {

		void vpItemClick(ImageObj imageInfo);
	}

	@Override
	public void onClick(View v) {
		mItemListener.vpItemClick((ImageObj) v.getTag());
	}
}

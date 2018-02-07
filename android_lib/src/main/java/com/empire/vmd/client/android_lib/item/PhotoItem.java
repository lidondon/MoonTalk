package com.empire.vmd.client.android_lib.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.empire.vmd.client.android_lib.R;
import com.empire.vmd.client.android_lib.util.ImageUtil;

import java.util.List;

/**
 * Created by lidondon on 2016/6/8.
 */
public class PhotoItem implements View.OnClickListener {
    private Context context;
    //private LinearLayout llRoot;
    private ViewHolder viewHolder;
    private ImageView ivCheck;
    private String uri;
    private int width;
    private boolean setBitmap;
    private boolean isCheck;
    private List checkedPhotoList;
    private  View resultView;
    private int fraction;
    private int resourceId = -1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            viewHolder.ivPhoto.setImageBitmap((Bitmap) msg.obj);
        }
    };

    public PhotoItem (Context ctx, String u, boolean sb, List list) {
        context = ctx;
        uri = u;
        setBitmap = sb;
        checkedPhotoList = list;
    }

    public PhotoItem(Context ctx, int srcId) {
        context = ctx;
        resourceId = srcId;
    }

    public int setWidth(int parentWidth, int fraction) {
        return width = parentWidth / fraction - (fraction * 2); //每個imageView有兩邊的空隙
    }

    private LayoutInflater getInflater() {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE));
    }

    public View getView() {
        resultView = getInflater().inflate(R.layout.item_photo, null);
        findViews(resultView);
        if (width > 0) {
            ViewGroup.LayoutParams photoParam = viewHolder.ivPhoto.getLayoutParams();
            ViewGroup.LayoutParams checkParam = ivCheck.getLayoutParams();

            checkParam.width = photoParam.width = width;
            checkParam.height = photoParam.height = width;
            viewHolder.ivPhoto.setLayoutParams(photoParam);
            ivCheck.setLayoutParams(checkParam);
        }
        resultView.setOnClickListener(this);
        if (setBitmap) {
            viewHolder.setContent(uri);
        } else if (resourceId != -1) {
            viewHolder.setContent(resourceId);
        }

        return resultView;
    }

    private void findViews(View rootView) {
        viewHolder = new ViewHolder();
        viewHolder.ivPhoto = (ImageView) rootView.findViewById(R.id.ivPhoto);
        ivCheck = (ImageView) rootView.findViewById(R.id.ivCheck);
    }

    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    @Override
    public void onClick(View v) {
        if (checkedPhotoList != null) {
            isCheck = !isCheck;
            if (isCheck) {
                ivCheck.setImageResource(R.drawable.check_photo);
                checkedPhotoList.add(uri);
            } else {
                ivCheck.setImageDrawable(null);
                checkedPhotoList.remove(uri);
            }
        }
    }

    public class ViewHolder {
        private ImageView ivPhoto;

        public void setContent(String uri) {
            Message message = Message.obtain();

            message.obj = ImageUtil.getThumbnailBitmap(uri);
            handler.sendMessage(message);
        }

        public void setContent(Bitmap bitmap) {
            ivPhoto.setImageBitmap(bitmap);
        }

        public void setContent(int id) {
            ivPhoto.setImageResource(id);
        }
    }
}

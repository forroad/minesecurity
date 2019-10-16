package com.ycjw.minesecurity.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.ycjw.minesecurity.MainActivity;
import com.ycjw.minesecurity.R;

import java.util.HashMap;

public class GlideUtil {
    private static HashMap<ImgType,Long> glide_signature = new HashMap<>();
    public enum ImgType{
        head_img
    }

    static {
       glide_signature.put(ImgType.head_img,System.currentTimeMillis());
    }


    public static void showImage(ImageView view, ImgType type, Context context){
        if(type == ImgType.head_img){
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.mine_image)
                    .error(R.drawable.mine_image)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .signature(new MediaStoreSignature(ImgType.head_img.toString(), glide_signature.get(ImgType.head_img), 0))
                    .skipMemoryCache(true);
            Glide.with(context)
                    .load(Constant.HTTP + Constant.CURRENT_IP + "user/getUserHeadImg?telephone=" + MainActivity.user.getPhoneNum())
                    .apply(options)
                    .thumbnail(0.2f).into(view);
        }
    }

    public static void set_glide_signature(ImgType type){
        glide_signature.put(type,System.currentTimeMillis());
    }
}

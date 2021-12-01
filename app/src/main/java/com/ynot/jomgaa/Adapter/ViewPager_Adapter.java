package com.ynot.jomgaa.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.victor.loading.rotate.RotateLoading;
import com.ynot.jomgaa.Model.Images;
import com.ynot.jomgaa.R;

import java.util.List;

public class ViewPager_Adapter extends PagerAdapter {

    Context context;
    List<Images> imageModel;
    LayoutInflater inflater;

    public ViewPager_Adapter(Context context, List<Images> imageModel) {
        this.context = context;
        this.imageModel = imageModel;
    }

    @Override
    public int getCount() {
        return imageModel.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view == (RelativeLayout) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img);
        final RotateLoading loading = (RotateLoading) view.findViewById(R.id.rotateloading);
        loading.setVisibility(View.VISIBLE);
        loading.start();
        Glide.with(context).load(imageModel.get(position).getImages()).apply(new RequestOptions().override(600, 200)).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                loading.setVisibility(View.GONE);
                loading.stop();
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                loading.setVisibility(View.GONE);
                loading.stop();
                return false;
            }
        }).into(imageView);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}

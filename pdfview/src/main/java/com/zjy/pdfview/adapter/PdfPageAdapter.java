package com.zjy.pdfview.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.zjy.pdfview.R;
import com.zjy.pdfview.widget.ScaleImageView;

import java.util.List;

/**
 * Date: 2021/1/27
 * Author: Yang
 * Describe:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PdfPageAdapter extends RecyclerView.Adapter<PdfPageAdapter.ViewHolder> {

    private Context context;
    private List<Bitmap> pageList;

    public PdfPageAdapter(Context context, List<Bitmap> pageList) {
        this.context = context;
        this.pageList = pageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_page_item, null));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Bitmap item = pageList.get(i);
        viewHolder.itemIv.setLayoutParams(new FrameLayout.LayoutParams(context.getResources().getDisplayMetrics().widthPixels, context.getResources().getDisplayMetrics().heightPixels - 300));
        viewHolder.itemIv.setImageBitmap(item);
    }

    @Override
    public int getItemCount() {
        return pageList.size();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.itemIv.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ScaleImageView itemIv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemIv = itemView.findViewById(R.id.pdf_page_iv);
        }
    }
}

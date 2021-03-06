package com.zjy.pdfview;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.zjy.pdfview.adapter.PdfPageAdapter;
import com.zjy.pdfview.download.DownloadResultBroadcast;
import com.zjy.pdfview.download.DownloadService;
import com.zjy.pdfview.download.IDownloadCallback;
import com.zjy.pdfview.utils.PdfLog;
import com.zjy.pdfview.utils.layoutmanager.PageLayoutManager;
import com.zjy.pdfview.utils.layoutmanager.PagerChangedListener;
import com.zjy.pdfview.widget.PdfLoadingLayout;
import com.zjy.pdfview.widget.ScrollSlider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.zjy.pdfview.constants.Constants.DOWNLOAD_ACTION;
import static com.zjy.pdfview.download.DownloadService.DOWNLOAD_URL_KEY;

/**
 * Date: 2021/1/27
 * Author: Yang
 * Describe:
 */
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class PdfView extends FrameLayout implements IDownloadCallback {

    private Button openButton;
    private Button previousButton;
    private Button nextButton;
    private RecyclerView contentRv;
    private TextView pageIndexTv;
    private PdfLoadingLayout loadingLayout;
    private ScrollSlider scrollSlider;

    /**
     * page count of pdf file
     */
    private int pageCount;
    /**
     * index of current page
     */
    private int currentIndex;
    /**
     * quality of preview
     */
    private int quality;

    private String pdfLocalPath;
    private String pdfUrl;

    private List<Bitmap> pageList;

    private PdfRenderer pdfRenderer;
    private PdfRenderer.Page curPdfPage;
    private ParcelFileDescriptor parcelFileDescriptor;

    private PdfPageAdapter pageAdapter;
    private PageLayoutManager pageLayoutManager;
    private Intent serviceIntent;
    private RenderTask renderTask;

    private boolean hasRenderFinish;

    public PdfView(@NonNull Context context) {
        this(context, null);
    }

    public PdfView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PdfView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleStyleable(context, attrs);
        init();
    }

    private void handleStyleable(Context context, AttributeSet attrs) {
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PdfView, 0, 0);
        try {
            quality = ta.getInteger(R.styleable.PdfView_quality, 3);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }
    }


    private void init() {
        registerResultBroadcast();

        LayoutInflater.from(getContext()).inflate(R.layout.layout_pdf_view, this);
        openButton = findViewById(R.id.button_open);
        pageIndexTv = findViewById(R.id.page_index_tv);
        loadingLayout = findViewById(R.id.loading_layout);
        contentRv = findViewById(R.id.content_rv);
        previousButton = findViewById(R.id.button_previous);
        nextButton = findViewById(R.id.button_next);
        scrollSlider = findViewById(R.id.scroll_slider);

        pageLayoutManager = new PageLayoutManager(getContext(), PageLayoutManager.VERTICAL);
        pageLayoutManager.setOnPagerChangeListener(new PagerChangedListener() {
            @Override
            public void onInitComplete() {

            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                scrollToPosition();
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {

            }
        });
        contentRv.setLayoutManager(pageLayoutManager);

        loadingLayout.setLoadLayoutListener(new PdfLoadingLayout.LoadLayoutListener() {
            @Override
            public void clickRetry() {
                if (!TextUtils.isEmpty(pdfUrl)) {
                    loadPdf(pdfUrl);
                }
            }
        });

        pageList = new ArrayList<>();
        pageAdapter = new PdfPageAdapter(getContext(), pageList);
        contentRv.setAdapter(pageAdapter);

        openButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openPdf();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentIndex = pageLayoutManager.getCurrentPosition();
                if(currentIndex + 1 < pageLayoutManager.getItemCount()) {
                    currentIndex++;
                    scrollToPosition();
                }
            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                currentIndex = pageLayoutManager.getCurrentPosition();
                if (currentIndex - 1 >= 0) {
                    currentIndex--;
                    scrollToPosition();
                }
            }
        });

        scrollSlider.setScrollSlideListener(new ScrollSlider.ScrollSlideListener() {
            @Override
            public boolean scrolling(int scrollY) {
                int pageItemHeight = contentRv.getHeight() / pageCount;
                int scrollIndex = (int) scrollY / pageItemHeight;
                if(scrollIndex >= 0 && scrollIndex < pageLayoutManager.getItemCount()) {
                    scrollSlider.setTranslationY(scrollY - scrollY % pageItemHeight);
                    currentIndex = scrollIndex;
                    pageLayoutManager.scrollToPosition(currentIndex);
                    pageIndexTv.setText(generatePageIndexText());
                }
                return true;
            }
        });
    }

    public void loadPdf(String url) {
        contentRv.setVisibility(GONE);
        loadingLayout.showLoading();
        if (!TextUtils.isEmpty(url)) {
            pdfUrl = url;
            serviceIntent = new Intent(getContext(), DownloadService.class);
            serviceIntent.putExtra(DOWNLOAD_URL_KEY, url);
            getContext().startService(serviceIntent);
        }
    }

    private void scrollToPosition() {
        pageLayoutManager.scrollToPosition(currentIndex);
        pageIndexTv.setText(generatePageIndexText());
    }

    private DownloadResultBroadcast downloadReceiver;

    private void registerResultBroadcast() {
        downloadReceiver = new DownloadResultBroadcast();
        downloadReceiver.setResultCallback(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(DOWNLOAD_ACTION);
        getContext().registerReceiver(downloadReceiver, intentFilter);
    }

    private void unregisterResultBroadcast() {
        getContext().unregisterReceiver(downloadReceiver);
    }

    private String generatePageIndexText() {
        return (currentIndex + 1) + "/" + pageCount;
    }

    private void openPdf() {
        renderTask = new RenderTask();
        renderTask.execute();
    }

    private ParcelFileDescriptor getFileDescriptor() {
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(new File(pdfLocalPath), ParcelFileDescriptor.MODE_READ_ONLY);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return parcelFileDescriptor;
    }

    @Override
    public void downloadSuccess(String path) {

    }

    @Override
    public void downloadFail() {
        loadingLayout.showFail();
    }

    @Override
    public void downloadComplete(String path) {
        PdfLog.logDebug("path: " + path);
        pdfLocalPath = path;
        if (TextUtils.isEmpty(pdfLocalPath)) {
            return;
        }
        openPdf();
    }

    public class RenderTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                currentIndex = 0;
                pdfRenderer = new PdfRenderer(getFileDescriptor());
                pageCount = pdfRenderer.getPageCount();
                pageList.clear();
                for (int i=0; i<pageCount; i++) {
                    PdfRenderer.Page item = pdfRenderer.openPage(i);
                    curPdfPage = item;
                    int qualityRatio = getResources().getDisplayMetrics().densityDpi / (quality * 72);
                    Bitmap bitmap = Bitmap.createBitmap(qualityRatio * item.getWidth(), qualityRatio * item.getHeight(),
                            Bitmap.Config.ARGB_4444);
                    item.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
                    item.close();
                    pageList.add(bitmap);
                }
                hasRenderFinish = true;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                pageIndexTv.setText(generatePageIndexText());
                pageAdapter.notifyDataSetChanged();
                contentRv.setVisibility(VISIBLE);
                loadingLayout.showContent();
            } else {
                loadingLayout.showFail();
            }
        }
    }


    public void release() {
        try {
            if (curPdfPage != null) {
                curPdfPage.close();
                curPdfPage = null;
            }
        } catch (Exception ignored) {

        }
        if (renderTask != null) {
            renderTask.cancel(true);
            renderTask = null;
        }
        unregisterResultBroadcast();
        if (serviceIntent != null) {
            getContext().stopService(serviceIntent);
        }
        if (hasRenderFinish && pdfRenderer != null) {
            pdfRenderer.close();
            pdfRenderer = null;
        }
        if (null != parcelFileDescriptor) {
            try {
                parcelFileDescriptor.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

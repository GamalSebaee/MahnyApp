package atiaf.mehany.Activity.phase2.ads;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import atiaf.mehany.Activity.phase2.BaseFragment;
import atiaf.mehany.R;
import atiaf.mehany.phase2.remote_data.ApiCallBack;
import atiaf.mehany.phase2.response.AdsData;
import atiaf.mehany.phase2.response.AdsResponse;

public class AdsFragment extends BaseFragment {

    WebView wvAdsContent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.frgament_ads, container, false);
        wvAdsContent = mView.findViewById(R.id.wv_adsContent);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    @Override
    public void initViews() {
        loadAllAds();
    }

    private void loadAllAds() {
        if(progressDialog != null){
            progressDialog.show();
        }

        apiManager.getAllAds(new ApiCallBack() {
            @Override
            public void ResponseSuccess(Object data) {
                progressDialog.dismiss();
                AdsResponse adsResponse = (AdsResponse) data;
                setAdsData(adsResponse.getAdsData());
            }

            @Override
            public void ResponseFail(Object data) {
                progressDialog.dismiss();
                Toast.makeText(requireActivity(), "" + data, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setAdsData(AdsData adsData) {
        if (adsData.getBody() != null) {
            WebSettings webSettings = wvAdsContent.getSettings();
            webSettings.setJavaScriptEnabled(true);
            wvAdsContent.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }
            });

            if(adsData.getType() != null && adsData.getType().toLowerCase().equals("text")){
                wvAdsContent.loadData(adsData.getBody(), "text/html", "utf-8");
            }else if(adsData.getType() != null && adsData.getType().toLowerCase().equals("image")){
                wvAdsContent.loadUrl(adsData.getBody());

            }else if(adsData.getType() != null && adsData.getType().toLowerCase().equals("video")){
                wvAdsContent.loadUrl(adsData.getBody());
            }
           // String content="<html><body>Video From YouTube<br><iframe width=\\\"560\\\" height=\\\"315\\\" src=\\\"https://www.youtube.com/embed/SXTyaKL6udY\\\" frameborder=\\\"0\\\" allow=\\\"accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture\\\" allowfullscreen></iframe></body></html>";
           // wvAdsContent.loadUrl("https://www.youtube.com/watch?v=7bDLIV96LD4");

        }
    }
}

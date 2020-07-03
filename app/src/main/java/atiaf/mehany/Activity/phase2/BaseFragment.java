package atiaf.mehany.Activity.phase2;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import atiaf.mehany.R;
import atiaf.mehany.phase2.ApiManager;

public abstract class BaseFragment extends Fragment {

    public ApiManager apiManager;
    public ProgressDialog progressDialog ;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager();
        setProgress();
    }
    private void setProgress(){
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle(getString(R.string.wait));
        progressDialog.setMessage(getString(R.string.che));
    }

    public abstract void initViews();
}

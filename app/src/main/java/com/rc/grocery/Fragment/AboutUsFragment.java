package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;

import static com.rc.grocery.util.AllConstant.ASSET_FILE_ABOUT_US;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class AboutUsFragment extends Fragment {

    private static String TAG = AboutUsFragment.class.getSimpleName();
    private PDFView pdfView;

    public AboutUsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_us, container, false);

        String title = getArguments().getString("title");
        ((MainActivity) getActivity()).setTitle(title);

        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        pdfView.fromAsset(ASSET_FILE_ABOUT_US)
                .enableSwipe(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();

        return view;
    }
}
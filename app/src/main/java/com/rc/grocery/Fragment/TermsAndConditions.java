package com.rc.grocery.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;

import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;

import static com.rc.grocery.util.AllConstant.ASSET_FILE_TERMS_AND_CONDITIONS;

/**
 * @author Md. Rashadul Alam
 * Email: rashed.droid@gmail.com
 */
public class TermsAndConditions extends Fragment {

    private static String TAG = TermsAndConditions.class.getSimpleName();
    private PDFView pdfView;

    public TermsAndConditions() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);

        String title = getArguments().getString("title");
        ((MainActivity) getActivity()).setTitle(title);

        pdfView = (PDFView) view.findViewById(R.id.pdfView);
        pdfView.fromAsset(ASSET_FILE_TERMS_AND_CONDITIONS)
                .enableSwipe(true)
                .enableDoubletap(true)
                .defaultPage(0)
                .load();

        return view;
    }
}
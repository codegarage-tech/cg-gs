package com.rc.grocery.Fragment;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.rc.grocery.activity.MainActivity;
import com.rc.grocery.R;

import static com.rc.grocery.util.AllConstant.SUPPORT_CELL;
import static com.rc.grocery.util.AllConstant.SUPPORT_EMAIL;
import static com.rc.grocery.util.AppUtil.isSimSupport;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class SupportInfoFragment extends Fragment {

    private static String TAG = SupportInfoFragment.class.getSimpleName();
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 420;

    public SupportInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_support_info, container, false);

        String title = getArguments().getString("title");

        ((MainActivity) getActivity()).setTitle(title);

        ((Button) view.findViewById(R.id.btn_support_call)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_PERMISSIONS);
                        return;
                    }
                }
                callPhone();
            }
        });
        ((Button) view.findViewById(R.id.btn_support_email)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_SENDTO);
                callIntent.setData(Uri.parse("mailto:" + SUPPORT_EMAIL));
                startActivity(callIntent);
            }
        });

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callPhone();
                } else {
                    Toast.makeText(getActivity(), "Call permission is not granted", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void callPhone() {
        if (isSimSupport(getActivity())) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + SUPPORT_CELL));
            startActivity(callIntent);
        } else {
            Toast.makeText(getActivity(), getString(R.string.toast_your_sim_card_is_absent), Toast.LENGTH_SHORT).show();
        }
    }
}
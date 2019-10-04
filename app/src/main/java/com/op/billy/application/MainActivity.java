package com.op.billy.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static ContentResolver mResolver;
    private final String AUTHORITY = "com.op.billy.dict.provider";
    private final String DICT_PATH = "dict";
    private final String DICT__DESCRIBE_PATH = "describe";
    private final Uri DICT_URI = Uri.parse("content://" + AUTHORITY + File.separatorChar + DICT_PATH);
    private final Uri DICT_DESCRIBE_URI = Uri.parse("content://" + AUTHORITY + File.separatorChar + DICT__DESCRIBE_PATH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResolver = getContentResolver();
        mResolver.query(DICT_URI, new String[]{""}, "", new String[]{""}, "");
    }

    @Override
    protected void onResume() {
        mResolver.query(DICT_URI, new String[]{""}, "", new String[]{""}, "");
        super.onResume();
    }
}

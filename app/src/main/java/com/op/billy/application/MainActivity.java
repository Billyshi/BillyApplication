package com.op.billy.application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static ContentResolver mResolver;
    private final String AUTHORITY = "com.op.billy.dict.provider";
    private final String DICT_PATH = "dict";
    private final String DICT__DESCRIBE_PATH = "describe";
    private final String COLUMN_NAME_DICT = "word";
    private final String COLUMN_NAME_DESCRIBE = "describe";
    private final Uri URI_DICT = Uri.parse("content://" + AUTHORITY + File.separatorChar + DICT_PATH);
    private final Uri URI_DICT_DESCRIBE = Uri.parse("content://" + AUTHORITY + File.separatorChar + DICT__DESCRIBE_PATH);

    Button mBtInsert;
    Button mBtModify;
    Button mBtDelete;
    Button mBtQuery;
    TextView mTvShow;
    EditText mEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mResolver = getContentResolver();

        mBtInsert = findViewById(R.id.bt_insert);
        mBtDelete = findViewById(R.id.bt_delete);
        mBtModify = findViewById(R.id.bt_modify);
        mBtQuery = findViewById(R.id.bt_query);
        mTvShow = findViewById(R.id.tv_show);
        mEditText = findViewById(R.id.et_inpput_content);

        mBtInsert.setOnClickListener(this);
        mBtDelete.setOnClickListener(this);
        mBtModify.setOnClickListener(this);
        mBtQuery.setOnClickListener(this);
        mTvShow.setOnClickListener(this);
        mEditText.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private String[] mProjections = {COLUMN_NAME_DESCRIBE,COLUMN_NAME_DICT };

    @Override
    public void onClick(View v) {
        ContentValues cv = new ContentValues();
        switch (v.getId()) {
            case R.id.bt_insert :
                String input = mEditText.getText().toString();
                if(TextUtils.isEmpty(input)) {
                    return;
                }

                //传递的cv,key是数据库表列的名字，value是列能接收的值，否则报错
                cv.put(COLUMN_NAME_DICT, input);
                mResolver.insert(URI_DICT, cv);
                mEditText.setText("");
                break;
            case R.id.bt_modify:
                String input_describe = mEditText.getText().toString();
                cv.put(COLUMN_NAME_DESCRIBE,input_describe);
                String where = "word like ?";//"word like '%b'";//如果使用？做占位符，那么下面再传入模糊查询的字符串时，不在需要单引号
                String[] selectionArgs_update = {"%b"};
                mResolver.update(URI_DICT_DESCRIBE, cv, where, selectionArgs_update);
                mEditText.setText("");
                break;
            case R.id.bt_query:
                //projection :想要查询的列的的集合（传入列名），可以使单列或多列
                String selection = "word like '%b'"; //查询的选择条件， 具体值用占位符 ？ 表示
                //查询条件中的具体值，代替上述的占位符， a%表示以a开头， %b表示以b结尾， %c%表示包含c
                String[] selectionArgs =  {" a% ", " %b "};
                //查询单词以a开头的，描述以b结尾的 行
                Cursor cursor = mResolver.query(URI_DICT, mProjections, null, null, null);
                if(cursor != null &&cursor.getCount() > 0) {
                    Toast.makeText(MainActivity.this, "query", Toast.LENGTH_SHORT).show();
                    int count = cursor.getCount();
                    String result = "result count= " + count +"\n";
                    while(cursor.moveToNext()) {
                        result += "单词： " +cursor.getString(1)+"， 单词描述： " + cursor.getString(0) +"\n";
                    }
                    mTvShow.setText(result);
                }
                break;
            case R.id.bt_delete:
                String where2 = "word like 'a%'";//如果传入完整的where语句，则不需要再传入selectionArgs，注意这里模糊查询需要单引号
                mResolver.delete(URI_DICT, where2, null);
                break;
            case R.id.et_inpput_content:
                break;
            case R.id.tv_show:
                break;
            default:
        }
    }
}

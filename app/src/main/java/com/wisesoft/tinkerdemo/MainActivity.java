package com.wisesoft.tinkerdemo;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.tinker.lib.tinker.Tinker;
import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.tencent.tinker.loader.shareutil.ShareTinkerInternals;
import com.wisesoft.tinkerdemo.tinker.util.Utils;

import rx.functions.Action1;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private TextView mTextView;
    private Button mBtnLoad;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermissions();
        initView();
//        mTextView.setText("new");
    }

    private void checkPermissions() {
        RxPermissions permissions = new RxPermissions(this);
        permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (!aBoolean)
                            Toast.makeText(MainActivity.this, "没有权限，补丁加载不成功", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTextView = (TextView) findViewById(R.id.textView);
        mBtnLoad = (Button) findViewById(R.id.btn_load);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mBtnLoad.setOnClickListener(this);
        mFab.setOnClickListener(this);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_load:
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(), Environment.getExternalStorageDirectory().getAbsolutePath() + "/patch_signed_7zip.apk");
                break;
            case R.id.fab:
                showInfo();
                break;
        }
    }

    private void showInfo() {
        final StringBuilder sb = new StringBuilder();
        Tinker tinker = Tinker.with(getApplicationContext());
        if (tinker.isTinkerLoaded()) {
            sb.append(String.format("[patch is loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName(ShareConstants.TINKER_ID)));
            sb.append(String.format("[packageConfig patchMessage] %s \n", tinker.getTinkerLoadResultIfPresent().getPackageConfigByName("patchMessage")));
            sb.append(String.format("[TINKER_ID Rom Space] %d k \n", tinker.getTinkerRomSpace()));

        } else {
            sb.append(String.format("[patch is not loaded] \n"));
            sb.append(String.format("[buildConfig TINKER_ID] %s \n", BuildInfo.TINKER_ID));
            sb.append(String.format("[buildConfig BASE_TINKER_ID] %s \n", BaseBuildInfo.BASE_TINKER_ID));

            sb.append(String.format("[buildConfig MESSSAGE] %s \n", BuildInfo.MESSAGE));
            sb.append(String.format("[TINKER_ID] %s \n", ShareTinkerInternals.getManifestTinkerID(getApplicationContext())));
        }
        sb.append(String.format("[BaseBuildInfo Message] %s \n", BaseBuildInfo.TEST_MESSAGE));

        mTextView.setText(sb.toString());
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utils.setBackground(false);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.setBackground(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareTinkerInternals.killAllOtherProcess(getApplicationContext());
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}

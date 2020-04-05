package net.qiujuer.lesson.socketdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private UDPProvider.Provider mProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 生成设备唯一标示
        String sn = UUID.randomUUID().toString();
        mProvider = new UDPProvider.Provider(sn);
        mProvider.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mProvider.exit();
    }
}

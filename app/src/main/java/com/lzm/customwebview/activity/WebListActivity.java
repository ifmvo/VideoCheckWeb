package com.lzm.customwebview.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.lzm.customwebview.R;

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew Chen on 2019-09-06.
 */
public class WebListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_list);

        EditText edUrl = findViewById(R.id.et_url);
        Button btnAction = findViewById(R.id.btn_action);

        btnAction.setOnClickListener(view -> {
            String url = edUrl.getText().toString();
            MainActivity.action(WebListActivity.this, url);
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fl_fg_container, new WebListFragment());
        fragmentTransaction.commit();
    }

    private long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time > 1500) {
            time = System.currentTimeMillis();
            Toast.makeText(this, "再按一次", Toast.LENGTH_SHORT).show();
            return;
        }

        super.onBackPressed();
    }
}

package com.example.btl_android;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    FrameLayout frameLayout;
    BottomNavigationView menuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mappingID();

        replaceFragment(new HomeFragment());

        menuView.setOnItemSelectedListener(item -> {

            int ID = item.getItemId();
            if(ID == R.id.home){
                replaceFragment(new HomeFragment());
            } else if (ID == R.id.search) {
                replaceFragment(new SearchFragment());
            } else if (ID == R.id.love) {
                replaceFragment(new LoveFragment());
            } else if (ID == R.id.user) {
                replaceFragment(new UserFragment());
            }
            return true;
        });

    }



    private void mappingID(){
        frameLayout = findViewById(R.id.frameLayout);
        menuView = findViewById(R.id.bottomNavigationView);
    }

    /**
     * Thay thế một Fragment hiện tại bằng một Fragment mới trong container.
     *
     *  fragment Fragment mới sẽ thay thế Fragment hiện tại.
     */
    private void replaceFragment(Fragment fragment){
        // Lấy đối tượng FragmentManager để quản lý các Fragment trong Activity.
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Bắt đầu một giao dịch Fragment.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Thay thế Fragment hiện tại trong container có ID R.id.farmeLayout bằng Fragment mới.
        // Đảm bảo R.id.farmeLayout là ID của một ViewGroup trong layout của Activity.
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        // Cam kết (commit) giao dịch, áp dụng thay đổi và thực hiện thay thế Fragment.
        fragmentTransaction.commit();
    }
}
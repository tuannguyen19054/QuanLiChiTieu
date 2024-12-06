package com.example.android_quanlychitieu;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class tablayout_khoanchi extends AppCompatActivity {
    private TabLayout mtabLayout;
    private ViewPager mviewPage;
    private DrawerLayout drawerLayout;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablayout_khoanchi);

        drawerLayout = findViewById(R.id.drawer_layout);
        mtabLayout = findViewById(R.id.tab_layout);
        mviewPage = findViewById(R.id.viewpager);

        // Nhận user_id từ Intent (được truyền qua từ màn hình đăng ký/đăng nhập)
        userId = getIntent().getIntExtra("user_id", -1);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
        // Gán name vào text để hiển thị
        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.username);
        String username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            username = "admin";
        }
        usernameTextView.setText(username);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_khoanthu) {
                    setupTabLayoutKhoanthu(); // Khi chọn "Khoản Thu"
                } else if (id == R.id.nav_khoanchi) {
                    setupTabLayoutKhoanchi(); // Khi chọn "Khoản Chi"
                } else if (id == R.id.nav_thongke) {
                    setupTabLayoutThongke(); // Khi chọn "Thông Kê"
                } else if (id == R.id.nav_thoat) {
                    Intent intent = new Intent(tablayout_khoanchi.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish(); // Kết thúc activity hiện tại
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        // Mặc định hiển thị các tab cho Khoản Chi
        setupTabLayoutKhoanthu();
    }

    private void setupTabLayoutKhoanchi() {
        List<Fragment> fragments = new ArrayList<>();

        KhoanchiFragment khoanchiFragment = KhoanchiFragment.newInstance(userId); // Truyền user_id vào khoanthuFragment

        fragments.add(khoanchiFragment); // Khoản Thu

        LoaichiFragment loaichiFragment = LoaichiFragment.newInstance(userId); // Truyền user_id vào LoaithuFragment
        fragments.add(loaichiFragment); // Loại Thu

        List<String> titles = new ArrayList<>();
        titles.add("Khoản Chi");
        titles.add("Loại Chi");

        setupViewPager(fragments, titles);
    }

    private void setupTabLayoutKhoanthu() {
        List<Fragment> fragments = new ArrayList<>();

        // Truyền userId vào LoaithuFragment
        KhoanthuFragment khoanthuFragment = KhoanthuFragment.newInstance(userId); // Truyền user_id vào khoanthuFragment

        fragments.add(khoanthuFragment); // Khoản Thu
        LoaithuFragment loaithuFragment = LoaithuFragment.newInstance(userId); // Truyền user_id vào LoaithuFragment
        fragments.add(loaithuFragment); // Loại Thu

        List<String> titles = new ArrayList<>();
        titles.add("Khoản Thu");
        titles.add("Loại Thu");

        setupViewPager(fragments, titles);
    }

    private void setupTabLayoutThongke() {
        List<Fragment> fragments = new ArrayList<>();
        ThongkeThuFragment thongkeThuFragment = ThongkeThuFragment.newInstance(userId);

        fragments.add(thongkeThuFragment); // Khoản Thu

        ThongkeChiFragment thongkeChiFragment = ThongkeChiFragment.newInstance(userId);

        fragments.add(thongkeChiFragment); // Khoản Thu
        List<String> titles = new ArrayList<>();
        titles.add("Thông Kê Thu");
        titles.add("Thống Kê Chi");

        setupViewPager(fragments, titles);
    }

    private void setupViewPager(List<Fragment> fragments, List<String> titles) {
        ViewPage viewPageAdapter = new ViewPage(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments, titles);

        mviewPage.setAdapter(viewPageAdapter);
        viewPageAdapter.notifyDataSetChanged();
        mtabLayout.setupWithViewPager(mviewPage);
    }
}

package android.jlu.com.municipalmanage.activity;

import android.jlu.com.municipalmanage.R;
import android.jlu.com.municipalmanage.base.AppActivity;
import android.jlu.com.municipalmanage.base.BaseFragment;
import android.jlu.com.municipalmanage.fragment.ContactsFragment;
import android.jlu.com.municipalmanage.fragment.HomeFragment;
import android.jlu.com.municipalmanage.fragment.TasksFragment;
import android.jlu.com.municipalmanage.fragment.UserMainFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {

    private ArrayList<Fragment> fragmentLists;
    private BadgeItem mTaskNumberBadgeItem;

    private BottomNavigationItem mHomeBtnItem, mUserMainBtnItem,
            mTaskBtnItem, mContactsBtnItem;

    private BottomNavigationBar mBottomNavigationBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();

        initData();

    }

    private void initView() {
        setContentView(R.layout.activity_main);

        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);

        //设置导航栏模式
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        //设置导航栏背景模式
        mBottomNavigationBar
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC
                );

        mHomeBtnItem = new BottomNavigationItem(
                R.drawable.home, "Home").setActiveColorResource(R.color.orange);
        mTaskBtnItem = new BottomNavigationItem(
                R.drawable.tasks, "Task").setActiveColorResource(R.color.blue);
        mContactsBtnItem = new BottomNavigationItem(
                android.R.drawable.ic_menu_call, "Contacts").setActiveColorResource(R.color.brown);
        mUserMainBtnItem = new BottomNavigationItem(
                R.drawable.usermain, "UserMain").setActiveColorResource(R.color.grey);

        mTaskNumberBadgeItem = new BadgeItem()
                .setBorderWidth(4)
                .setBackgroundColorResource(R.color.red)
                .setText("5")
                .setHideOnSelect(true);

        mTaskBtnItem.setBadgeItem(mTaskNumberBadgeItem);

        mBottomNavigationBar.addItem(mHomeBtnItem)
                .addItem(mTaskBtnItem)
                .addItem(mContactsBtnItem)
                .addItem(mUserMainBtnItem)
                .setFirstSelectedPosition(0)
                .initialise();

        mBottomNavigationBar.setTabSelectedListener(this);

    }

    private void initData() {
        fragmentLists = getFragments();
        setDefaultFragment();

    }

    /**
     * 设置默认的
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.layFrame, fragmentLists.get(0));
        transaction.commit();
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();

        fragments.add(HomeFragment.newInstance("Home"));
        fragments.add(ContactsFragment.newInstance("Contacts"));
        fragments.add(TasksFragment.newInstance("Records"));
        fragments.add(UserMainFragment.newInstance("UserMain"));
        return fragments;
    }

    @Override
    public void onTabSelected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.layFrame, fragment);
                } else {
                    ft.add(R.id.layFrame, fragment);
                }
                ft.commitAllowingStateLoss();
                if (position == 1) {
                    BadgeItem badgeItem = mTaskNumberBadgeItem.hide().setText("0");
                    mTaskBtnItem.setBadgeItem(badgeItem);
                }
            }
        }
    }

    @Override
    public void onTabUnselected(int position) {
        if (fragmentLists != null) {
            if (position < fragmentLists.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = fragmentLists.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();

//                Toast.makeText(MainActivity.this, "onTabUnselected: " + position, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {
//        Toast.makeText(MainActivity.this, "onTabReselected: " + position, Toast.LENGTH_SHORT).show();
    }
}

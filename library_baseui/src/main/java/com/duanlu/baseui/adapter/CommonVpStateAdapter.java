package com.duanlu.baseui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/********************************
 * @name CommonVpStateAdapter
 * @author 段露
 * @createDate 2019/3/6  16:38.
 * @updateDate 2019/3/6  16:38.
 * @version V1.0.0
 * @describe ViewPager—Fragment适配器公共适配器(不保存状态).
 ********************************/
public class CommonVpStateAdapter extends FragmentStatePagerAdapter {

    private String[] mTitles;
    private List<Fragment> mFragments;

    public CommonVpStateAdapter(FragmentManager fm) {
        this(fm, "");
    }

    public CommonVpStateAdapter(FragmentManager fm, String... titles) {
        this(fm, null, titles);
    }

    public CommonVpStateAdapter(FragmentManager fm, List<Fragment> fragments, String... titles) {
        super(fm);
        this.mFragments = new ArrayList<>();
        this.setTitles(titles);
        this.addFragments(fragments);
    }

    public String[] getTitles() {
        return this.mTitles;
    }

    public void setTitles(String[] titles) {
        this.mTitles = titles;
    }

    public List<Fragment> getFragments() {
        return this.mFragments;
    }

    public void addFragment(Fragment fragment) {
        if (null != fragment) {
            this.mFragments.add(fragment);
        }
    }

    public void addFragments(List<Fragment> fragments) {
        if (null != fragments && !fragments.isEmpty()) {
            this.mFragments.addAll(fragments);
        }
    }

    public void addFragments(Fragment... fragments) {
        if (null != fragments && fragments.length > 0) {
            Collections.addAll(this.mFragments, fragments);
        }
    }

    public Fragment getItem(int position) {
        return (Fragment) this.mFragments.get(position);
    }

    public int getCount() {
        return this.mFragments.size();
    }

    public int getItemPosition(Object object) {
        return -2;
    }

    public CharSequence getPageTitle(int position) {
        return null != this.mTitles && this.mTitles.length > 0 ? this.mTitles[position] : "";
    }
}

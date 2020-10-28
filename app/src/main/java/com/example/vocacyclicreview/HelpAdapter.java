package com.example.vocacyclicreview;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class HelpAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mData;

    public HelpAdapter(@NonNull FragmentManager fm) {
        super(fm);

        mData = new ArrayList<>();
        mData.add(new FragmentHelpIntroduce());
        mData.add(new FragmentHelpVocagroup());
        mData.add(new FragmentHelpVoca());
        mData.add(new FragmentHelpLearningcycle());
        mData.add(new FragmentHelpSyncBackup());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mData.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }
}

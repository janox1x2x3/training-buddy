package sk.android.training.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

import sk.android.training.R;

public class MyTabsListener implements ActionBar.TabListener {

    private Fragment fragment;

    public MyTabsListener(Fragment fragment) {

        this.fragment = fragment;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        ft.add(R.id.fragment_place, fragment, null);
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        ft.remove(fragment);
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub


    }

}
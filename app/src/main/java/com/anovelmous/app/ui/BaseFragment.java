package com.anovelmous.app.ui;

import android.app.Activity;
import android.net.Uri;

import com.fizzbuzz.android.dagger.InjectingFragment;

/**
 * Created by Greg Ziegan on 6/20/15.
 */
public abstract class BaseFragment extends InjectingFragment {
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

}

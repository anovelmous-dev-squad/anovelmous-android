package com.anovelmous.app.ui.reading;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anovelmous.app.R;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.melnykov.fab.FloatingActionButton;

import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReadingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ReadingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReadingFragment extends Fragment implements ObservableScrollViewCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ObservableScrollView scrollView;
    private FloatingActionButton mFab;
    private Toolbar toolbar;
    private int mFabMargin;
    private boolean mFabIsShown;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReadingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReadingFragment newInstance(String param1, String param2) {
        ReadingFragment fragment = new ReadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mFabMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin);
        mFabIsShown = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        toolbar = (Toolbar) getView().findViewById(R.id.app_toolbar);
        scrollView = (ObservableScrollView) getView().findViewById(R.id.scrollable);
        scrollView.setScrollViewCallbacks(this);
        mFab = (FloatingActionButton) getView().findViewById(R.id.fab);

        ScrollUtils.addOnGlobalLayoutListener(mFab, new Runnable() {
            @Override
            public void run() {
                float fabTranslationY = getView().getHeight() - mFabMargin - mFab.getHeight();
                mFab.setTranslationX(getView().getWidth() - mFabMargin - mFab.getWidth());
                mFab.setTranslationY(fabTranslationY);
            }
        });
        return inflater.inflate(R.layout.fragment_reading, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onScrollChanged(int i, boolean b, boolean b1) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        Timber.d("DEBUG: onUpOrCancelMotionEvent: " + scrollState);
        if (scrollState == ScrollState.UP) {
            hideFab();
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            showFab();
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return toolbar.getTranslationY() == 0;
    }

    private boolean toolbarIsHidden() {
        return toolbar.getTranslationY() == -toolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-toolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (toolbar.getTranslationY() == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(toolbar.getTranslationY(), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                toolbar.setTranslationY(translationY);
                (scrollView).setTranslationY(translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) (scrollView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                (scrollView).requestLayout();
            }
        });
        animator.start();
    }

    private int getScreenHeight() {
        return getView().getRootView().getHeight();
    }

    private void showFab() {
        if (!mFabIsShown) {
            mFab.animate().cancel();
            mFab.animate().scaleX(1).scaleY(1).setDuration(200).start();
            mFabIsShown = true;
        }
    }

    private void hideFab() {
        if (mFabIsShown) {
            mFab.animate().cancel();
            mFab.animate().scaleX(0).scaleY(0).setDuration(200).start();
            mFabIsShown = false;
        }
    }

}

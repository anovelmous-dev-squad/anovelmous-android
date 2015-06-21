package com.anovelmous.app.ui;

import android.app.Activity;
import android.net.Uri;
import android.support.v4.app.Fragment;

import com.anovelmous.app.InjectingFragmentModule;
import com.anovelmous.app.Injector;
import com.anovelmous.app.data.api.AnovelmousService;
import com.anovelmous.app.data.api.NetworkService;
import com.anovelmous.app.data.api.PersistenceService;
import com.anovelmous.app.data.api.RestService;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.ObjectGraph;

import static com.anovelmous.app.util.Preconditions.checkState;

/**
 * Created by Greg Ziegan on 6/20/15.
 */
public class BaseFragment extends Fragment implements Injector {
    private ObjectGraph mObjectGraph;
    private boolean mFirstAttach = true;

    private OnFragmentInteractionListener mListener;
    protected RestService restService;

    @Inject NetworkService networkService;
    @Inject PersistenceService persistenceService;

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

        ObjectGraph appGraph = ((Injector) activity).getObjectGraph();
        List<Object> fragmentModules = getModules();
        mObjectGraph = appGraph.plus(fragmentModules.toArray());

        if (mFirstAttach) {
            inject(this);
            mFirstAttach = false;
        }

        restService = new AnovelmousService(networkService, persistenceService);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        mObjectGraph = null;
        super.onDestroy();
    }

    @Override
    public final ObjectGraph getObjectGraph() {
        return mObjectGraph;
    }

    @Override
    public void inject(Object target) {
        checkState(mObjectGraph != null, "object graph must be assigned prior to calling inject");
        mObjectGraph.inject(target);
    }

    protected List<Object> getModules() {
        List<Object> result = new ArrayList<>();
        result.add(new InjectingFragmentModule(this, this));
        return result;
    }

}

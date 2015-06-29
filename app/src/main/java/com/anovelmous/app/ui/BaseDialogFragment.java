package com.anovelmous.app.ui;

import android.app.Activity;
import android.support.v4.app.DialogFragment;

import com.anovelmous.app.InjectingFragmentModule;
import com.anovelmous.app.Injector;

import java.util.ArrayList;
import java.util.List;

import dagger.ObjectGraph;

import static com.anovelmous.app.util.Preconditions.checkState;

/**
 * Created by Greg Ziegan on 6/28/15.
 */
public abstract class BaseDialogFragment extends DialogFragment implements Injector {
    private ObjectGraph mObjectGraph;
    private boolean mFirstAttach = true;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        ObjectGraph appGraph = ((Injector) activity).getObjectGraph();
        List<Object> fragmentModules = getModules();
        mObjectGraph = appGraph.plus(fragmentModules.toArray());

        if (mFirstAttach) {
            inject(this);
            mFirstAttach = false;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

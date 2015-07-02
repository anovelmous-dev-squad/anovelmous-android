package com.anovelmous.app.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anovelmous.app.R;
import com.anovelmous.app.data.api.RestService;
import com.anovelmous.app.data.api.model.RestVerb;
import com.anovelmous.app.data.api.resource.Contributor;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.fizzbuzz.android.dagger.InjectingDialogFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class LoginFragment extends InjectingDialogFragment {
    private static final String DIALOG_TITLE = "com.anovelmous.app.ui.LoginFragment.DIALOG_TITLE";

    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        void onSuccessfulLogin(Contributor contributor);
    }

    @Inject RestService restService;

    @InjectView(R.id.fb_login_button) LoginButton loginButton;
    CallbackManager callbackManager;
    Contributor me;

    private String title;

    public static LoginFragment newInstance(String title) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(DIALOG_TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(DIALOG_TITLE);
        }
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);

        getDialog().setTitle(title);

        loginButton.setReadPermissions(Arrays.asList(new String [] {"public_profile", "email"}));
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        try {
                            String username = jsonObject.getString("id");
                            String email = jsonObject.getString("email");
                            createAndPersistNewUser(username, email, loginResult.getAccessToken().getToken());
                        } catch (JSONException e) {
                            Timber.e(e.getMessage());
                        }
                    }
                }).executeAsync();
            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onError(FacebookException e) {

            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement LoginFragment.OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void createAndPersistNewUser(String username, String email, String accessToken) {
        me = new Contributor.Builder()
                .restVerb(RestVerb.POST)
                .fbAccessToken(accessToken)
                .username(username)
                .email(email)
                .groups(new ArrayList<String>())
                .build();
        restService.createContributor(me)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<Contributor>() {
                @Override
                public void call(Contributor contributor) {
                    me = contributor;
                    mListener.onSuccessfulLogin(contributor);
                }
            });
    }
}

package com.anovelmous.app.ui.debug;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.anovelmous.app.AnovelmousApp;
import com.anovelmous.app.BuildConfig;
import com.anovelmous.app.R;
import com.anovelmous.app.data.AnimationSpeed;
import com.anovelmous.app.data.ApiEndpoint;
import com.anovelmous.app.data.ApiEndpoints;
import com.anovelmous.app.data.CaptureIntents;
import com.anovelmous.app.data.IsMockMode;
import com.anovelmous.app.data.LumberYard;
import com.anovelmous.app.data.PixelGridEnabled;
import com.anovelmous.app.data.PixelRatioEnabled;
import com.anovelmous.app.data.ScalpelEnabled;
import com.anovelmous.app.data.ScalpelWireframeEnabled;
import com.anovelmous.app.data.SeenDebugDrawer;
import com.anovelmous.app.data.api.MockNetworkService;
import com.anovelmous.app.data.api.MockNovelsResponse;
import com.anovelmous.app.data.prefs.BooleanPreference;
import com.anovelmous.app.data.prefs.IntPreference;
import com.anovelmous.app.data.prefs.NetworkProxyPreference;
import com.anovelmous.app.data.prefs.StringPreference;
import com.anovelmous.app.ui.AppContainer;
import com.anovelmous.app.ui.novels.NovelSelectActivity;
import com.anovelmous.app.ui.bugreport.BugReportLens;
import com.anovelmous.app.ui.logs.LogsDialog;
import com.anovelmous.app.ui.misc.EnumAdapter;
import com.anovelmous.app.util.Keyboards;
import com.anovelmous.app.util.Strings;
import com.jakewharton.madge.MadgeFrameLayout;
import com.jakewharton.scalpel.ScalpelFrameLayout;
import com.mattprecious.telescope.TelescopeLayout;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.lang.reflect.Method;
import java.net.Proxy;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Set;
import java.util.TimeZone;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.MockRestAdapter;
import retrofit.RestAdapter;
import timber.log.Timber;

import static android.content.Context.POWER_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.os.PowerManager.ACQUIRE_CAUSES_WAKEUP;
import static android.os.PowerManager.FULL_WAKE_LOCK;
import static android.os.PowerManager.ON_AFTER_RELEASE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
import static butterknife.ButterKnife.findById;

/**
 * Created by Greg Ziegan on 5/31/15.
 */
@Singleton
public final class DebugAppContainer implements AppContainer {
    private static final DateFormat DATE_DISPLAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm a");

    private final OkHttpClient client;
    private final OkHttpClient apiClient;
    private final LumberYard lumberYard;
    private final boolean isMockMode;
    private final StringPreference networkEndpoint;
    private final NetworkProxyPreference networkProxy;
    private final IntPreference animationSpeed;
    private final BooleanPreference pixelGridEnabled;
    private final BooleanPreference pixelRatioEnabled;
    private final BooleanPreference captureIntents;
    private final BooleanPreference scalpelEnabled;
    private final BooleanPreference scalpelWireframeEnabled;
    private final BooleanPreference seenDebugDrawer;
    private final RestAdapter restAdapter;
    private final MockRestAdapter mockRestAdapter;
    private final MockNetworkService mockAnovelmousService;
    private final Application app;

    Activity activity;
    Context drawerContext;

    @Inject
    public DebugAppContainer(OkHttpClient client,
                             @Named("Api") OkHttpClient apiClient,
                             LumberYard lumberYard,
                             @IsMockMode boolean isMockMode,
                             @ApiEndpoint StringPreference networkEndpoint,
                             NetworkProxyPreference networkProxy,
                             @CaptureIntents BooleanPreference captureIntents,
                             @AnimationSpeed IntPreference animationSpeed,
                             @PixelGridEnabled BooleanPreference pixelGridEnabled,
                             @PixelRatioEnabled BooleanPreference pixelRatioEnabled,
                             @ScalpelEnabled BooleanPreference scalpelEnabled,
                             @ScalpelWireframeEnabled BooleanPreference scalpelWireframeEnabled,
                             @SeenDebugDrawer BooleanPreference seenDebugDrawer,
                             RestAdapter restAdapter,
                             MockNetworkService mockAnovelmousService,
                             MockRestAdapter mockRestAdapter,
                             Application app) {
        this.client = client;
        this.apiClient = apiClient;
        this.lumberYard = lumberYard;
        this.isMockMode = isMockMode;
        this.networkEndpoint = networkEndpoint;
        this.captureIntents = captureIntents;
        this.pixelGridEnabled = pixelGridEnabled;
        this.pixelRatioEnabled = pixelRatioEnabled;
        this.scalpelEnabled = scalpelEnabled;
        this.scalpelWireframeEnabled = scalpelWireframeEnabled;
        this.seenDebugDrawer = seenDebugDrawer;
        this.mockRestAdapter = mockRestAdapter;
        this.networkProxy = networkProxy;
        this.animationSpeed = animationSpeed;
        this.mockAnovelmousService = mockAnovelmousService;
        this.restAdapter = restAdapter;
        this.app = app;
    }

    @InjectView(R.id.debug_drawer_layout) DebugDrawerLayout drawerLayout;
    @InjectView(R.id.telescope_container)
    TelescopeLayout telescopeLayout;
    @InjectView(R.id.madge_container) MadgeFrameLayout madgeFrameLayout;
    @InjectView(R.id.debug_content) ScalpelFrameLayout content;

    @InjectView(R.id.debug_contextual_title)
    View contextualTitleView;
    @InjectView(R.id.debug_contextual_list)
    LinearLayout contextualListView;

    @InjectView(R.id.debug_network_endpoint)
    Spinner endpointView;
    @InjectView(R.id.debug_network_endpoint_edit) View endpointEditView;
    @InjectView(R.id.debug_network_delay) Spinner networkDelayView;
    @InjectView(R.id.debug_network_variance) Spinner networkVarianceView;
    @InjectView(R.id.debug_network_error) Spinner networkErrorView;
    @InjectView(R.id.debug_network_proxy) Spinner networkProxyView;
    @InjectView(R.id.debug_network_logging) Spinner networkLoggingView;

    @InjectView(R.id.debug_capture_intents)
    Switch captureIntentsView;
    @InjectView(R.id.debug_repositories_response) Spinner repositoriesResponseView;

    @InjectView(R.id.debug_ui_animation_speed) Spinner uiAnimationSpeedView;
    @InjectView(R.id.debug_ui_pixel_grid) Switch uiPixelGridView;
    @InjectView(R.id.debug_ui_pixel_ratio) Switch uiPixelRatioView;
    @InjectView(R.id.debug_ui_scalpel) Switch uiScalpelView;
    @InjectView(R.id.debug_ui_scalpel_wireframe) Switch uiScalpelWireframeView;

    @InjectView(R.id.debug_build_name)
    TextView buildNameView;
    @InjectView(R.id.debug_build_code) TextView buildCodeView;
    @InjectView(R.id.debug_build_sha) TextView buildShaView;
    @InjectView(R.id.debug_build_date) TextView buildDateView;

    @InjectView(R.id.debug_device_make) TextView deviceMakeView;
    @InjectView(R.id.debug_device_model) TextView deviceModelView;
    @InjectView(R.id.debug_device_resolution) TextView deviceResolutionView;
    @InjectView(R.id.debug_device_density) TextView deviceDensityView;
    @InjectView(R.id.debug_device_release) TextView deviceReleaseView;
    @InjectView(R.id.debug_device_api) TextView deviceApiView;

    @InjectView(R.id.debug_okhttp_cache_max_size) TextView okHttpCacheMaxSizeView;
    @InjectView(R.id.debug_okhttp_cache_write_error) TextView okHttpCacheWriteErrorView;
    @InjectView(R.id.debug_okhttp_cache_request_count) TextView okHttpCacheRequestCountView;
    @InjectView(R.id.debug_okhttp_cache_network_count) TextView okHttpCacheNetworkCountView;
    @InjectView(R.id.debug_okhttp_cache_hit_count) TextView okHttpCacheHitCountView;

    @Override public ViewGroup get(final Activity activity) {
        this.activity = activity;
        drawerContext = new ContextThemeWrapper(activity, R.style.Theme_Anovelmous_Debug);;

        riseAndShine(activity);

        activity.setContentView(R.layout.debug_activity_frame);

        // Manually find the debug drawer and inflate the drawer layout inside of it.
        ViewGroup drawer = findById(activity, R.id.debug_drawer);
        LayoutInflater.from(drawerContext).inflate(R.layout.debug_drawer_content, drawer);

        // Inject after inflating the drawer layout so its views are available to inject.
        ButterKnife.inject(this, activity);

        // Set up the contextual actions to watch views coming in and out of the content area.
        Set<ContextualDebugActions.DebugAction<?>> debugActions = Collections.emptySet();
        ContextualDebugActions contextualActions = new ContextualDebugActions(this, debugActions);
        content.setOnHierarchyChangeListener(HierarchyTreeChangeListener.wrap(contextualActions));

        drawerLayout.setDrawerShadow(R.drawable.debug_drawer_shadow, GravityCompat.END);
        drawerLayout.setDrawerListener(new DebugDrawerLayout.SimpleDrawerListener() {
            @Override public void onDrawerOpened(View drawerView) {
                refreshOkHttpCacheStats();
            }
        });

        TelescopeLayout.cleanUp(activity); // Clean up any old screenshots.
        telescopeLayout.setLens(new BugReportLens(activity, lumberYard));

        // If you have not seen the debug drawer before, show it with a message
        if (!seenDebugDrawer.get()) {
            drawerLayout.postDelayed(new Runnable() {
                @Override public void run() {
                    drawerLayout.openDrawer(GravityCompat.END);
                    Toast.makeText(drawerContext, R.string.debug_drawer_welcome, Toast.LENGTH_LONG).show();
                }
            }, 1000);
            seenDebugDrawer.set(true);
        }

        setupNetworkSection();
        setupMockBehaviorSection();
        setupBuildSection();
        setupDeviceSection();
        setupOkHttpCacheSection();

        return content;
    }

    private void setupNetworkSection() {
        final ApiEndpoints currentEndpoint = ApiEndpoints.from(networkEndpoint.get());
        final EnumAdapter<ApiEndpoints> endpointAdapter =
                new EnumAdapter<>(drawerContext, ApiEndpoints.class);
        endpointView.setAdapter(endpointAdapter);
        endpointView.setSelection(currentEndpoint.ordinal());
        endpointView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                ApiEndpoints selected = endpointAdapter.getItem(position);
                if (selected != currentEndpoint) {
                    if (selected == ApiEndpoints.CUSTOM) {
                        Timber.d("Custom network endpoint selected. Prompting for URL.");
                        showCustomEndpointDialog(currentEndpoint.ordinal(), "http://");
                    } else {
                        setEndpointAndRelaunch(selected.url);
                    }
                } else {
                    Timber.d("Ignoring re-selection of network endpoint %s", selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final NetworkDelayAdapter delayAdapter = new NetworkDelayAdapter(drawerContext);
        networkDelayView.setAdapter(delayAdapter);
        networkDelayView.setSelection(
                NetworkDelayAdapter.getPositionForValue(mockRestAdapter.getDelay()));
        networkDelayView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                long selected = delayAdapter.getItem(position);
                if (selected != mockRestAdapter.getDelay()) {
                    Timber.d("Setting network delay to %sms", selected);
                    mockRestAdapter.setDelay(selected);
                } else {
                    Timber.d("Ignoring re-selection of network delay %sms", selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final NetworkVarianceAdapter varianceAdapter = new NetworkVarianceAdapter(drawerContext);
        networkVarianceView.setAdapter(varianceAdapter);
        networkVarianceView.setSelection(
                NetworkVarianceAdapter.getPositionForValue(mockRestAdapter.getVariancePercentage()));
        networkVarianceView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int selected = varianceAdapter.getItem(position);
                if (selected != mockRestAdapter.getVariancePercentage()) {
                    Timber.d("Setting network variance to %s%%", selected);
                    mockRestAdapter.setVariancePercentage(selected);
                } else {
                    Timber.d("Ignoring re-selection of network variance %s%%", selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        final NetworkErrorAdapter errorAdapter = new NetworkErrorAdapter(drawerContext);
        networkErrorView.setAdapter(errorAdapter);
        networkErrorView.setSelection(
                NetworkErrorAdapter.getPositionForValue(mockRestAdapter.getErrorPercentage()));
        networkErrorView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int selected = errorAdapter.getItem(position);
                if (selected != mockRestAdapter.getErrorPercentage()) {
                    Timber.d("Setting network error to %s%%", selected);
                    mockRestAdapter.setErrorPercentage(selected);
                } else {
                    Timber.d("Ignoring re-selection of network error %s%%", selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        int currentProxyPosition = networkProxy.isSet() ? ProxyAdapter.PROXY : ProxyAdapter.NONE;
        final ProxyAdapter proxyAdapter = new ProxyAdapter(drawerContext, networkProxy);
        networkProxyView.setAdapter(proxyAdapter);
        networkProxyView.setSelection(currentProxyPosition);
        networkProxyView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position == ProxyAdapter.NONE) {
                    Timber.d("Clearing network proxy");
                    // TODO: Keep the custom proxy around so you can easily switch back and forth.
                    networkProxy.delete();
                    client.setProxy(null);
                    apiClient.setProxy(null);
                } else if (networkProxy.isSet() && position == ProxyAdapter.PROXY) {
                    Timber.d("Ignoring re-selection of network proxy %s", networkProxy.get());
                } else {
                    Timber.d("New network proxy selected. Prompting for host.");
                    showNewNetworkProxyDialog(proxyAdapter);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        // Only show the endpoint editor when a custom endpoint is in use.
        endpointEditView.setVisibility(currentEndpoint == ApiEndpoints.CUSTOM ? VISIBLE : GONE);

        if (currentEndpoint == ApiEndpoints.MOCK_MODE) {
            // Disable network proxy if we are in mock mode.
            networkProxyView.setEnabled(false);
            networkLoggingView.setEnabled(false);
        } else {
            // Disable network controls if we are not in mock mode.
            networkDelayView.setEnabled(false);
            networkVarianceView.setEnabled(false);
            networkErrorView.setEnabled(false);
        }

        // We use the JSON rest adapter as the source of truth for the log level.
        final EnumAdapter<RestAdapter.LogLevel> loggingAdapter = new EnumAdapter<>(drawerContext, RestAdapter.LogLevel.class);
        networkLoggingView.setAdapter(loggingAdapter);
        networkLoggingView.setSelection(restAdapter.getLogLevel().ordinal());
        networkLoggingView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                RestAdapter.LogLevel selected = loggingAdapter.getItem(position);
                if (selected != restAdapter.getLogLevel()) {
                    Timber.d("Setting logging level to %s", selected);
                    restAdapter.setLogLevel(selected);
                } else {
                    Timber.d("Ignoring re-selection of logging level " + selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @OnClick(R.id.debug_network_endpoint_edit) void onEditEndpointClicked() {
        Timber.d("Prompting to edit custom endpoint URL.");
        // Pass in the currently selected position since we are merely editing.
        showCustomEndpointDialog(endpointView.getSelectedItemPosition(), networkEndpoint.get());
    }

    private void setupMockBehaviorSection() {
        captureIntentsView.setEnabled(isMockMode);
        captureIntentsView.setChecked(captureIntents.get());
        captureIntentsView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Timber.d("Capture intents set to %s", b);
                captureIntents.set(b);
            }
        });

        configureResponseSpinner(repositoriesResponseView, MockNovelsResponse.class);
    }

    /**
     * Populates a {@code Spinner} with the values of an {@code enum} and binds it to the value set in
     * the mock service.
     */
    private <T extends Enum<T>> void configureResponseSpinner(Spinner spinner,
                                                              final Class<T> responseClass) {
        final EnumAdapter<T> adapter = new EnumAdapter<>(drawerContext, responseClass);
        spinner.setEnabled(isMockMode);
        spinner.setAdapter(adapter);
        spinner.setSelection(mockAnovelmousService.getResponse(responseClass).ordinal());
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, View view, int position,
                                                 long id) {
                T selected = adapter.getItem(position);
                if (selected != mockAnovelmousService.getResponse(responseClass)) {
                    Timber.d("Setting %s to %s", responseClass.getSimpleName(), selected);
                    mockAnovelmousService.setResponse(responseClass, selected);
                } else {
                    Timber.d("Ignoring re-selection of %s %s", responseClass.getSimpleName(), selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupUserInterfaceSection() {
        final AnimationSpeedAdapter speedAdapter = new AnimationSpeedAdapter(drawerContext);
        uiAnimationSpeedView.setAdapter(speedAdapter);
        final int animationSpeedValue = animationSpeed.get();
        uiAnimationSpeedView.setSelection(
                AnimationSpeedAdapter.getPositionForValue(animationSpeedValue));
        uiAnimationSpeedView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                int selected = speedAdapter.getItem(position);
                if (selected != animationSpeed.get()) {
                    Timber.d("Setting animation speed to %sx", selected);
                    animationSpeed.set(selected);
                    applyAnimationSpeed(selected);
                } else {
                    Timber.d("Ignoring re-selection of animation speed %sx", selected);
                }
            }

            @Override public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        // Ensure the animation speed value is always applied across app restarts.
        content.post(new Runnable() {
            @Override public void run() {
                applyAnimationSpeed(animationSpeedValue);
            }
        });

        boolean gridEnabled = pixelGridEnabled.get();
        madgeFrameLayout.setOverlayEnabled(gridEnabled);
        uiPixelGridView.setChecked(gridEnabled);
        uiPixelRatioView.setEnabled(gridEnabled);
        uiPixelGridView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("Setting pixel grid overlay enabled to " + isChecked);
                pixelGridEnabled.set(isChecked);
                madgeFrameLayout.setOverlayEnabled(isChecked);
                uiPixelRatioView.setEnabled(isChecked);
            }
        });

        boolean ratioEnabled = pixelRatioEnabled.get();
        madgeFrameLayout.setOverlayRatioEnabled(ratioEnabled);
        uiPixelRatioView.setChecked(ratioEnabled);
        uiPixelRatioView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("Setting pixel scale overlay enabled to " + isChecked);
                pixelRatioEnabled.set(isChecked);
                madgeFrameLayout.setOverlayRatioEnabled(isChecked);
            }
        });

        boolean scalpel = scalpelEnabled.get();
        content.setLayerInteractionEnabled(scalpel);
        uiScalpelView.setChecked(scalpel);
        uiScalpelWireframeView.setEnabled(scalpel);
        uiScalpelView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("Setting scalpel interaction enabled to " + isChecked);
                scalpelEnabled.set(isChecked);
                content.setLayerInteractionEnabled(isChecked);
                uiScalpelWireframeView.setEnabled(isChecked);
            }
        });

        boolean wireframe = scalpelWireframeEnabled.get();
        content.setDrawViews(!wireframe);
        uiScalpelWireframeView.setChecked(wireframe);
        uiScalpelWireframeView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Timber.d("Setting scalpel wireframe enabled to " + isChecked);
                scalpelWireframeEnabled.set(isChecked);
                content.setDrawViews(!isChecked);
            }
        });
    }

    @OnClick(R.id.debug_logs_show) void showLogs() {
        // Using activity here instead of drawerContext to get the light theme.
        new LogsDialog(activity, lumberYard).show();
    }

    private void setupBuildSection() {
        buildNameView.setText(BuildConfig.VERSION_NAME);
        buildCodeView.setText(String.valueOf(BuildConfig.VERSION_CODE));
        buildShaView.setText(BuildConfig.GIT_SHA);

        try {
            // Parse ISO8601-format time into local time.
            DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            inFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date buildTime = inFormat.parse(BuildConfig.BUILD_TIME);
            buildDateView.setText(DATE_DISPLAY_FORMAT.format(buildTime));
        } catch (ParseException e) {
            throw new RuntimeException("Unable to decode build time: " + BuildConfig.BUILD_TIME, e);
        }
    }

    private void setupDeviceSection() {
        DisplayMetrics displayMetrics = drawerContext.getResources().getDisplayMetrics();
        String densityBucket = getDensityString(displayMetrics);
        deviceMakeView.setText(Strings.truncateAt(Build.MANUFACTURER, 20));
        deviceModelView.setText(Strings.truncateAt(Build.MODEL, 20));
        deviceResolutionView.setText(displayMetrics.heightPixels + "x" + displayMetrics.widthPixels);
        deviceDensityView.setText(displayMetrics.densityDpi + "dpi (" + densityBucket + ")");
        deviceReleaseView.setText(Build.VERSION.RELEASE);
        deviceApiView.setText(String.valueOf(Build.VERSION.SDK_INT));
    }

    private void setupOkHttpCacheSection() {
        Cache cache = client.getCache(); // Shares the cache with apiClient, so no need to check both.
        okHttpCacheMaxSizeView.setText(getSizeString(cache.getMaxSize()));

        refreshOkHttpCacheStats();
    }

    private void refreshOkHttpCacheStats() {
        Cache cache = client.getCache(); // Shares the cache with apiClient, so no need to check both.
        int writeTotal = cache.getWriteSuccessCount() + cache.getWriteAbortCount();
        int percentage = (int) ((1f * cache.getWriteAbortCount() / writeTotal) * 100);
        okHttpCacheWriteErrorView.setText(cache.getWriteAbortCount() + " / " + writeTotal + " (" + percentage + "%)");
        okHttpCacheRequestCountView.setText(String.valueOf(cache.getRequestCount()));
        okHttpCacheNetworkCountView.setText(String.valueOf(cache.getNetworkCount()));
        okHttpCacheHitCountView.setText(String.valueOf(cache.getHitCount()));
    }

    private void applyAnimationSpeed(int multiplier) {
        try {
            Method method = ValueAnimator.class.getDeclaredMethod("setDurationScale", float.class);
            method.invoke(null, (float) multiplier);
        } catch (Exception e) {
            throw new RuntimeException("Unable to apply animation speed.", e);
        }
    }

    private static String getDensityString(DisplayMetrics displayMetrics) {
        switch (displayMetrics.densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
                return "ldpi";
            case DisplayMetrics.DENSITY_MEDIUM:
                return "mdpi";
            case DisplayMetrics.DENSITY_HIGH:
                return "hdpi";
            case DisplayMetrics.DENSITY_XHIGH:
                return "xhdpi";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "xxhdpi";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "xxxhdpi";
            case DisplayMetrics.DENSITY_TV:
                return "tvdpi";
            default:
                return "unknown";
        }
    }

    private static String getSizeString(long bytes) {
        String[] units = new String[] { "B", "KB", "MB", "GB" };
        int unit = 0;
        while (bytes >= 1024) {
            bytes /= 1024;
            unit += 1;
        }
        return bytes + units[unit];
    }

    private void showNewNetworkProxyDialog(final ProxyAdapter proxyAdapter) {
        final int originalSelection = networkProxy.isSet() ? ProxyAdapter.PROXY : ProxyAdapter.NONE;

        View view = LayoutInflater.from(app).inflate(R.layout.debug_drawer_network_proxy, null);
        final EditText hostView = findById(view, R.id.debug_drawer_network_proxy_host);

        String host = networkProxy.get();
        if (!Strings.isBlank(host)) {
            hostView.setText(host); // Set the current host.
            hostView.setSelection(0, host.length()); // Pre-select it for editing.

            // Show the keyboard. Post this to the next frame when the dialog has been attached.
            hostView.post(new Runnable() {
                @Override public void run() {
                    Keyboards.showKeyboard(hostView);
                }
            });
        }

        new AlertDialog.Builder(drawerContext) //
                .setTitle("Set Network Proxy")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int i) {
                        networkProxyView.setSelection(originalSelection);
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int i) {
                        String theHost = hostView.getText().toString();
                        if (!Strings.isBlank(theHost)) {
                            networkProxy.set(theHost); // Persist across restarts.
                            proxyAdapter.notifyDataSetChanged(); // Tell the spinner to update.
                            networkProxyView.setSelection(ProxyAdapter.PROXY); // And show the proxy.

                            Proxy proxy = networkProxy.getProxy();
                            client.setProxy(proxy);
                            apiClient.setProxy(proxy);
                        } else {
                            networkProxyView.setSelection(originalSelection);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override public void onCancel(DialogInterface dialogInterface) {
                        networkProxyView.setSelection(originalSelection);
                    }
                })
                .show();
    }

    private void showCustomEndpointDialog(final int originalSelection, String defaultUrl) {
        View view = LayoutInflater.from(app).inflate(R.layout.debug_drawer_network_endpoint, null);
        final EditText url = findById(view, R.id.debug_drawer_network_endpoint_url);
        url.setText(defaultUrl);
        url.setSelection(url.length());

        new AlertDialog.Builder(drawerContext) //
                .setTitle("Set Network Endpoint")
                .setView(view)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int i) {
                        endpointView.setSelection(originalSelection);
                        dialog.cancel();
                    }
                })
                .setPositiveButton("Use", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int i) {
                        String theUrl = url.getText().toString();
                        if (!Strings.isBlank(theUrl)) {
                            setEndpointAndRelaunch(theUrl);
                        } else {
                            endpointView.setSelection(originalSelection);
                        }
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override public void onCancel(DialogInterface dialogInterface) {
                        endpointView.setSelection(originalSelection);
                    }
                })
                .show();
    }

    private void setEndpointAndRelaunch(String endpoint) {
        Timber.d("Setting network endpoint to %s", endpoint);
        networkEndpoint.set(endpoint);

        Intent newApp = new Intent(app, NovelSelectActivity.class);
        newApp.setFlags(FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NEW_TASK);
        app.startActivity(newApp);
        AnovelmousApp.get(app).buildObjectGraphAndInject();
    }

    /**
     * Show the activity over the lock-screen and wake up the device. If you launched the app manually
     * both of these conditions are already true. If you deployed from the IDE, however, this will
     * save you from hundreds of power button presses and pattern swiping per day!
     */
    public static void riseAndShine(Activity activity) {
        activity.getWindow().addFlags(FLAG_SHOW_WHEN_LOCKED);

        PowerManager power = (PowerManager) activity.getSystemService(POWER_SERVICE);
        PowerManager.WakeLock lock =
                power.newWakeLock(FULL_WAKE_LOCK | ACQUIRE_CAUSES_WAKEUP | ON_AFTER_RELEASE, "wakeup!");
        lock.acquire();
        lock.release();
    }
}

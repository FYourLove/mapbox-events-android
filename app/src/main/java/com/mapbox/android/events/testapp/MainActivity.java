package com.mapbox.android.events.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.android.telemetry.AppUserTurnstile;
import com.mapbox.android.telemetry.Event;
import com.mapbox.android.telemetry.MapboxTelemetry;

import java.util.List;

public class MainActivity extends AppCompatActivity implements PermissionsListener {
  private final String LOG_TAG = "MainActivity";
  private MapboxTelemetry mapboxTelemetry;
  private PermissionsManager permissionsManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    String accessTokenTelemetry = obtainAccessToken();
    String userAgentTelemetry = "MapboxEventsAndroid/3.1.0";
    mapboxTelemetry = new MapboxTelemetry(this, accessTokenTelemetry, userAgentTelemetry);

    checkPermissions();
  }

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    mapboxTelemetry.disable();
  }

  private String obtainAccessToken() {
    String accessToken = getString(R.string.mapbox_access_token);
    return accessToken;
  }

  private void checkPermissions() {
    boolean permissionsGranted = PermissionsManager.areLocationPermissionsGranted(this);

    if (permissionsGranted) {
      mapboxTelemetry.enable();
      pushTurnstile();
    } else {
      permissionsManager = new PermissionsManager(this);
      permissionsManager.requestLocationPermissions(this);
    }
  }

  @Override
  public void onExplanationNeeded(List<String> permissionsToExplain) {

  }

  @Override
  public void onPermissionResult(boolean granted) {
    if (granted) {
      mapboxTelemetry.enable();
      pushTurnstile();
    }
  }

  private void pushTurnstile() {
    Event event = new AppUserTurnstile("mapbox-events-android", "3.2.0");
    mapboxTelemetry.push(event);
  }
}

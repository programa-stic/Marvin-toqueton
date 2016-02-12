package ar.fsadosky.marvintoqueton.privacyhooks;

import java.lang.reflect.Method;

import android.app.PendingIntent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import android.os.Looper;
import android.util.Log;

import com.saurik.substrate.MS;

public class LocationHook extends PrivacyHook {

	public void hookgetLastKnownLocation() {
		Class<?>[] params = new Class[1];
		params[0] = String.class;
		hookAndSaveReturnValue("android.location.LocationManager",
				"getLastKnownLocation", params);
	}

	@Override
	protected Object convertFromProfile(String clazz, String method,
			String value) {
		if (method.equals("getLastKnownLocation")) {
			return getLocationIn(Double.parseDouble(value.split(",")[0]),
					Double.parseDouble(value.split(",")[1]), "");
		}
		return null;
	}

	private static FakeLocationListener fakeListener;

	public void hook() {
		hookgetLastKnownLocation();

		MS.hookClassLoad("android.location.LocationManager",
				new MS.ClassLoadHook() {
					public void classLoaded(Class<?> _clazz) {
						fakeListener = new FakeLocationListener();
						hookRequestUpdates();
					}
				});

	}

	protected static Location getLocationIn(double latitude, double longitude,
			String provider) {
		Location location = new Location(provider);
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setAltitude(0);
		location.setAccuracy(0.2F);
		location.setTime(System.currentTimeMillis());
		return location;
	}

	private void hookRequestUpdates() {
		Method method = null;
		Class locationManager = null;
		try {
			locationManager = Class.forName("android.location.LocationManager");
			Class locationRequest = Class
					.forName("android.location.LocationRequest");
			Class[] params = { locationRequest, LocationListener.class,
					Looper.class, PendingIntent.class };
			method = locationManager.getDeclaredMethod(
					"requestLocationUpdates", params);
		} catch (Exception e) {
			Log.d(Utils.ERROR, "No such method requestLocationUpdates");
		}

		if (method != null) {
			MS.hookMethod(locationManager, method,
					new MS.MethodAlteration<LocationManager, Void>() {
						public Void invoked(final LocationManager hooked,
								final Object... args) throws Throwable {

							try {
								String value = readValueFromProfile(
										"android.location.LocationManager",
										"requestLocationUpdates");
								Location location = getLocationIn(
										Double.parseDouble(value.split(",")[0]),
										Double.parseDouble(value.split(",")[1]),
										"");

								LocationListener listener = (LocationListener) args[5];
								fakeListener.setListener(listener);
								fakeListener.setLocation(location);
								args[5] = fakeListener;
							} catch (Exception e) {
								try {
									// TODO: we are not doing anything in
									// this case
									Log.d(Utils.DEBUG,
											"_requestUpdates, ASA");
									//PendingIntent intent = (PendingIntent) args[5];
								} catch (Exception e2) {
									Log.d(Utils.ERROR,
											"The argument should be LocationListener or PendingIntent");
								}
							}

							return invoke(hooked, args);
						}

					});
		}
	}
}

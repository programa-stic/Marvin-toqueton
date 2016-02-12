package ar.fsadosky.marvintoqueton.privacyhooks;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.json.JSONObject;

import android.os.Environment;
import android.util.Log;
import com.saurik.substrate.MS;

public abstract class PrivacyHook {
	protected static JSONObject privacyValues;

	protected static Method getMethod(String clazz, String method,
			Class<?>[] params) {
		try {
			return Class.forName(clazz).getDeclaredMethod(method, params);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	protected void loadJsonFromFile() {
		try {
			File dir = Environment.getExternalStorageDirectory();
			File jsonFile = new File(dir, "privacy.json");
			FileInputStream stream = new FileInputStream(jsonFile);
			String jString = null;
			FileChannel fc = stream.getChannel();
			MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0,
					fc.size());
			/* Instead of using default, pass in a decoder. */
			jString = Charset.defaultCharset().decode(bb).toString();

			privacyValues = new JSONObject(jString);
			if (stream != null)
				stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected String readValueFromProfile(String clazz, String method) {
		if (privacyValues == null) {
			// init on first time
			loadJsonFromFile();
		}
		// read json from sdcard
		/*
		 * format { class1 : method1 : value, method2 : value, class2
		 */
		try {

			return privacyValues.getJSONObject(clazz).getString(method);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	protected abstract Object convertFromProfile(String clazz, String method,
			String value);

	protected void hookAndSaveReturnValue(final String clazz,
			final String methodName, final Class<?>[] params) {
		MS.hookClassLoad(clazz, new MS.ClassLoadHook() {
			public void classLoaded(Class<?> clazzLoaded) {
				Method method;
				try {
					method = clazzLoaded.getDeclaredMethod(methodName, params);
				} catch (NoSuchMethodException e) {
					Log.d(Utils.ERROR, Log.getStackTraceString(e));
					method = null;
				}

				if (method != null) {
					MS.hookMethod(clazzLoaded, method,
							new MS.MethodAlteration<Object, Object>() {
								public Object invoked(final Object hooked,
										final Object... args) throws Throwable {
									Object result = invoke(hooked, args);

									if (result == null)
										return null;

									Object returned_object = null;

									try {
										returned_object = convertFromProfile(
												clazz,
												methodName,
												readValueFromProfile(clazz,
														methodName));
									} catch (Exception e) {
										Log.d("DEBUG",
												"Probably booting, return null as a fallback");
									}
									// Log.d("DEBUG", "class:" + clazz);
									// Log.d("DEBUG", "method:" + methodName);
									// Log.d("DEBUG", "value:" +
									// result.toString());

									return returned_object;
								}
							});

				}
			}
		});
	}

}

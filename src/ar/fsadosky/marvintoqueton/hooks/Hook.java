package ar.fsadosky.marvintoqueton.hooks;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnAttachStateChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.ToggleButton;
import ar.fsadosky.marvintoqueton.Utils;

import com.saurik.substrate.MS;

public abstract class Hook {
	final static Class hooked[] = { Button.class, CheckBox.class,
			EditText.class, RadioGroup.class, Spinner.class, TimePicker.class,
			DatePicker.class, ToggleButton.class, AdapterView.class , ViewGroupHook.class };
	
	public static final int UPDATEVIEW = 0;
	public static final int CLEAN_SERVED_VIEWS_TIMER = 1;
	public static final int CHANGE_ACTIVITY = 2;

	public static Set<View> dettached_views = new HashSet<View>();
	//public static Set<View> already_served_views = new HashSet<View>();

	public abstract boolean updateView(View view);
	
	public void hook() {
		MS.hookClassLoad(getHookedClass(), new MS.ClassLoadHook() {
			public void classLoaded(final Class<?> class_hooked) {
				if (getConstructor() != null) {
					Log.d("DEBUG", "hooking " + class_hooked.getName());
					MS.hookMethod(class_hooked, getConstructor(),
							new MS.MethodAlteration<Object, Object>() {
								public Object invoked(Object hooked,
										Object... args) throws Throwable {
									Object o = invoke(hooked, args);
									Context c = (Context) args[0];
									if (Utils.isThirdParty(c,
											c.getPackageName())	&& (checkHook((View) hooked))) {
										if (getCallMultipleTimes())
											Utils.repeatAtRandomEventToUpdateView(Hook.this,
													(View) hooked, getDelay());
										else
											Utils.sendEventToUpdateView(Hook.this,(View) hooked,getDelay());
										((View) hooked).addOnAttachStateChangeListener(new OnAttachStateChangeListener() {
											
											@Override
											public void onViewDetachedFromWindow(View v) {
												Log.d("DEBUG","detached window, when obtaining it in handler it will be removed");
												Hook.dettached_views .add(v);
											}

											@Override
											public void onViewAttachedToWindow(
													View v) {}
										});
									}

									return o;
								}

							});
				}

			}

		});
	}

	protected long getDelay() {
		return Utils.getRandomWaitForEvent();
	}

	protected String getHookedClass() {
		Log.d("DEBUG", "calling empty");
		return "";
	}

	protected boolean checkHook(View view) {
		return true;
	}

	protected boolean getCallMultipleTimes() {
		return false;
	}

	protected Constructor getConstructor() {
		Constructor constructor = null;
		try {
			constructor = Class.forName(getHookedClass())
					.getDeclaredConstructor(Context.class, AttributeSet.class,
							int.class);
		} catch (Exception e) {
			Log.d("DEBUG", Log.getStackTraceString(e));
		}
		return constructor;
	}
}

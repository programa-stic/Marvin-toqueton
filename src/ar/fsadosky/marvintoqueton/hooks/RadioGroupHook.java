package ar.fsadosky.marvintoqueton.hooks;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import ar.fsadosky.marvintoqueton.Utils;

public class RadioGroupHook extends Hook {

	public String getHookedClass() {
		return "android.widget.RadioGroup";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	protected Constructor getConstructor() {
		Constructor constructor = null;
		try {
			constructor = Class.forName(getHookedClass())
					.getDeclaredConstructor(Context.class, AttributeSet.class);
		} catch (Exception e) {
			Log.d("DEBUG", Log.getStackTraceString(e));
		}
		return constructor;
	}

	@Override
	public boolean updateView(View view) {
		RadioGroup radioGroup = ((RadioGroup) view);
		if (radioGroup.getChildCount() > 0) {
			int randomIndex = Utils.randInt(0, radioGroup.getChildCount());
			Log.d("DEBUG", "Setting radioGroup index " + randomIndex);
			((RadioButton) radioGroup.getChildAt(randomIndex)).setChecked(true);
		}
		radioGroup.setFocusable(false);
		return true;
	}
}

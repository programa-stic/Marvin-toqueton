package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

public class ToggleButtonHook extends Hook {

	public String getHookedClass() {
		return "android.widget.ToggleButton";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	@Override
	public boolean updateView(View view) {
		ToggleButton toggle = (ToggleButton) view;
		Log.d("DEBUG", "Setting ToggleButton to " + !toggle.isChecked());
		toggle.setChecked(!toggle.isChecked());
		view.setFocusable(false);
		return true;

	}
}

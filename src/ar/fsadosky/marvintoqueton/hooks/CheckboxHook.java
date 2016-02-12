package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

public class CheckboxHook extends Hook {

	public String getHookedClass() {
		return "android.widget.CheckBox";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	@Override
	public boolean updateView(View view) {
		CheckBox checkbox = (CheckBox) view;
		Log.d("DEBUG", "Setting checkbox " + !checkbox.isChecked());
		checkbox.setChecked(checkbox.isChecked());
		view.setFocusable(false);
		return true;
	}
}

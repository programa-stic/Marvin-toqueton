package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import ar.fsadosky.marvintoqueton.Utils;

public class SpinnerHook extends Hook {

	public String getHookedClass() {
		return "android.widget.Spinner";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	@Override
	public boolean updateView(View view) {
		Spinner spinner = (Spinner) view;
		int randomIndex = Utils.randInt(0, spinner.getChildCount());
		Log.d("DEBUG", "Setting radioGroup index " + randomIndex);
		spinner.setSelection(randomIndex);
		view.setFocusable(false);
		return true;

	}
}

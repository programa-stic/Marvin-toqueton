package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.Button;

public class ButtonHook extends Hook {

	public String getHookedClass() {
		return "android.widget.Button";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	@Override
	public boolean updateView(View view) {
		Log.d("DEBUG", "Pressing button ");
		((Button) view).performClick();
		view.setFocusable(false);
		return true;

	}
	
}

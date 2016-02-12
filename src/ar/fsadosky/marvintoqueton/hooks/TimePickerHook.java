package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

public class TimePickerHook extends Hook {
	public String getHookedClass() {
		return "android.widget.TimePicker";
	}

	@Override
	public boolean updateView(View view) {
		TimePicker timePicker = ((TimePicker) view);
		Log.d("DEBUG", "Setting Timepicker");
		timePicker.setCurrentHour(007);
		timePicker.setCurrentMinute(007);
		view.setFocusable(false);
		return true;

	}
}

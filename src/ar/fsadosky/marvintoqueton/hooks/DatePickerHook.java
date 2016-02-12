package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.DatePicker;


public class DatePickerHook extends Hook {

	public String getHookedClass() {
		return "android.widget.DatePicker";
	}

	@Override
	public boolean updateView(View view) {
		Log.d("DEBUG", "Setting date ");
		((DatePicker) view).updateDate(
				1989, 1, 1);
		view.setFocusable(false);	
		return true;
	}

	
}

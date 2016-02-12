package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import ar.fsadosky.marvintoqueton.Utils;

public class AdapterViewHook extends Hook {

	protected String getHookedClass() {
		return "android.widget.AdapterView";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	@Override
	public boolean updateView(View view) {
		AdapterView adapterview = (AdapterView) view;
		Log.d("DEBUG", "Setting clicking in adapter view ");
		if (adapterview.getCount() > 0) {
			int randomIndex = Utils.randInt(0, adapterview.getCount());
			View child = adapterview.getAdapter().getView(randomIndex, null,
					null);
			adapterview.setSelection(randomIndex);
			adapterview.performItemClick(child, randomIndex,
					adapterview.getItemIdAtPosition(randomIndex));
		}
		view.setFocusable(false);
		return true;
	}

}

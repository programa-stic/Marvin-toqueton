package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import ar.fsadosky.marvintoqueton.Utils;

public class ViewHook extends Hook {

	protected String getHookedClass() {
		return "android.view.View";
	}

	protected boolean checkHook(View view) {
		for (Class c : hooked) {
			if (view.getClass() == c)
				return false;
		}
		return true;
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	protected long getDelay() {
		return Utils.getRandomWaitForEvent() * 3;
	}

	@Override
	public boolean updateView(View view) {
		if (view.isClickable() && view.isLongClickable()) {
			Log.d("DEBUG", "Clicking view ");
			int random = Utils.randInt(0, 3);
			// 2 out of 3 perform simple click
			if (random % 2 == 0)
				view.performClick();
			else
				view.performLongClick();
		} else if (view.isClickable()) {
			Log.d("DEBUG", "Clicking view ");
			view.performClick();
		} else if (view.isLongClickable()) {
			Log.d("DEBUG", "Clicking view ");
			view.performLongClick();
		} else {
			// do not call back message
			return false;
		}
		return true;

	}

}

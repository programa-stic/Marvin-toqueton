package ar.fsadosky.marvintoqueton.hooks;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import ar.fsadosky.marvintoqueton.Utils;

public class ViewGroupHook extends Hook {

	protected String getHookedClass() {
		return "android.view.ViewGroup";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	protected long getDelay() {
		return Utils.getRandomWaitForEvent() * 3;
	}

	@Override
	public boolean updateView(View view) {
		int childCount = ((ViewGroup) view).getChildCount();
		Log.d("DEBUG", "child count is " + childCount);
		if (childCount > 0) {
			int randomIndex = Utils.randInt(0, childCount);
			View child = ((ViewGroup) view).getChildAt(randomIndex);
			int x = child.getHeight();
			int y = child.getWidth();
			((ViewGroup) view).requestChildFocus(child, child);
			Log.d("DEBUG", "scrolling view to " + x + " " + y);
		}
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
		}
		return true;

	}

}

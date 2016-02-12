package ar.fsadosky.marvintoqueton.hooks;

import java.lang.reflect.Constructor;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import ar.fsadosky.marvintoqueton.Utils;

public class SearchViewHook extends Hook {
	final private static String searchKeywords[] = { "app", "Android",
			"application", "phone" };

	public String getHookedClass() {
		return "android.widget.SearchView";
	}

	protected boolean getCallMultipleTimes() {
		return true;
	}

	public static String getQueryFor(View view) {
		return searchKeywords[Utils.randInt(0, searchKeywords.length)];
	}

	protected long getDelay() {
		return Utils.getRandomWaitForEvent() * 5;
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
		SearchView searchView = ((SearchView) view);
		Log.d("DEBUG", "Performing search ");
		searchView.setQuery(SearchViewHook.getQueryFor(searchView), true);
		view.setFocusable(false);
		return true;

	}
}

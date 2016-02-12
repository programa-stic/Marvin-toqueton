package ar.fsadosky.marvintoqueton.hooks;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditTextHook extends Hook {
	private final static String[] mailKeywords = { "mail" };
	private final static String[] phoneKeywords = { "cell", "phone", "mobile" };
	private final static String[] passwordKeywords = { "password", "pwd",
			"pass", "characters" };
	private final static String[] nameKeywords = { "name", "lastname", "last",
			"last_name" };
	private final static String[] zipKeywords = { "zip", "postal" };
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public String getHookedClass() {
		return "android.widget.EditText";
	}

	static boolean looksLike(String match, String EMAIL_PATTERN) {
		if (match == null)
			return false;
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(match);
		return matcher.matches();
	}

	static boolean hasKeywords(String match, String[] keywords) {
		if (match == null)
			return false;
		for (String key : keywords) {
			if (match.toLowerCase(Locale.getDefault()).contains(key)) {
				return true;
			}
		}
		return false;
	}

	protected String getMessage(View hooked) {
		return getMessageFor(hooked);
	}

	public static String getMessageFor(View hooked) {
		String message = null;
		EditText textview = (EditText) hooked;

		String resource_id = textview.getContext().getResources()
				.getResourceEntryName(textview.getId());
		String hint = "" + textview.getHint();
		int types = textview.getInputType();

		Log.d("DEBUG", "id " + textview.getId());
		Log.d("DEBUG", "name " + resource_id);
		Log.d("DEBUG", "hint " + hint);
		Log.d("DEBUG", "class " + textview.getClass().getName());

		if ((types & InputType.TYPE_CLASS_NUMBER) == InputType.TYPE_CLASS_NUMBER
				|| (types & InputType.TYPE_NUMBER_FLAG_DECIMAL) == InputType.TYPE_NUMBER_FLAG_DECIMAL
				|| (types & InputType.TYPE_NUMBER_FLAG_SIGNED) == InputType.TYPE_NUMBER_FLAG_SIGNED) {
			Log.d("DEBUG", "setting number");
			message = "23";
		}
		if ((types & InputType.TYPE_CLASS_TEXT) == InputType.TYPE_CLASS_TEXT) {
			Log.d("DEBUG", "setting text");
			message = "FuzzingText";
		}

		if ((types & InputType.TYPE_DATETIME_VARIATION_NORMAL) == InputType.TYPE_DATETIME_VARIATION_NORMAL
				|| (types & InputType.TYPE_DATETIME_VARIATION_DATE) == InputType.TYPE_DATETIME_VARIATION_DATE) {
			Log.d("DEBUG", "setting date");
			message = "01-02-1989";
		}

		if ((types & InputType.TYPE_DATETIME_VARIATION_TIME) == InputType.TYPE_DATETIME_VARIATION_TIME) {
			Log.d("DEBUG", "setting time");
			message = "00:70:07";
		}

		if ((types & InputType.TYPE_CLASS_PHONE) == InputType.TYPE_CLASS_PHONE
				|| hasKeywords(hint, phoneKeywords)
				|| hasKeywords(resource_id, phoneKeywords)) {
			Log.d("DEBUG", "setting phone");
			message = "1112341234";
		}

		if ((types & InputType.TYPE_TEXT_VARIATION_PERSON_NAME) == InputType.TYPE_TEXT_VARIATION_PERSON_NAME
				|| hasKeywords(hint, nameKeywords)
				|| hasKeywords(resource_id, nameKeywords)) {
			Log.d("DEBUG", "setting name");
			message = "NameFuzzing";
		}
		if ((types & InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS
				|| hasKeywords(hint, zipKeywords)
				|| hasKeywords(resource_id, zipKeywords)) {
			Log.d("DEBUG", "setting postal code");
			message = "90210";
		}
		if ((types & InputType.TYPE_TEXT_VARIATION_URI) == InputType.TYPE_TEXT_VARIATION_URI) {
			Log.d("DEBUG", "setting URI");
			message = "http://www.fundacionsadosky.org.ar";
		}
		if ((types & InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS
				|| (types & InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS) == InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
				|| hasKeywords(hint, mailKeywords)
				|| hasKeywords(resource_id, mailKeywords)
				|| looksLike(hint, EMAIL_PATTERN)) {
			message = "fakeemailandroid@gmail.com";
		}

		if ((types & InputType.TYPE_NUMBER_VARIATION_PASSWORD) == InputType.TYPE_NUMBER_VARIATION_PASSWORD) {
			Log.d("DEBUG", "setting number password");
			message = "007007";
		} else if ((types & InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
				|| (types & InputType.TYPE_TEXT_VARIATION_PASSWORD) == InputType.TYPE_TEXT_VARIATION_PASSWORD
				|| (types & InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD) == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD
				|| hasKeywords(hint, passwordKeywords)
				|| hasKeywords(resource_id, passwordKeywords)) {
			Log.d("DEBUG", "setting password");
			message = "s3cr3tpass";
		}

		return message;

	}

	@Override
	public boolean updateView(View view) {
		Log.d("DEBUG", "Setting edittext " + EditTextHook.getMessageFor(view));
		((EditText) view).setText(EditTextHook.getMessageFor(view));
		view.setFocusable(false);
		return true;

	}

}

package ar.fsadosky.marvintoqueton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.Log;
import ar.fsadosky.marvintoqueton.hooks.*;
import ar.fsadosky.marvintoqueton.privacyhooks.ContactsHelper;
import ar.fsadosky.marvintoqueton.privacyhooks.DeviceDataHook;
import ar.fsadosky.marvintoqueton.privacyhooks.LocationHook;
import ar.fsadosky.marvintoqueton.privacyhooks.WifiHook;

public class Main {

	static void initialize() {

		// gui fuzzer hooks
		(new EditTextHook()).hook();
		(new DatePickerHook()).hook();
		(new TimePickerHook()).hook();
		(new ToggleButtonHook()).hook();
		(new SpinnerHook()).hook();
		(new ButtonHook()).hook();
		(new RadioGroupHook()).hook();
		(new SearchViewHook()).hook();
		(new CheckboxHook()).hook();
		(new AdapterViewHook()).hook();
		(new ViewGroupHook()).hook();
		(new ViewHook()).hook();

		// privacy Hooks
		(new WifiHook()).hook();
		(new DeviceDataHook()).hook();
		(new LocationHook()).hook();
	}

	private static void CopyJsontoSDCard(Context c) throws IOException {

		File dir = Environment.getExternalStorageDirectory();
		InputStream in = c.getAssets().open("privacy.json");
		FileOutputStream out = new FileOutputStream(dir.getAbsolutePath()+"/privacy.json");
		byte[] buff = new byte[1024];
		int read = 0;
		try {
			while ((read = in.read(buff)) > 0) {
				out.write(buff, 0, read);
			}
		} finally {
			in.close();
			out.close();
		}
	}

	public static void doOnceWhenInstalled(Context context) {

		ContactsHelper.AddDummyContacts(context);
		try { 
			Main.CopyJsontoSDCard(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
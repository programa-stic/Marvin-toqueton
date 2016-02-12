package ar.fsadosky.marvintoqueton;

import java.util.Arrays;
import java.util.Random;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.View;
import ar.fsadosky.marvintoqueton.hooks.Hook;

public class Utils {

	public static final int MIN_DELAY_FOR_EVENT = 10000;
	public static final int MAX_DELAY_FOR_EVENT = 20000;
	public static final String ERROR = "FUZZER_ERROR";

	public static boolean isThirdParty(Context context, String packageName) {
		PackageManager manager = context.getPackageManager();
		try {
			return !((manager.getPackageInfo(packageName, 0).applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static void sendMessageToClearServedViews() {
		MessageHandler.sendMessage(null, Hook.CLEAN_SERVED_VIEWS_TIMER,
				Utils.MIN_DELAY_FOR_EVENT);
	}

	public static void sendMessageToChangeActivity() {
		MessageHandler.sendMessage(null, Hook.CHANGE_ACTIVITY,Utils.MIN_DELAY_FOR_EVENT * 10);
	}

	public static void repeatAtRandomEventToUpdateView(Hook hook, View hooked,
			long delay) {
		UpdateViewTask task = new UpdateViewTask();
		task.view = hooked;
		task.hook = hook;
		task.repeat = true;
		MessageHandler.sendMessage(task, Hook.UPDATEVIEW, delay);
	}

	public static void sendEventToUpdateView(Hook hook, View hooked, long delay) {
		UpdateViewTask task = new UpdateViewTask();
		task.view = hooked;
		task.hook = hook;
		MessageHandler.sendMessage(task, Hook.UPDATEVIEW, delay);
	}

	public static int randInt(int Low, int High) {
		Random r = new Random();
		int random = r.nextInt(High - Low) + Low;
		return random;
	}

	public static long getRandomWaitForEvent() {
		// get random between 500 milliseconds and 3000 (3 seconds)
		return randInt(MIN_DELAY_FOR_EVENT, MAX_DELAY_FOR_EVENT);
	}

	public static void changeToRandomActivity(Context context) throws NameNotFoundException {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
		int randomIndex = Utils.randInt(0, info.activities.length);
		ActivityInfo activity = info.activities[randomIndex];
		Intent intent = new Intent();
		ComponentName name =new ComponentName(activity.applicationInfo.packageName,
                activity.name);
		intent.setComponent(name);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);	
		}

}

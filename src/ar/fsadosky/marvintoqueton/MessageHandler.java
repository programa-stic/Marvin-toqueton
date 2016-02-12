package ar.fsadosky.marvintoqueton;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import ar.fsadosky.marvintoqueton.hooks.Hook;

public class MessageHandler {
	public static Handler handler;
	//Last used context in a view, used to start another activity
	public static Context context;
	//is third party app
	public static boolean third_party = false;


	public static void sendMessage(Object object, int code, long delayMillis) {
		Message msg = MessageHandler.getHandler().obtainMessage(code, object);
		MessageHandler.getHandler().sendMessageDelayed(msg, delayMillis);
	}

	public static Handler getHandler() {
		if (handler == null) {
			initMessageHandler();
		}
		return handler;
	}

	public static void initMessageHandler() {
		if (handler == null) {
			Log.d("DEBUG", "initializating handler");
			handler = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message inputMessage) {
					try {
						switch (inputMessage.what) {
						/*case Hook.CLEAN_SERVED_VIEWS_TIMER:
							Hook.already_served_views.clear();
							//resend message again
							Utils.sendMessageToClearServedViews();
							break;*/
						case Hook.UPDATEVIEW:
							UpdateViewTask viewTask = (UpdateViewTask) inputMessage.obj;
							//allows changing activity
							if(context == null){
								//init if it's third_party
								third_party = Utils.isThirdParty(viewTask.view.getContext(), viewTask.view.getContext().getPackageName());
							}
							//updating last used context
							MessageHandler.context = viewTask.view.getContext();

							Log.d("DEBUG", "ID: " + viewTask.view.getId());
							Log.d("DEBUG", "Class: "
									+ viewTask.view.getClass().getName());
							Log.d("DEBUG",
									"getVisibility: "
											+ viewTask.view.getVisibility());
							// if was dettached, do not process it
							if (Hook.dettached_views.contains(viewTask.view)) {
								Hook.dettached_views.remove(viewTask.view);
								Log.d("DEBUG",
										"stopped processing detached view");
								return;
							}
							/*
							Log.d("DEBUG","Served views size"+Hook.already_served_views.size());
							Log.d("DEBUG","Served views "+Arrays.toString(Hook.already_served_views.toArray()));
							if(Hook.already_served_views.contains(viewTask.view)){
								//there appears to be multiple instances of the same 
								//view, since served_already is refreshed every
								//min delay so stop processing this update
								Log.d("DEBUG",
										"view was served before MIN delay");
								return;
							}
							else{
								//marking as served
								Hook.already_served_views.add(viewTask.view);
							}*/
														
							// If set to repeat, send message again
							if (viewTask.hook.updateView(viewTask.view) && viewTask.repeat) {

								long newdelay = 0;
								// long delay for background objects
								if (viewTask.view.getVisibility() != View.VISIBLE) {
									newdelay = Utils.getRandomWaitForEvent() * 5;
								} else {
									newdelay = Utils.getRandomWaitForEvent();
								}
								Log.d("DEBUG",
										"ID delay is " + viewTask.view.getId());
								Log.d("DEBUG", "new delay is " + newdelay);
								handler.sendMessageDelayed(
										Message.obtain(inputMessage),
										newdelay);
							}
							break;
						case Hook.CHANGE_ACTIVITY:
							Log.d("DEBUG", "Changing activity ");
							//only for third_parties
							if( MessageHandler.context != null && third_party){
								Log.d("DEBUG", "Changing activity ");
								Utils.changeToRandomActivity(MessageHandler.context);
							}
							handler.sendMessageDelayed(
									Message.obtain(inputMessage),
									Utils.MIN_DELAY_FOR_EVENT * 5);
							break;
						}
					} catch (Exception e) {
						Log.d("DEBUG", "Ignoring exceptions ");
						Log.d("DEBUG", Log.getStackTraceString(e));
					}
				}

			};
			//send message to clear served views every MIN seconds
			//Utils.sendMessageToClearServedViews();
			//send message to change activity
			Utils.sendMessageToChangeActivity();
		}
	}
}

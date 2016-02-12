package ar.fsadosky.marvintoqueton.privacyhooks;

public class DeviceDataHook extends PrivacyHook {

	public void hook() {

		// mising hook

		// get SimOperatorName
		// getNetworkOperatorName
		// getNetworkOperator
		// getSIMOperator
		Class[] params = new Class[0];
		String telephonyManager = "android.telephony.TelephonyManager";
		hookAndSaveReturnValue(telephonyManager, "getSimOperatorName", params);
		hookAndSaveReturnValue(telephonyManager, "getSimOperator", params);
		hookAndSaveReturnValue(telephonyManager, "getNetworkOperator", params);
		hookAndSaveReturnValue(telephonyManager, "getNetworkOperatorName", params);
		
		String gsmPhone = "com.android.internal.telephony.gsm.GSMPhone";
		hookAndSaveReturnValue(gsmPhone, "getDeviceId", params);
		hookAndSaveReturnValue(gsmPhone, "getImei", params);
		hookAndSaveReturnValue(gsmPhone, "getSubscriberId", params);
		hookAndSaveReturnValue(gsmPhone, "getLine1Number", params);

		String CDMAPhone = "com.android.internal.telephony.gsm.CDMAPhone";
		hookAndSaveReturnValue(CDMAPhone, "getDeviceId", params);
		hookAndSaveReturnValue(CDMAPhone, "getMeid", params);
		hookAndSaveReturnValue(CDMAPhone, "getSubscriberId", params);
		hookAndSaveReturnValue(CDMAPhone, "getLine1Number", params);

		hookAndSaveReturnValue("com.android.internal.telephony.PhoneBase",
				"getIccSerialNumber", params);

	}

	@Override
	protected Object convertFromProfile(String clazz, String method, String value) {
		return value;
	}

}

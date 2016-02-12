package ar.fsadosky.marvintoqueton.privacyhooks;

import android.content.Context;
//import ar.fsadosky.fuzzinghelper.Utils;
import ar.fsadosky.marvintoqueton.privacyhooks.Utils;

import com.saurik.substrate.MS;

public class WifiHook extends PrivacyHook {

	protected void hookGetDefaultIP() {
		Class<?>[] params = new Class[1];
		params[0] = Context.class;
		hookAndSaveReturnValue("com.android.settings.Utils",
				"getWifiIpAddresses", params);
	}

	public void hook() {
		hookGetDefaultIP();
		hookUtilsGetDefaultWifiIpAddresses();
		hookWifiConfiguredNetworks();
		hookWifiScannedNetworks();
		hookWifiInfoMethods();
	}

	protected void hookUtilsGetDefaultWifiIpAddresses() {
		Class<?>[] params = new Class[1];
		params[0] = Context.class;
		hookAndSaveReturnValue("com.android.settings.Utils",
				"getDefaultIpAddresses", params);
		// must return Utils.getFormatedIpFromIp(String);
	}

	public void hookWifiInfoMethods() {
		Class<?>[] params = new Class[0];
		String wifiInfo = "android.net.wifi.WifiInfo";
		hookAndSaveReturnValue(wifiInfo, "getBSSID", params);
		hookAndSaveReturnValue(wifiInfo, "getIpAddress", params);
		hookAndSaveReturnValue(wifiInfo, "getMacAddress", params);
		hookAndSaveReturnValue(wifiInfo, "getSSID", params);
	}

	protected static int fakeIP() {
		return getRealIPFrom("192.168.0.2");
	}

	@Override
	protected Object convertFromProfile(String clazz, String method, String value) {
		if (method.equals("getFormatedIpFromIp")
				|| method.equals("getWifiIpAddresses"))
			return Utils.getFormatedIpFromIp(getRealIPFrom("192.168.0.2"));
		else if (method.equals("getConfiguredNetworks"))
			return getDummyConfiguredNetworks();
		else if (method.equals("getScanResults"))
			return getDummyScannedNetworks();
		else
			return value;
	}

	private Object getDummyScannedNetworks() {
		// TODO Auto-generated method stub
		return null;
	}

	private Object getDummyConfiguredNetworks() {
		// TODO Auto-generated method stub
		return null;
	}

	private static int getRealIPFrom(String ipAddress) {
		String[] octets = ipAddress.split("\\.");
		return (Integer.parseInt(octets[3]) << 24)
				+ (Integer.parseInt(octets[2]) << 16)
				+ (Integer.parseInt(octets[1]) << 8)
				+ Integer.parseInt(octets[0]);
	}

	protected void hookWifiConfiguredNetworks() {
		Class<?>[] params = new Class[0];
		hookAndSaveReturnValue("android.net.wifi.WifiManager",
				"getConfiguredNetworks", params);
	}

	protected void hookWifiScannedNetworks() {
		Class<?>[] params = new Class[0];
		hookAndSaveReturnValue("android.net.wifi.WifiManager",
				"getScanResults", params);
	}

}

package ar.fsadosky.marvintoqueton.privacyhooks;

public class Utils {

	protected static final String ERROR = "FUZZER_ERROR";
	protected static final String DEBUG = "FUZZER_DEBUG";

	public static String getFormatedIpFromIp(Integer ipAddress) {
		return String.format("%d.%d.%d.%d", (ipAddress & 0xff),
				(ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
				(ipAddress >> 24 & 0xff));
	}
}

package cat.aubricoc.holcost.util;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import cat.aubricoc.holcost.androidservice.UploadDataService;

public class Utils {

	public static void runUploadDataService(Context context) {
		context.startService(new Intent(context, UploadDataService.class));
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}
}

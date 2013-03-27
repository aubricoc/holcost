package cat.aubricoc.holcost.androidservice;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import cat.aubricoc.holcost.service.SyncService;
import cat.aubricoc.holcost.util.Utils;

public class UploadDataService extends Service {

	private SyncService syncService = new SyncService(this);

	private Timer timer = new Timer();

	private TimerTask timerTask = new TimerTask() {

		@Override
		public void run() {
			Log.i(UploadDataService.class.getName(),
					"Uploading data service runned");
			if (Utils.isOnline(UploadDataService.this)) {
				if (syncService.havePendingChanges()) {
					if (syncService.uploadData()) {
						if (!syncService.havePendingChanges()) {
							Log.i(UploadDataService.class.getName(),
									"Uploading data service stopped");
							stopSelf();
							cancel();
						}
					}
				} else {
					Log.i(UploadDataService.class.getName(),
							"Uploading data service stopped");
					stopSelf();
					cancel();
				}
			}
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		timer.scheduleAtFixedRate(timerTask, 100, 120000);
		Log.i(UploadDataService.class.getName(),
				"Uploading data service created");
	}

	@Override
	public void onDestroy() {
		timerTask.cancel();
		Log.i(UploadDataService.class.getName(),
				"Uploading data service destroyed");
	}
}

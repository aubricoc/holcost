package cat.aubricoc.holcost.activity;

import android.content.Intent;
import android.view.MenuItem;
import cat.aubricoc.holcost.R;

import com.canteratech.androidutils.exception.NoFinishActivityException;

public abstract class Activity extends com.canteratech.androidutils.Activity {

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			back();
			return true;
		}
		return false;
	}

	@Override
	public void back() {
		try {
			Intent intent = onBack();
			finish();
			if (intent != null) {
				startActivity(intent);
			}
			overridePendingTransition(R.anim.animation_back_enter,
					R.anim.animation_back_exit);
		} catch (NoFinishActivityException e) {
			return;
		}
	}

	@Override
	public void goTo(Class<?> activity) {
		goTo(activity, true);
	}

	public void goTo(Class<?> activity, boolean moveToRight) {
		Intent intent = newIntent(activity);
		goTo(intent, moveToRight);
	}

	@Override
	public void goToAndFinish(Class<?> activity) {
		goToAndFinish(activity, true);
	}

	public void goToAndFinish(Class<?> activity, boolean moveToRight) {
		Intent intent = newIntent(activity);
		goToAndFinish(intent, moveToRight);
	}

	@Override
	public void goToAndFinish(Intent intent) {
		goToAndFinish(intent, true);
	}

	public void goToAndFinish(Intent intent, boolean moveToRight) {
		finish();
		goTo(intent, moveToRight);
	}

	@Override
	public void goTo(Intent intent) {
		goTo(intent, true);
	}

	public void goTo(Intent intent, boolean moveToRight) {
		goToForResult(intent, null, moveToRight);
	}

	@Override
	public void goToForResult(Intent intent, Integer requestCode) {
		goToForResult(intent, requestCode, true);
	}
	
	public void goToForResult(Class<?> activity, Integer requestCode,
			boolean moveToRight) {
		Intent intent = newIntent(activity);
		goToForResult(intent, requestCode, moveToRight);
	}

	public void goToForResult(Intent intent, Integer requestCode,
			boolean moveToRight) {
		if (requestCode == null) {
			startActivity(intent);
		} else {
			startActivityForResult(intent, requestCode);
		}
		if (moveToRight) {
			overridePendingTransition(R.anim.animation_next_enter,
					R.anim.animation_next_exit);
		} else {
			overridePendingTransition(R.anim.animation_back_enter,
					R.anim.animation_back_exit);
		}
	}
	
	public void refreshOnBack() {
		goToAndFinish(getIntent(), false);
	}
}

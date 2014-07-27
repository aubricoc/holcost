package cat.aubricoc.holcost.activity;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import android.app.ActionBar;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public abstract class Activity extends android.app.Activity {

	public static Context CURRENT_CONTEXT = null;

	protected boolean backButtonInActionBar = false;

	@Override
	protected final void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CURRENT_CONTEXT = this;

		setContentView(getLayoutId());

		onCreate();

		if (backButtonInActionBar) {
			ActionBar actionBar = getActionBar();
			actionBar.setHomeButtonEnabled(true);
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	protected abstract void onCreate();

	protected abstract int getLayoutId();

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			back();
			return true;
		}
		return false;
	}

	@Override
	public final void onBackPressed() {
		back();
	}

	public void back() {
		Intent intent = onBack();
		finish();
		if (intent != null) {
			startActivity(intent);
		}
	}

	protected Intent onBack() {
		return null;
	}

	public void onClick(int id, OnClickListener onClickListener) {
		View view = findViewById(id);
		onClick(view, onClickListener);
	}

	public void onClick(View view, OnClickListener onClickListener) {
		if (view != null) {
			view.setOnClickListener(onClickListener);
		}
	}

	public void onClickGoTo(int id, final Class<?> activity) {
		View view = findViewById(id);
		onClickGoTo(view, activity);
	}

	public void onClickGoTo(View view, final Class<?> activity) {
		onClick(view, new OnClickListener() {
			@Override
			public void onClick(View view) {
				goTo(activity);
			}
		});
	}

	public void onItemClick(int id, OnItemClickListener onItemClickListener) {
		ListView listView = (ListView) findViewById(id);
		onItemClick(listView, onItemClickListener);
	}

	public void onItemClick(ListView listView,
			OnItemClickListener onItemClickListener) {
		if (listView != null) {
			listView.setOnItemClickListener(onItemClickListener);
		}
	}

	public void onItemClickGoTo(int id, final Class<?> activity) {
		ListView listView = (ListView) findViewById(id);
		onItemClickGoTo(listView, activity);
	}

	public void onItemClickGoTo(ListView listView, final Class<?> activity) {
		onItemClick(listView, new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				goTo(activity);
			}
		});
	}

	public void onFocus(int id, OnFocusChangeListener onFocusChangeListener) {
		View view = findViewById(id);
		onFocus(view, onFocusChangeListener);
	}

	public void onFocus(View view, OnFocusChangeListener onFocusChangeListener) {
		if (view != null) {
			view.setOnFocusChangeListener(onFocusChangeListener);
		}
	}

	public void clear(int id) {
		setText(id, "");
	}

	public void clear(View view) {
		setText(view, "");
	}

	public void clear() {
		View focused = getWindow().getCurrentFocus();
		if (focused instanceof TextView) {
			TextView textView = (TextView) focused;
			textView.setText("");
		}
	}

	public void setText(int id, int string) {
		setText(findViewById(id), string);
	}

	public void setText(View view, int string) {
		setText(view, getString(string));
	}

	public void setText(int id, Character character) {
		setText(findViewById(id), character);
	}

	public void setText(View view, Character character) {
		setText(view, character != null ? character.toString() : null);
	}

	public void setText(int id, String text) {
		setText(findViewById(id), text);
	}

	public void setText(View view, String text) {
		TextView textView = (TextView) view;
		if (textView != null) {
			textView.setText(text);
		}
	}

	public void setTextIntValue(int id, Integer intValue) {
		setTextIntValue(findViewById(id), intValue);
	}

	public void setTextIntValue(View view, Integer intValue) {
		if (intValue == null) {
			clear(view);
		} else {
			setText(view, intValue.toString());
		}
	}

	public void check(int id, boolean checked) {
		CheckBox checkBox = (CheckBox) findViewById(id);
		if (checkBox != null) {
			checkBox.setChecked(checked);
		}
	}

	public String getInputText(int id) {
		TextView textView = (TextView) findViewById(id);
		if (textView != null) {
			return textView.getText().toString();
		}
		return null;
	}

	public Integer getInputInteger(int id) {
		String text = getInputText(id);
		if (text != null) {
			try {
				return Integer.parseInt(text);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	public Long getInputLong(int id) {
		String text = getInputText(id);
		if (text != null) {
			try {
				return Long.parseLong(text);
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	public Double getInputDouble(int id) {
		String text = getInputText(id);
		if (text != null) {
			try {
				NumberFormat numberFormat = NumberFormat.getInstance();
				Number number = numberFormat.parse(text);
				return number.doubleValue();
			} catch (ParseException e) {
				return null;
			}
		}
		return null;
	}

	public void showOnFront(int id) {
		View view = findViewById(id);
		showOnFront(view);
	}

	public void showOnFront(View view) {
		show(view);
		if (view != null) {
			view.bringToFront();
		}
	}

	public void show(int id) {
		View view = findViewById(id);
		show(view);
	}

	public void show(View view) {
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
	}

	public void hide(int id, boolean keepSpace) {
		View view = findViewById(id);
		hide(view, keepSpace);
	}

	public void hide(View view, boolean keepSpace) {
		if (view != null) {
			view.setVisibility(keepSpace ? View.INVISIBLE : View.GONE);
		}
	}

	public Intent newIntent(Class<?> activity) {
		return new Intent(this, activity);
	}

	public void goTo(Class<?> activity) {
		Intent intent = newIntent(activity);
		goTo(intent);
	}

	public void goToAndFinish(Class<?> activity) {
		Intent intent = newIntent(activity);
		goToAndFinish(intent);
	}

	public void goToAndFinish(Intent intent) {
		finish();
		goTo(intent);
	}

	public void goTo(Intent intent) {
		goToForResult(intent, null);
	}
	
	public void goToForResult(Class<?> activity, Integer requestCode) {
		Intent intent = newIntent(activity);
		goToForResult(intent, requestCode);
	}

	public void goToForResult(Intent intent, Integer requestCode) {
		if (requestCode == null) {
			startActivity(intent);
		} else {
			startActivityForResult(intent, requestCode);
		}
	}

	public void focus(int id) {
		View view = (View) findViewById(id);
		if (view != null) {
			focus(view);
		}
	}

	public void focus(View view) {
		if (view instanceof Button) {
			Button button = (Button) view;
			button.performClick();
		} else {
			view.requestFocus();
		}
	}

	public Integer getMaxLength(int id) {
		TextView textView = (TextView) findViewById(id);
		if (textView != null) {
			return getMaxLength(textView);
		}
		return null;
	}

	public Integer getMaxLength(TextView textView) {
		InputFilter[] filters = textView.getFilters();
		for (InputFilter filter : filters) {
			if (filter instanceof InputFilter.LengthFilter) {
				try {
					Field field = filter.getClass().getDeclaredField("mMax");
					field.setAccessible(true);
					return (Integer) field.get(filter);
				} catch (Exception e) {
					throw new IllegalArgumentException(e);
				}
			}
		}
		return null;
	}

	public <T> void setList(int id, List<T> list, int itemId) {
		ListView listView = (ListView) findViewById(id);
		setList(listView, list, itemId);
	}

	public <T> void setList(ListView listView, List<T> list, int itemId) {
		if (listView != null && list != null) {
			ListAdapter adapter = new ArrayAdapter<T>(this, itemId, list);
			listView.setAdapter(adapter);
		}
	}

	public boolean isChecked(int id) {
		CheckBox checkBox = (CheckBox) findViewById(id);
		return isChecked(checkBox);
	}

	public boolean isChecked(View view) {
		CheckBox checkBox = (CheckBox) view;
		if (checkBox != null) {
			return checkBox.isChecked();
		}
		return false;
	}

	public boolean isServiceRunning(Class<?> service) {
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> services = activityManager
				.getRunningServices(Integer.MAX_VALUE);

		for (RunningServiceInfo runningServiceInfo : services) {
			if (runningServiceInfo.service.getClassName().equals(
					service.getName())) {
				return true;
			}
		}
		return false;
	}

	public void refresh() {
		goToAndFinish(getIntent());
	}

	public void showToast(int stringId) {
		Toast.makeText(this, stringId, Toast.LENGTH_SHORT).show();
	}

	public void showToast(String text) {
		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
	}

	public void onChange(int id, OnTextChangeListener onTextChangeListener) {
		TextView textView = (TextView) findViewById(id);
		onChange(textView, onTextChangeListener);
	}

	public void onChange(final TextView textView,
			final OnTextChangeListener onTextChangeListener) {
		if (textView != null) {
			textView.addTextChangedListener(new TextWatcher() {

				private String previousValue;
				private String newValue;

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					previousValue = s.toString();
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					newValue = s.toString();
				}

				@Override
				public void afterTextChanged(Editable s) {
					onTextChangeListener.onChange(textView, previousValue,
							newValue);
				}
			});
		}
	}

	public interface OnTextChangeListener {
		void onChange(TextView textView, String previousValue, String newValue);
	}
}

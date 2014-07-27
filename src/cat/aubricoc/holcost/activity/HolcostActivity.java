package cat.aubricoc.holcost.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.DebtService;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.service.HolcostService;
import cat.aubricoc.holcost.util.Constants;
import cat.aubricoc.holcost.view.DebtListAdapter;

public class HolcostActivity extends Activity {

	@Override
	public void onCreate() {
		Holcost activeHolcost = HolcostService.getInstance().getActiveHolcost();

		if (activeHolcost == null) {
			goToAndFinish(CreateHolcostActivity.class);
		} else {

			setTitle(activeHolcost.getName());

			List<Dude> dudes = DudeService.getInstance().getDudesByHolcost(
					activeHolcost);

			new LoadDataTask().execute(dudes.toArray(new Dude[0]));

			if (dudes.isEmpty()) {
				show(R.id.firstStepMessage);
				show(R.id.createDude);
				onClick(R.id.createDude, new OnClickListener() {
					public void onClick(View v) {
						goToForResult(CreateDudeActivity.class, 0);
					}
				});
			} else {
				show(R.id.listDudes);
				show(R.id.createCost);
				onClick(R.id.createCost, new OnClickListener() {
					public void onClick(View v) {
						goToForResult(CostActivity.class, 0);
					}
				});
			}

			if (CostService.getInstance().existsCosts(activeHolcost)) {
				show(R.id.listCostButton);
				onClick(R.id.listCostButton, new OnClickListener() {
					public void onClick(View v) {
						goToForResult(ListCostActivity.class, 0);
					}
				});
			}
		}
	}

	private void showDebts(final List<Debt> debts) {
		ListView listView = (ListView) findViewById(R.id.listDudes);

		DebtListAdapter adapter = new DebtListAdapter(this,
				R.layout.list_item_debt, R.layout.list_item_nodebt, debts,
				getText(R.string.has_debt).toString(), getText(
						R.string.has_nodebt).toString());
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = newIntent(DudeActivity.class);
				intent.putExtra(Constants.EXTRA_DUDE_ID, debts.get(position)
						.getDude().getId());
				goToForResult(intent, 0);
			}
		});
	}

	class LoadDataTask extends AsyncTask<Dude, Void, List<Debt>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			progressDialog = ProgressDialog.show(HolcostActivity.this, null,
					getText(R.string.calculating_debts), true);
		}

		@Override
		protected List<Debt> doInBackground(Dude... dudes) {
			List<Debt> debts = new ArrayList<Debt>();
			for (Dude dude : dudes) {
				debts.add(DebtService.getInstance().getDudeDebt(dude));
			}
			return debts;
		}

		@Override
		protected void onPostExecute(List<Debt> debts) {
			showDebts(debts);
			progressDialog.dismiss();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.holcost_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.closeHolcostMenu:
			HolcostService.getInstance().closeActiveHolcost();
			goToAndFinish(CreateHolcostActivity.class, false);
			return true;
		case R.id.deleteHolcostMenu:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_delete_holcost)
					.setCancelable(true)
					.setPositiveButton(R.string.alert_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									HolcostService.getInstance()
											.deleteActiveHolcost();

									HolcostService.getInstance()
											.closeActiveHolcost();
									goToAndFinish(CreateHolcostActivity.class,
											false);
								}
							})
					.setNegativeButton(R.string.alert_no,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
			return true;
		case R.id.createDudeMenu:
			goToForResult(CreateDudeActivity.class, 0);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			refreshOnBack();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_holcost;
	}
}
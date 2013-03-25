package cat.aubricoc.holcost.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.DebtService;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.service.HolcostService;
import cat.aubricoc.holcost.view.DebtListAdapter;

public class HolcostActivity extends Activity {

	private HolcostService holcostService = new HolcostService(this);
	private DudeService dudeService = new DudeService(this);
	private CostService costService = new CostService(this);
	private DebtService debtService = new DebtService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Holcost activeHolcost = holcostService.getActiveHolcost();

		if (activeHolcost == null) {
			Intent intent = new Intent(this, CreateHolcostActivity.class);
			startActivity(intent);

			finish();
		} else {

			setContentView(R.layout.holcost);

			TextView title = (TextView) findViewById(R.id.holcostTitle);
			title.setText(activeHolcost.getName());

			List<Dude> dudes = dudeService.getDudesByHolcost(activeHolcost);
			final List<Debt> debts = new ArrayList<Debt>();
			for (Dude dude : dudes) {
				debts.add(debtService.getDudeDebt(dude));
			}

			ListView listView = (ListView) findViewById(R.id.listDudes);

			DebtListAdapter adapter = new DebtListAdapter(this,
					R.layout.list_line_debt, R.layout.list_line_nodebt, debts,
					getText(R.string.has_debt).toString(), getText(
							R.string.has_nodebt).toString());
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					Intent intent = new Intent(HolcostActivity.this,
							DudeActivity.class);
					intent.putExtra("dudeId", debts.get(position).getDude()
							.getId());
					startActivityForResult(intent, 0);
				}
			});

			if (!dudes.isEmpty()) {
				Button costButton = (Button) findViewById(R.id.createCost);
				costButton.setVisibility(View.VISIBLE);
				costButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(HolcostActivity.this,
								CostActivity.class);
						startActivityForResult(intent, 0);
					}
				});
			}

			if (costService.existsCosts(activeHolcost)) {
				Button listCostButton = (Button) findViewById(R.id.listCostButton);
				listCostButton.setVisibility(View.VISIBLE);
				listCostButton.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						Intent intent = new Intent(HolcostActivity.this,
								ListCostActivity.class);
						startActivityForResult(intent, 0);
					}
				});
			}
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
		Intent intent = null;

		switch (item.getItemId()) {
		case R.id.closeHolcostMenu:
			holcostService.closeActiveHolcost();

			intent = new Intent(HolcostActivity.this,
					CreateHolcostActivity.class);
			startActivity(intent);

			finish();
			return true;
		case R.id.deleteHolcostMenu:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_delete_holcost)
					.setCancelable(true)
					.setPositiveButton(R.string.alert_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									holcostService.deleteActiveHolcost();

									Intent intent = new Intent(
											HolcostActivity.this,
											CreateHolcostActivity.class);
									startActivity(intent);

									finish();
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
			intent = new Intent(HolcostActivity.this, CreateDudeActivity.class);
			startActivityForResult(intent, 0);

			return true;
		default:
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			finish();
			startActivity(getIntent());
		}
	}
}
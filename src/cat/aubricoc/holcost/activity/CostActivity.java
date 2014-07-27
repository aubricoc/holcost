package cat.aubricoc.holcost.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.service.HolcostService;

public class CostActivity extends Activity {

	@Override
	public void onCreate() {

		Long costId = getIntent().getLongExtra("costId", -1);

		Holcost activeHolcost = HolcostService.getInstance().getActiveHolcost();
		List<Dude> dudes = DudeService.getInstance().getDudesByHolcost(
				activeHolcost);
		final Cost cost = CostService.getInstance().getCostById(costId);

		final EditText nameInput = (EditText) findViewById(R.id.costName);
		final EditText amountInput = (EditText) findViewById(R.id.costAmount);
		if (cost != null) {
			nameInput.setText(cost.getName());
			amountInput.setText(cost.getAmount().toString());
		}

		final Spinner payers = (Spinner) findViewById(R.id.costPayer);
		payers.setAdapter(new ArrayAdapter<Dude>(this, R.layout.spinner_option,
				dudes));
		if (cost != null) {
			int iter = 0;
			for (Dude dude : dudes) {
				if (dude.getId().equals(cost.getPayer().getId())) {
					payers.setSelection(iter);
					break;
				}
				iter++;
			}
		}

		List<Dude> participants = null;
		if (cost != null) {
			participants = CostService.getInstance().getParticipants(cost);
		}

		LinearLayout participantsContainer = (LinearLayout) findViewById(R.id.costParticipantsContainer);
		final Map<CheckBox, Dude> checkboxes = new HashMap<CheckBox, Dude>();
		for (Dude dude : dudes) {

			CheckBox checkBox = new CheckBox(this);
			checkBox.setText(dude.getName());
			if (cost == null) {
				checkBox.setChecked(true);
			} else {
				checkBox.setChecked(false);
				for (Dude participant : participants) {
					if (participant.getId().equals(dude.getId())) {
						checkBox.setChecked(true);
					}
				}
			}
			participantsContainer.addView(checkBox);
			checkboxes.put(checkBox, dude);
		}
		participantsContainer.refreshDrawableState();

		Button saveButton = (Button) findViewById(R.id.createCostButton);

		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				String name = nameInput.getText().toString();

				if (name == null || name.trim().length() == 0) {

					Toast toast = Toast.makeText(CostActivity.this,
							getText(R.string.error_name_required),
							Toast.LENGTH_SHORT);
					toast.show();
					return;
				}

				String amountString = amountInput.getText().toString();

				if (amountString == null || amountString.trim().length() == 0) {

					Toast toast = Toast.makeText(CostActivity.this,
							getText(R.string.error_amount_required),
							Toast.LENGTH_SHORT);
					toast.show();
					return;
				}
				Double amount = Double.parseDouble(amountString);

				Dude payer = (Dude) payers.getSelectedItem();

				List<Dude> participants = new ArrayList<Dude>();
				for (Entry<CheckBox, Dude> entry : checkboxes.entrySet()) {
					if (entry.getKey().isChecked()) {
						participants.add(entry.getValue());
					}
				}

				if (participants.isEmpty()) {
					Toast toast = Toast.makeText(CostActivity.this,
							getText(R.string.error_participant_required),
							Toast.LENGTH_SHORT);
					toast.show();
					return;
				}

				if (cost == null) {
					Holcost activeHolcost = HolcostService.getInstance()
							.getActiveHolcost();

					CostService.getInstance().createCost(name, amount, payer,
							participants, activeHolcost);
				} else {
					CostService.getInstance().updateCost(cost, name, amount,
							payer, participants);
				}
				setResult(RESULT_OK);
				finish();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		Long costId = getIntent().getLongExtra("costId", -1);
		if (costId > -1) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.cost_menu, menu);
			return true;
		}
		return false;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Long costId = getIntent().getLongExtra("costId", -1);
		switch (item.getItemId()) {
		case R.id.deleteCostMenu:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_delete_cost)
					.setCancelable(true)
					.setPositiveButton(R.string.alert_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									CostService.getInstance()
											.deleteCost(costId);
									setResult(RESULT_OK);
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
		default:
			return false;
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.cost;
	}
}
package cat.aubricoc.holcost.activity;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.DebtService;
import cat.aubricoc.holcost.service.DudeService;

public class DudeActivity extends Activity {

	private DudeService dudeService = new DudeService(this);
	private CostService costService = new CostService(this);
	private DebtService debtService = new DebtService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dude);

		Long dudeId = getIntent().getLongExtra("dudeId", -1);

		final Dude dude = dudeService.getDudeById(dudeId);
		Debt debt = debtService.getDudeDebt(dude);

		TextView title = (TextView) findViewById(R.id.dudeTitle);
		title.setText(dude.getName());

		TextView debtDebt = (TextView) findViewById(R.id.debtDebt);
		TextView debtSpent = (TextView) findViewById(R.id.debtSpent);
		TextView debtPayed = (TextView) findViewById(R.id.debtPayed);

		String debtText = getText(
				(debt.getDebtAmount().doubleValue() > 0) ? R.string.has_debt
						: R.string.has_nodebt).toString();
		debtText = debtText.replaceAll("\\{0\\}", debt.getDude().getName());
		debtText = debtText.replaceAll("\\{1\\}",
				String.valueOf(Math.abs(debt.getDebtAmount())));
		debtDebt.setText(debtText);
		debtPayed.setText(getText(R.string.debt_payed) + " "
				+ debt.getPayedAmount());
		debtSpent.setText(getText(R.string.debt_spent) + " "
				+ debt.getSpentAmount());

		final List<Cost> payedCosts = costService.getCostsByPayer(dudeId);
		final ListView payedCostsList = (ListView) findViewById(R.id.listPayedCosts);
		if (!payedCosts.isEmpty()) {
			payedCostsList.setAdapter(new ArrayAdapter<Cost>(this,
					R.layout.list_line, payedCosts));
			payedCostsList.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					Intent intent = new Intent(DudeActivity.this,
							CostActivity.class);
					intent.putExtra("costId", payedCosts.get(position).getId());
					startActivityForResult(intent, 0);
				}
			});
		}

		final List<Cost> participantCosts = costService
				.getCostsByParticipant(dudeId);
		final ListView participantCostsList = (ListView) findViewById(R.id.listParticipantCosts);
		if (!participantCosts.isEmpty()) {
			participantCostsList.setAdapter(new ArrayAdapter<Cost>(this,
					R.layout.list_line, participantCosts));
			participantCostsList
					.setOnItemClickListener(new OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							Intent intent = new Intent(DudeActivity.this,
									CostActivity.class);
							intent.putExtra("costId",
									participantCosts.get(position).getId());
							startActivityForResult(intent, 0);
						}
					});
		}

		final TextView payedsLabel = (TextView) findViewById(R.id.payedCostsLabel);
		final TextView participantsLabel = (TextView) findViewById(R.id.participantCostsLabel);
		if (!payedCosts.isEmpty()) {
			payedsLabel.setVisibility(View.VISIBLE);
			payedCostsList.setVisibility(View.VISIBLE);
		} else if (!participantCosts.isEmpty()) {
			participantsLabel.setVisibility(View.VISIBLE);
			participantCostsList.setVisibility(View.VISIBLE);
		}

		if (!payedCosts.isEmpty() && !participantCosts.isEmpty()) {
			final Button showPayedButton = (Button) findViewById(R.id.viewPayedCosts);
			final Button showParticipantButton = (Button) findViewById(R.id.viewParticipantCosts);
			showParticipantButton.setVisibility(View.VISIBLE);
			showPayedButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					participantsLabel.setVisibility(View.GONE);
					participantCostsList.setVisibility(View.GONE);
					showParticipantButton.setVisibility(View.VISIBLE);
					payedsLabel.setVisibility(View.VISIBLE);
					payedCostsList.setVisibility(View.VISIBLE);
					showPayedButton.setVisibility(View.GONE);
				}
			});
			showParticipantButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					payedsLabel.setVisibility(View.GONE);
					payedCostsList.setVisibility(View.GONE);
					showPayedButton.setVisibility(View.VISIBLE);
					participantsLabel.setVisibility(View.VISIBLE);
					participantCostsList.setVisibility(View.VISIBLE);
					showParticipantButton.setVisibility(View.GONE);
				}
			});
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.dude_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Long dudeId = getIntent().getLongExtra("dudeId", -1);
		switch (item.getItemId()) {
		case R.id.deleteDudeMenu:
			if (dudeService.dudeHavePayedCosts(dudeId)) {
				Toast toast = Toast.makeText(this,
						getText(R.string.error_dude_have_payed_costs), 3);
				toast.show();
				return false;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_delete_dude)
					.setCancelable(true)
					.setPositiveButton(R.string.alert_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dudeService.deleteDude(dudeId);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setResult(resultCode);
			finish();
			startActivity(getIntent());
		}
	}
}
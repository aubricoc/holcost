package cat.aubricoc.holcost.activity;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.DebtService;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.util.Constants;

public class DudeActivity extends Activity {

	@Override
	public void onCreate() {
		
		backButtonInActionBar = true;
		
		Long dudeId = getIntent().getLongExtra(Constants.EXTRA_DUDE_ID, -1);

		final Dude dude = DudeService.getInstance().getDudeById(dudeId);
		Debt debt = DebtService.getInstance().getDudeDebt(dude);

		setTitle(dude.getName());

		String debtText = getText(
				(debt.getDebtAmount().doubleValue() > 0) ? R.string.has_debt
						: R.string.has_nodebt).toString();
		debtText = debtText.replaceAll("\\{0\\}", debt.getDude().getName());
		debtText = debtText.replaceAll("\\{1\\}",
				String.valueOf(Math.abs(debt.getDebtAmount())));
		setText(R.id.debtDebt, debtText);
		setText(R.id.debtPayed, getText(R.string.debt_payed) + " "
				+ debt.getPayedAmount());
		setText(R.id.debtSpent, getText(R.string.debt_spent) + " "
				+ debt.getSpentAmount());

		final List<Cost> payedCosts = CostService.getInstance().getCostsByPayer(dudeId);
		setList(R.id.listPayedCosts, payedCosts,
				R.layout.list_item);
		onItemClick(R.id.listPayedCosts, new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = newIntent(
						CostActivity.class);
				intent.putExtra(Constants.EXTRA_COST_ID, payedCosts.get(position).getId());
				goToForResult(intent, 0);
			}
		});

		final List<Cost> participantCosts = CostService.getInstance()
				.getCostsByParticipant(dudeId);
			setList(R.id.listParticipantCosts, participantCosts,
					R.layout.list_item);
			onItemClick(R.id.listParticipantCosts, new OnItemClickListener() {
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {

							Intent intent = newIntent(
									CostActivity.class);
							intent.putExtra(Constants.EXTRA_COST_ID,
									participantCosts.get(position).getId());
							goToForResult(intent, 0);
						}
					});

		if (!payedCosts.isEmpty()) {
			show(R.id.payedCostsLabel);
			show(R.id.listPayedCosts);
		} else if (!participantCosts.isEmpty()) {
			show(R.id.participantCostsLabel);
			show(R.id.listParticipantCosts);
		}

		if (!payedCosts.isEmpty() && !participantCosts.isEmpty()) {
			show(R.id.viewParticipantCosts);
			onClick(R.id.viewPayedCosts, new OnClickListener() {
				public void onClick(View v) {
					show(R.id.payedCostsLabel);
					show(R.id.listPayedCosts);
					hide(R.id.participantCostsLabel);
					hide(R.id.listParticipantCosts);
					
					hide(R.id.viewPayedCosts);
					show(R.id.viewParticipantCosts);
				}
			});
			onClick(R.id.viewParticipantCosts, new OnClickListener() {
				public void onClick(View v) {
					hide(R.id.payedCostsLabel);
					hide(R.id.listPayedCosts);
					show(R.id.participantCostsLabel);
					show(R.id.listParticipantCosts);
					
					show(R.id.viewPayedCosts);
					hide(R.id.viewParticipantCosts);
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
		final Long dudeId = getIntent().getLongExtra(Constants.EXTRA_DUDE_ID, -1);
		switch (item.getItemId()) {
		case R.id.deleteDudeMenu:
			if (DudeService.getInstance().dudeHavePayedCosts(dudeId)) {
				showToast(R.string.error_dude_have_payed_costs);
				return false;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(R.string.alert_delete_dude)
					.setCancelable(true)
					.setPositiveButton(R.string.alert_yes,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									DudeService.getInstance().deleteDude(dudeId);
									setResult(RESULT_OK);
									back();
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
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			setResult(resultCode);
			refreshOnBack();
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_dude;
	}
}
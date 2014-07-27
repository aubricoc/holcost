package cat.aubricoc.holcost.activity;

import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.HolcostService;
import cat.aubricoc.holcost.util.Constants;

public class ListCostActivity extends Activity {

	@Override
	public void onCreate() {
		
		setTitle(R.string.list_cost_title);
		backButtonInActionBar = true;
		
		final Holcost activeHolcost = HolcostService.getInstance()
				.getActiveHolcost();

		final List<Cost> costs = CostService.getInstance().getCostsByHolcost(
				activeHolcost);

		setList(R.id.listCost, costs, R.layout.list_item);
		onItemClick(R.id.listCost, new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = newIntent(CostActivity.class);
				intent.putExtra(Constants.EXTRA_COST_ID, costs.get(position).getId());
				goToForResult(intent, 0);
			}
		});
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
		return R.layout.activity_list_cost;
	}
}
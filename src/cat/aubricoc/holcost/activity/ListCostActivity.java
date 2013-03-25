package cat.aubricoc.holcost.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;
import cat.aubricoc.holcost.service.HolcostService;

public class ListCostActivity extends Activity {

	private HolcostService holcostService = new HolcostService(this);
	private CostService costService = new CostService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_cost);

		final Holcost activeHolcost = holcostService.getActiveHolcost();
		
		ListView listView = (ListView) findViewById(R.id.listCost);
		
		final List<Cost> costs = costService.getCostsByHolcost(activeHolcost);

		listView.setAdapter(new ArrayAdapter<Cost>(this, R.layout.list_line,
				costs));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Intent intent = new Intent(ListCostActivity.this, CostActivity.class);
				intent.putExtra("costId", costs.get(position).getId());
				startActivityForResult(intent, 0);
			}
		});
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
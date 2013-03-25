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
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.HolcostService;

public class ListHolcostActivity extends Activity {

	private HolcostService holcostService = new HolcostService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.list_holcost);

		final List<Holcost> holcosts = holcostService.getAllHolcosts();
		
		ListView listView = (ListView) findViewById(R.id.listHolcost);

		listView.setAdapter(new ArrayAdapter<Holcost>(this, R.layout.list_line,
				holcosts));
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				Holcost holcost = holcosts.get(position);
				holcostService.openHolcost(holcost);
				
				setResult(RESULT_OK);
				
				Intent intent = new Intent(ListHolcostActivity.this,
						HolcostActivity.class);
				startActivity(intent);
				
				finish();
			}
		});
	}
}
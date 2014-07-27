package cat.aubricoc.holcost.activity;

import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.db.DataLoader;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.HolcostService;

public class CreateHolcostActivity extends Activity {

	@Override
	public void onCreate() {

		setTitle(R.string.create_holcost_title);

		onClick(R.id.createHolcostButton, new OnClickListener() {
			public void onClick(View v) {

				String name = getInputText(R.id.holcostName);

				if (name == null || name.trim().length() == 0) {

					showToast(R.string.error_name_required);

				} else if (name.equals("JSON2DB")) {
					DataLoader.getInstance().loadData();

				} else {

					HolcostService.getInstance().createHolcost(name);

					goToAndFinish(HolcostActivity.class);
				}
			}
		});

		final List<Holcost> holcosts = HolcostService.getInstance()
				.getAllHolcosts();
		if (!holcosts.isEmpty()) {

			show(R.id.listHolcostTitle);
			show(R.id.listHolcost);
			setList(R.id.listHolcost, holcosts, R.layout.list_item);
			onItemClick(R.id.listHolcost, new OnItemClickListener() {
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {

					Holcost holcost = holcosts.get(position);
					HolcostService.getInstance().openHolcost(holcost);

					goToAndFinish(HolcostActivity.class);
				}
			});
		}
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_create_holcost;
	}
}
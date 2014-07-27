package cat.aubricoc.holcost.activity;

import android.view.View;
import android.view.View.OnClickListener;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.service.HolcostService;

public class CreateDudeActivity extends Activity {

	@Override
	public void onCreate() {

		setTitle(R.string.create_dude_title);
		backButtonInActionBar = true;

		onClick(R.id.createDudeButton, new OnClickListener() {
			public void onClick(View v) {

				String name = getInputText(R.id.dudeName);

				if (name == null || name.trim().length() == 0) {

					showToast(R.string.error_name_required);

				} else {

					Holcost activeHolcost = HolcostService.getInstance()
							.getActiveHolcost();

					if (DudeService.getInstance().existsDude(name,
							activeHolcost)) {
						showToast(R.string.error_dude_duplicated);
					} else {
						DudeService.getInstance().createDude(name,
								activeHolcost);
						setResult(RESULT_OK);
						back();
					}
				}
			}
		});
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_create_dude;
	}
}
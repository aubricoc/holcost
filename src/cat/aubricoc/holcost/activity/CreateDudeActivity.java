package cat.aubricoc.holcost.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.DudeService;
import cat.aubricoc.holcost.service.HolcostService;

public class CreateDudeActivity extends Activity {

	private HolcostService holcostService = new HolcostService(this);
	private DudeService dudeService = new DudeService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_dude);

		Button saveButton = (Button) findViewById(R.id.createDudeButton);

		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				EditText nameInput = (EditText) findViewById(R.id.dudeName);
				String name = nameInput.getText().toString();

				if (name == null || name.trim().length() == 0) {

					Toast toast = Toast.makeText(CreateDudeActivity.this,
							getText(R.string.error_name_required), 3);
					toast.show();

				} else {

					Holcost activeHolcost = holcostService.getActiveHolcost();

					if (dudeService.existsDudeName(name, activeHolcost)) {
						Toast toast = Toast.makeText(CreateDudeActivity.this,
								getText(R.string.error_dude_duplicated), 3);
						toast.show();
					} else {
						dudeService.createDude(name, activeHolcost);
						setResult(RESULT_OK);
						finish();
					}
				}
			}
		});
	}
}
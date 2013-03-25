package cat.aubricoc.holcost.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.service.HolcostService;

public class CreateHolcostActivity extends Activity {

	private HolcostService holcostService = new HolcostService(this);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_holcost);

		Button saveButton = (Button) findViewById(R.id.createHolcostButton);

		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

				EditText nameInput = (EditText) findViewById(R.id.holcostName);
				String name = nameInput.getText().toString();

				if (name == null || name.trim().length() == 0) {

					Toast toast = Toast.makeText(CreateHolcostActivity.this,
							getText(R.string.error_name_required), 3);
					toast.show();

				} else {

					holcostService.createHolcost(name);

					Intent intent = new Intent(CreateHolcostActivity.this,
							HolcostActivity.class);
					startActivity(intent);

					finish();
				}
			}
		});

		if (holcostService.existsHolcosts()) {

			Button listButton = (Button) findViewById(R.id.listHolcostButton);
			listButton.setVisibility(View.VISIBLE);
			listButton.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent(CreateHolcostActivity.this,
							ListHolcostActivity.class);
					startActivityForResult(intent, 0);
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
}
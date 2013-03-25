package cat.aubricoc.holcost.view;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cat.aubricoc.holcost.model.Debt;

public class DebtListAdapter extends ArrayAdapter<Debt> {

	private int layoutDebtId;
	private int layoutNoDebtId;
	private LayoutInflater mInflater;
	private String textDebt;
	private String textNoDebt;

	public DebtListAdapter(Context context, int layoutDebtId,
			int layoutNoDebtId, List<Debt> objects, String textDebt,
			String textNoDebt) {
		super(context, layoutDebtId, objects);
		this.layoutDebtId = layoutDebtId;
		this.layoutNoDebtId = layoutNoDebtId;
		this.mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.textDebt = textDebt;
		this.textNoDebt = textNoDebt;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return createViewFromResource(position, convertView, parent);
	}

	private View createViewFromResource(int position, View convertView,
			ViewGroup parent) {
		View view;
		TextView text;

		Debt debt = getItem(position);

		if (convertView == null) {
			int resource = (debt.getDebtAmount().doubleValue() > 0) ? layoutDebtId
					: layoutNoDebtId;
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}

		try {
			text = (TextView) view;
		} catch (ClassCastException e) {
			Log.e("ArrayAdapter",
					"You must supply a resource ID for a TextView");
			throw new IllegalStateException(
					"ArrayAdapter requires the resource ID to be a TextView", e);
		}

		String textView = (debt.getDebtAmount().doubleValue() > 0) ? textDebt
				: textNoDebt;
		textView = textView.replaceAll("\\{0\\}", debt.getDude().getName());
		textView = textView.replaceAll("\\{1\\}",
				String.valueOf(Math.abs(debt.getDebtAmount())));
		text.setText(textView);

		return view;
	}
}

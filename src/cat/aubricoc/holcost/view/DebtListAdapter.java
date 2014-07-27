package cat.aubricoc.holcost.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import cat.aubricoc.holcost.model.Debt;

public class DebtListAdapter extends ArrayAdapter<Debt> {

	private int layoutDebtId;
	private int layoutNoDebtId;
	private LayoutInflater inflater;
	private String textDebt;
	private String textNoDebt;

	public DebtListAdapter(Context context, int layoutDebtId,
			int layoutNoDebtId, List<Debt> objects, String textDebt,
			String textNoDebt) {
		super(context, layoutDebtId, objects);
		this.layoutDebtId = layoutDebtId;
		this.layoutNoDebtId = layoutNoDebtId;
		this.inflater = (LayoutInflater) context
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

		Debt debt = getItem(position);

		int resource = (debt.getDebtAmount().doubleValue() > 0) ? layoutDebtId
				: layoutNoDebtId;
		TextView view = (TextView) inflater.inflate(resource, parent, false);

		String textView = (debt.getDebtAmount().doubleValue() > 0) ? textDebt
				: textNoDebt;
		textView = textView.replaceAll("\\{0\\}", debt.getDude().getName());
		textView = textView.replaceAll("\\{1\\}",
				String.valueOf(Math.abs(debt.getDebtAmount())));
		view.setText(textView);

		return view;
	}
}

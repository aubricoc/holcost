package cat.aubricoc.holcost.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.util.SparseArray;
import cat.aubricoc.holcost.R;
import cat.aubricoc.holcost.dao.DudeDao;
import cat.aubricoc.holcost.dao.HolcostDao;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.Holcost;
import cat.aubricoc.holcost.service.CostService;

public class DataLoader {

	private Context context;

	private HolcostDao holcostDao;

	private DudeDao dudeDao;

	private CostService costService;

	public DataLoader(Context context) {
		this.context = context;
		this.holcostDao = new HolcostDao(context);
		this.dudeDao = new DudeDao(context);
		this.costService = new CostService(context);
	}

	public void loadData() {

		if (!holcostDao.getAll().isEmpty()) {
			return;
		}
		
		try {
			String json = toString(context.getResources().openRawResource(
					R.raw.data));

			JSONArray jsonHolcosts = new JSONArray(json);

			for (int iter = 0; iter < jsonHolcosts.length(); iter++) {
				JSONObject jsonHolcost = jsonHolcosts.getJSONObject(iter);
				Holcost holcost = new Holcost();
				holcost.setName(jsonHolcost.getString("name"));
				holcost.setActive(false);
				long holcostId = holcostDao.create(holcost);
				holcost.setId(holcostId);

				SparseArray<Dude> dudes = new SparseArray<Dude>();
				JSONArray jsonDudes = jsonHolcost.getJSONArray("dudes");
				for (int iter2 = 0; iter2 < jsonDudes.length(); iter2++) {
					JSONObject jsonDude = jsonDudes.getJSONObject(iter2);
					Dude dude = new Dude();
					dude.setName(jsonDude.getString("name"));
					dude.setHolcostId(holcostId);
					long dudeId = dudeDao.create(dude);
					dude.setId(dudeId);
					dudes.put(jsonDude.getInt("id"), dude);
				}

				JSONArray jsonCosts = jsonHolcost.getJSONArray("costs");
				for (int iter2 = 0; iter2 < jsonCosts.length(); iter2++) {
					JSONObject jsonCost = jsonCosts.getJSONObject(iter2);
					String name = jsonCost.getString("name");
					Double amount = jsonCost.getDouble("amount");
					Dude payer = dudes.get(jsonCost.getInt("payer"));
					List<Dude> participants = new ArrayList<Dude>();
					JSONArray jsonParticipants = jsonCost
							.getJSONArray("participants");
					for (int iter3 = 0; iter3 < jsonParticipants.length(); iter3++) {
						int participant = jsonParticipants.getInt(iter3);
						participants.add(dudes.get(participant));
					}
					costService.createCost(name, amount, payer, participants,
							holcost);
				}
			}

		} catch (Exception e) {
			Log.e("DataLoader", "Error loading data", e);
		}

	}

	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static String toString(InputStream input) throws IOException {
		StringWriter sw = new StringWriter();
		copy(input, sw);
		return sw.toString();
	}

	public static void copy(InputStream input, Writer output)
			throws IOException {
		InputStreamReader in = new InputStreamReader(input);
		copy(in, output);
	}

	public static int copy(Reader input, Writer output) throws IOException {
		long count = copyLarge(input, output);
		if (count > Integer.MAX_VALUE) {
			return -1;
		}
		return (int) count;
	}

	public static long copyLarge(Reader input, Writer output)
			throws IOException {
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return count;
	}
}
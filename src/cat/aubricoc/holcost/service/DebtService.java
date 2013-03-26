package cat.aubricoc.holcost.service;

import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;

public class DebtService {

	private CostDao costDao;
	private DudeCostDao dudeCostDao;

	public DebtService(Context context) {
		costDao = new CostDao(context);
		dudeCostDao = new DudeCostDao(context);
	}

	public Debt getDudeDebt(Dude dude) {
		Debt debt = new Debt();
		debt.setDude(dude);

		Double debtAmount = 0D;
		Double payedAmount = 0D;
		Double spentAmount = 0D;

		List<Cost> payedCosts = costDao.getByPayerAndRemoved(dude.getId(),
				false);
		for (Cost cost : payedCosts) {
			payedAmount += cost.getAmount();
		}

		List<DudeCost> dudeCosts = dudeCostDao.getByDudeAndRemoved(
				dude.getId(), false);

		for (DudeCost dudeCost : dudeCosts) {
			Cost cost = costDao.getById(dudeCost.getCost());
			List<DudeCost> participants = dudeCostDao.getByCostAndRemoved(
					cost.getId(), false);
			spentAmount += (cost.getAmount() / participants.size());
		}

		debtAmount = spentAmount - payedAmount;

		debt.setPayedAmount(Math.round(payedAmount * 100) / 100D);
		debt.setSpentAmount(Math.round(spentAmount * 100) / 100D);
		debt.setDebtAmount(Math.round(debtAmount * 100) / 100D);

		return debt;
	}
}

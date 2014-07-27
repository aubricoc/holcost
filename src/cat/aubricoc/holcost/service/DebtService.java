package cat.aubricoc.holcost.service;

import java.util.List;

import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Debt;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;

public class DebtService {

	private static final DebtService INSTANCE = new DebtService();

	private DebtService() {
		super();
	}

	public static DebtService getInstance() {
		return INSTANCE;
	}

	public Debt getDudeDebt(Dude dude) {
		Debt debt = new Debt();
		debt.setDude(dude);

		Double debtAmount = 0D;
		Double payedAmount = 0D;
		Double spentAmount = 0D;

		List<Cost> payedCosts = CostDao.getInstance().getByPayer(dude.getId());
		for (Cost cost : payedCosts) {
			payedAmount += cost.getAmount();
		}

		List<DudeCost> dudeCosts = DudeCostDao.getInstance().getByDude(
				dude.getId());

		for (DudeCost dudeCost : dudeCosts) {
			Cost cost = CostDao.getInstance().getById(
					dudeCost.getCost().getId());
			List<DudeCost> participants = DudeCostDao.getInstance().getByCost(
					cost.getId());
			spentAmount += (cost.getAmount() / participants.size());
		}

		debtAmount = spentAmount - payedAmount;

		debt.setPayedAmount(Math.round(payedAmount * 100) / 100D);
		debt.setSpentAmount(Math.round(spentAmount * 100) / 100D);
		debt.setDebtAmount(Math.round(debtAmount * 100) / 100D);

		return debt;
	}
}

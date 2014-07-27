package cat.aubricoc.holcost.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;

public class CostService {

	private static final CostService INSTANCE = new CostService();

	private CostService() {
		super();
	}

	public static CostService getInstance() {
		return INSTANCE;
	}

	public void createCost(String name, Double amount, Dude payer,
			List<Dude> participants, Holcost holcost) {

		Cost cost = new Cost();
		cost.setName(name);
		cost.setAmount(amount);
		cost.setDate(new Date());
		cost.setPayer(payer);
		cost.setHolcost(holcost);

		CostDao.getInstance().create(cost);

		for (Dude participant : participants) {
			DudeCost dudeCost = new DudeCost();
			dudeCost.setDude(participant);
			dudeCost.setCost(cost);

			DudeCostDao.getInstance().create(dudeCost);
		}
	}

	public List<Cost> getCostsByHolcost(Holcost holcost) {
		return CostDao.getInstance().getByHolcost(holcost.getId());
	}

	public boolean existsCosts(Holcost holcost) {
		return !getCostsByHolcost(holcost).isEmpty();
	}

	public Cost getCostById(Long costId) {
		if (costId < 0) {
			return null;
		}
		return CostDao.getInstance().getById(costId);
	}

	public List<Dude> getParticipants(Cost cost) {
		List<DudeCost> dudeCosts = DudeCostDao.getInstance().getByCost(
				cost.getId());
		List<Dude> dudes = new ArrayList<Dude>();
		for (DudeCost dudeCost : dudeCosts) {
			dudes.add(dudeCost.getDude());
		}
		return dudes;
	}

	public void updateCost(Cost cost, String name, Double amount, Dude payer,
			List<Dude> participants) {
		cost.setName(name);
		cost.setAmount(amount);
		cost.setPayer(payer);

		CostDao.getInstance().update(cost);

		List<DudeCost> oldDudeCosts = DudeCostDao.getInstance().getByCost(
				cost.getId());
		for (Dude participant : participants) {
			boolean create = true;
			for (DudeCost oldDudeCost : oldDudeCosts) {
				if (participant.getId().equals(oldDudeCost.getDude().getId())) {
					create = false;
				}
			}
			if (create) {
				DudeCost dudeCost = new DudeCost();
				dudeCost.setDude(participant);
				dudeCost.setCost(cost);

				DudeCostDao.getInstance().create(dudeCost);
			}
		}

		for (DudeCost oldDudeCost : oldDudeCosts) {
			boolean remove = true;
			for (Dude participant : participants) {
				if (participant.getId().equals(oldDudeCost.getDude().getId())) {
					remove = false;
				}
			}
			if (remove) {
				DudeCostDao.getInstance().delete(oldDudeCost);
			}
		}
	}

	public void deleteCost(Long costId) {
		Cost cost = new Cost();
		cost.setId(costId);
		CostDao.getInstance().delete(cost);
	}

	public List<Cost> getCostsByPayer(Long payerId) {
		return CostDao.getInstance().getByPayer(payerId);
	}

	public List<Cost> getCostsByParticipant(Long dudeId) {
		List<DudeCost> dudeCosts = DudeCostDao.getInstance().getByDude(dudeId);
		List<Cost> costs = new ArrayList<Cost>();
		for (DudeCost dudeCost : dudeCosts) {
			costs.add(CostDao.getInstance().getById(dudeCost.getCost().getId()));
		}
		return costs;
	}
}

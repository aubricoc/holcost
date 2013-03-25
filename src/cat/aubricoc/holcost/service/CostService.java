package cat.aubricoc.holcost.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;

public class CostService {

	private CostDao costDao;
	private DudeCostDao dudeCostDao;

	public CostService(Context context) {
		costDao = new CostDao(context);
		dudeCostDao = new DudeCostDao(context);
	}

	public void createCost(String name, Double amount, Dude payer,
			List<Dude> participants, Holcost holcost) {

		Cost cost = new Cost();
		cost.setName(name);
		cost.setAmount(amount);
		cost.setDate(new Date());
		cost.setPayer(payer);
		cost.setHolcostId(holcost.getId());
		
		costDao.create(cost);
		
		for (Dude participant : participants) {
			DudeCost dudeCost = new DudeCost();
			dudeCost.setDude(participant);
			dudeCost.setCost(cost);
			
			dudeCostDao.create(dudeCost);
		}
	}
	
	public List<Cost> getCostsByHolcost(Holcost holcost) {
		return costDao.getByHolcost(holcost.getId());
	}
	
	public boolean existsCosts(Holcost holcost) {
		return !getCostsByHolcost(holcost).isEmpty();
	}
	
	public Cost getCostById(Long costId) {
		if (costId < 0) {
			return null;
		}
		Cost cost = new Cost();
		cost.setId(costId);
		return costDao.getById(cost);
	}

	public List<Dude> getParticipants(Cost cost) {
		List<DudeCost> dudeCosts = dudeCostDao.getByCost(cost.getId());
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
		
		costDao.update(cost);
		
		List<DudeCost> oldDudeCosts = dudeCostDao.getByCost(cost.getId());
		for (Dude participant : participants) {
			boolean create = true;
			for (DudeCost oldDudeCost : oldDudeCosts){
				if (participant.getId().equals(oldDudeCost.getDude().getId())) {
					create = false;
				}
			}
			if (create) {
				DudeCost dudeCost = new DudeCost();
				dudeCost.setDude(participant);
				dudeCost.setCost(cost);
				
				dudeCostDao.create(dudeCost);
			}
		}
		
		for (DudeCost oldDudeCost : oldDudeCosts){
			boolean remove = true;
			for (Dude participant : participants) {
				if (participant.getId().equals(oldDudeCost.getDude().getId())) {
					remove = false;
				}
			}
			if (remove) {
				dudeCostDao.delete(oldDudeCost);
			}
		}
	}

	public void deleteCost(Long costId) {
		Cost cost = new Cost();
		cost.setId(costId);
		costDao.delete(cost);
	}
	
	public List<Cost> getCostsByPayer(Long payerId) {
		return costDao.getByPayer(payerId);
	}

	public List<Cost> getCostsByParticipant(Long dudeId) {
		List<DudeCost> dudeCosts = dudeCostDao.getByDude(dudeId);
		List<Cost> costs = new ArrayList<Cost>();
		for (DudeCost dudeCost : dudeCosts) {
			costs.add(costDao.getById(dudeCost.getCost()));
		}
		return costs;
	}
}

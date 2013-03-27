package cat.aubricoc.holcost.service;

import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.dao.DudeDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;

public class DudeService {

	private DudeDao dudeDao;
	private CostDao costDao;
	private DudeCostDao dudeCostDao;
	
	public DudeService(Context context) {
		dudeDao = new DudeDao(context);
		costDao = new CostDao(context);
		dudeCostDao = new DudeCostDao(context);
	}
	
	public void createDude(String name, Holcost holcost) {
		Dude dude = new Dude();
		dude.setName(name);
		dude.setHolcostId(holcost.getId());
		dude.setEmail(null);
		dude.setIsUser(false);
		dude.setPendingChanges(true);
		dude.setRemoved(false);
		dude.setServerId(null);
		dudeDao.create(dude);
	}

	public List<Dude> getDudesByHolcost(Holcost holcost) {
		return dudeDao.getByHolcostAndRemoved(holcost.getId(), false);
	}

	public boolean existsDudeName(String name, Holcost holcost) {
		List<Dude> dudes = getDudesByHolcost(holcost);
		for (Dude dude : dudes) {
			if (dude.getName().trim().equalsIgnoreCase(name.trim())) {
				return true;
			}
		}
		return false;
	}

	public Dude getDudeById(Long dudeId) {
		Dude dude = new Dude();
		dude.setId(dudeId);
		return dudeDao.getById(dude);
	}

	public void deleteDude(Long dudeId) {
		Dude dude = getDudeById(dudeId);
		dude.setId(dudeId);
		dude.setRemoved(true);
		dude.setPendingChanges(true);
		dudeDao.update(dude);
		
		List<DudeCost> dudeCosts = dudeCostDao.getByDudeAndRemoved(dudeId, false);
		for (DudeCost dudeCost : dudeCosts) {
			dudeCost.setPendingChanges(true);
			dudeCost.setRemoved(true);
			dudeCostDao.update(dudeCost);
		}
	}

	public boolean dudeHavePayedCosts(Long dudeId) {
		List<Cost> payedCosts = costDao.getByPayerAndRemoved(dudeId, false);
		return !payedCosts.isEmpty();
	}
}

package cat.aubricoc.holcost.service;

import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.Holcost;

public class DudeService {

	private DudeDao dudeDao;
	private CostDao costDao;
	
	public DudeService(Context context) {
		dudeDao = new DudeDao(context);
		costDao = new CostDao(context);
	}
	
	public void createDude(String name, Holcost holcost) {
		Dude dude = new Dude();
		dude.setName(name);
		dude.setHolcostId(holcost.getId());
		dudeDao.create(dude);
	}

	public List<Dude> getDudesByHolcost(Holcost holcost) {
		return dudeDao.getByHolcost(holcost.getId());
	}

	public boolean existsDude(String name, Holcost holcost) {
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
		Dude dude = new Dude();
		dude.setId(dudeId);
		dudeDao.delete(dude);
	}

	public boolean dudeHavePayedCosts(Long dudeId) {
		List<Cost> payedCosts = costDao.getByPayer(dudeId);
		return !payedCosts.isEmpty();
	}
}

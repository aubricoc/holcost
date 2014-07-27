package cat.aubricoc.holcost.service;

import java.util.List;

import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.dao.DudeDao;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;

public class DudeService {

	private static final DudeService INSTANCE = new DudeService();

	private DudeService() {
		super();
	}

	public static DudeService getInstance() {
		return INSTANCE;
	}

	public void createDude(String name, Holcost holcost) {
		Dude dude = new Dude();
		dude.setName(name);
		dude.setHolcost(holcost);
		DudeDao.getInstance().create(dude);
	}

	public List<Dude> getDudesByHolcost(Holcost holcost) {
		return DudeDao.getInstance().getByHolcost(holcost.getId());
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
		return DudeDao.getInstance().getById(dudeId);
	}

	public void deleteDude(Long dudeId) {
		Dude dude = new Dude();
		dude.setId(dudeId);
		List<DudeCost> dudeCosts = DudeCostDao.getInstance().getByDude(dudeId);
		DudeCostDao.getInstance().delete(dudeCosts);
		DudeDao.getInstance().delete(dude);
	}

	public boolean dudeHavePayedCosts(Long dudeId) {
		List<Cost> payedCosts = CostDao.getInstance().getByPayer(dudeId);
		return !payedCosts.isEmpty();
	}
}

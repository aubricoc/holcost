package cat.aubricoc.holcost.service;

import java.util.List;

import cat.aubricoc.holcost.dao.HolcostDao;
import cat.aubricoc.holcost.model.Holcost;

public class HolcostService {

	private static final HolcostService INSTANCE = new HolcostService();

	private HolcostService() {
		super();
	}

	public static HolcostService getInstance() {
		return INSTANCE;
	}

	public Holcost getActiveHolcost() {
		return HolcostDao.getInstance().getActiveHolcost();
	}

	public void createHolcost(String name) {

		closeActiveHolcost();

		Holcost holcost = new Holcost();
		holcost.setName(name);
		holcost.setActive(true);

		HolcostDao.getInstance().create(holcost);
	}

	public void closeActiveHolcost() {
		Holcost active = getActiveHolcost();
		if (active != null) {
			closeHolcost(getActiveHolcost());
		}
	}

	public void closeHolcost(Holcost holcost) {
		holcost.setActive(false);
		HolcostDao.getInstance().update(holcost);
	}

	public List<Holcost> getAllHolcosts() {
		return HolcostDao.getInstance().getAll();
	}

	public void openHolcost(Long id) {
		openHolcost(getHolcostById(id));
	}

	public void openHolcost(Holcost holcost) {
		holcost.setActive(true);
		HolcostDao.getInstance().update(holcost);
	}

	public Holcost getHolcostById(Long id) {
		return HolcostDao.getInstance().getById(id);
	}

	public boolean existsHolcosts() {
		return !getAllHolcosts().isEmpty();
	}

	public void deleteActiveHolcost() {
		deleteHolcost(getActiveHolcost());
	}

	public void deleteHolcost(Holcost holcost) {
		HolcostDao.getInstance().delete(holcost);
	}
}

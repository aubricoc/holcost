package cat.aubricoc.holcost.service;

import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.HolcostDao;
import cat.aubricoc.holcost.model.Holcost;

public class HolcostService {

	private HolcostDao holcostDao;

	public HolcostService(Context context) {
		holcostDao = new HolcostDao(context);
	}

	public Holcost getActiveHolcost() {
		List<Holcost> holcosts = holcostDao.getByActive(true);
		if (holcosts.isEmpty()) {
			return null;
		}
		return holcosts.get(0);
	}

	public void createHolcost(String name) {

		closeActiveHolcost();

		Holcost holcost = new Holcost();
		holcost.setName(name);
		holcost.setActive(true);
		holcost.setRemoved(false);
		holcost.setPendingChanges(true);
		holcost.setServerId(null);

		holcostDao.create(holcost);
	}

	public void closeActiveHolcost() {
		Holcost active = getActiveHolcost();
		if (active != null) {
			closeHolcost(getActiveHolcost());
		}
	}

	public void closeHolcost(Holcost holcost) {
		holcost.setActive(false);
		holcostDao.update(holcost);
	}

	public List<Holcost> getAllHolcosts() {
		return holcostDao.getByRemoved(false);
	}

	public void openHolcost(Long id) {
		openHolcost(getHolcostById(id));
	}

	public void openHolcost(Holcost holcost) {
		holcost.setActive(true);
		holcostDao.update(holcost);
	}

	public Holcost getHolcostById(Long id) {
		Holcost holcost = new Holcost();
		holcost.setId(id);
		return holcostDao.getById(holcost);
	}

	public boolean existsHolcosts() {
		return !getAllHolcosts().isEmpty();
	}

	public void deleteActiveHolcost() {
		deleteHolcost(getActiveHolcost());
	}

	public void deleteHolcost(Holcost holcost) {
		holcost.setRemoved(true);
		holcost.setActive(false);
		holcostDao.update(holcost);
	}
}

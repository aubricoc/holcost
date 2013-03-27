package cat.aubricoc.holcost.service;

import java.util.List;

import android.content.Context;
import cat.aubricoc.holcost.dao.CostDao;
import cat.aubricoc.holcost.dao.DudeCostDao;
import cat.aubricoc.holcost.dao.DudeDao;
import cat.aubricoc.holcost.dao.HolcostDao;
import cat.aubricoc.holcost.exception.ServerRequestException;
import cat.aubricoc.holcost.model.Cost;
import cat.aubricoc.holcost.model.Dude;
import cat.aubricoc.holcost.model.DudeCost;
import cat.aubricoc.holcost.model.Holcost;

public class SyncService {

	private HolcostDao holcostDao;
	private DudeDao dudeDao;
	private CostDao costDao;
	private DudeCostDao dudeCostDao;

	private ServerService serverService = new ServerService();

	public SyncService(Context context) {
		this.holcostDao = new HolcostDao(context);
		this.dudeDao = new DudeDao(context);
		this.costDao = new CostDao(context);
		this.dudeCostDao = new DudeCostDao(context);
	}

	public boolean havePendingChanges() {
		if (!holcostDao.getByPendingChanges(true).isEmpty()) {
			return true;
		}
		if (!dudeDao.getByPendingChanges(true).isEmpty()) {
			return true;
		}
		if (!costDao.getByPendingChanges(true).isEmpty()) {
			return true;
		}
		if (!dudeCostDao.getByPendingChanges(true).isEmpty()) {
			return true;
		}
		return false;
	}

	public boolean uploadData() {
		try {
			doCleans();
			doCreates();
			doUpdates();
			doRemoves();

		} catch (ServerRequestException e) {
			return false;
		}

		return true;
	}

	private void doCleans() {
		doHolcostsCleans();
		doDudesCleans();
		doCostsCleans();
	}

	private void doCreates() {
		doHolcostsCreates();
		doDudesCreates();
		doCostsCreates();
		doDudeCostsCreates();
	}

	private void doUpdates() {
		doHolcostsUpdates();
		doDudesUpdates();
		doCostsUpdates();
	}

	private void doRemoves() {
		doDudeCostsRemoves();
		doCostsRemoves();
		doDudesRemoves();
		doHolcostsRemoves();
	}

	private void doHolcostsCleans() {
		List<Holcost> holcosts = holcostDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, true);
		for (Holcost holcost : holcosts) {
			holcostDao.delete(holcost);
		}
	}

	private void doDudesCleans() {
		List<Dude> dudes = dudeDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, true);
		for (Dude dude : dudes) {
			dudeDao.delete(dude);
		}
	}

	private void doCostsCleans() {
		List<Cost> costs = costDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, true);
		for (Cost cost : costs) {
			costDao.delete(cost);
		}
	}

	private void doHolcostsCreates() {
		List<Holcost> holcosts = holcostDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false, true);
		for (Holcost holcost : holcosts) {
			Long serverId = serverService.createHolcost(holcost);
			holcost.setServerId(serverId);
			holcost.setPendingChanges(false);
			holcostDao.update(holcost);
		}
	}

	private void doDudesCreates() {
		List<Dude> dudes = dudeDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false, true);
		for (Dude dude : dudes) {
			Long serverId = serverService.createDude(dude);
			dude.setServerId(serverId);
			dude.setPendingChanges(false);
			dudeDao.update(dude);
		}
	}

	private void doCostsCreates() {
		List<Cost> costs = costDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false, true);
		for (Cost cost : costs) {
			Long serverId = serverService.createCost(cost);
			cost.setServerId(serverId);
			cost.setPendingChanges(false);
			costDao.update(cost);
		}
	}

	private void doDudeCostsCreates() {
		List<DudeCost> dudeCosts = dudeCostDao.getByPendingChangesAndRemoved(
				true, false);
		for (DudeCost dudeCost : dudeCosts) {
			serverService.createDudeCost(dudeCost);
			dudeCost.setPendingChanges(false);
			dudeCostDao.update(dudeCost);
		}
	}

	private void doHolcostsUpdates() {
		List<Holcost> holcosts = holcostDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false,
						false);
		for (Holcost holcost : holcosts) {
			serverService.updateHolcost(holcost);
			holcost.setPendingChanges(false);
			holcostDao.update(holcost);
		}
	}

	private void doDudesUpdates() {
		List<Dude> dudes = dudeDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false,
						false);
		for (Dude dude : dudes) {
			serverService.updateDude(dude);
			dude.setPendingChanges(false);
			dudeDao.update(dude);
		}
	}

	private void doCostsUpdates() {
		List<Cost> costs = costDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, false,
						false);
		for (Cost cost : costs) {
			serverService.updateCost(cost);
			cost.setPendingChanges(false);
			costDao.update(cost);
		}
	}

	private void doHolcostsRemoves() {
		List<Holcost> holcosts = holcostDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, false);
		for (Holcost holcost : holcosts) {
			holcost.setPendingChanges(false);
			holcostDao.update(holcost);
		}
	}

	private void doDudesRemoves() {
		List<Dude> dudes = dudeDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, false);
		for (Dude dude : dudes) {
			serverService.removeDude(dude);
			dudeDao.delete(dude);
		}
	}

	private void doCostsRemoves() {
		List<Cost> costs = costDao
				.getByPendingChangesAndRemovedAndServerIdNull(true, true, false);
		for (Cost cost : costs) {
			serverService.removeCost(cost);
			costDao.delete(cost);
		}
	}

	private void doDudeCostsRemoves() {
		List<DudeCost> dudeCosts = dudeCostDao.getByPendingChangesAndRemoved(
				true, true);
		for (DudeCost dudeCost : dudeCosts) {
			serverService.removeDudeCost(dudeCost);
			dudeCostDao.delete(dudeCost);
		}
	}
}

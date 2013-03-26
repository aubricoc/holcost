package cat.aubricoc.holcost.model;

public class DudeCost {

	private Dude dude;
	
	private Cost cost;
	
	private Boolean pendingChanges;
	
	private Boolean removed;

	public Dude getDude() {
		return dude;
	}

	public void setDude(Dude dude) {
		this.dude = dude;
	}

	public Cost getCost() {
		return cost;
	}

	public void setCost(Cost cost) {
		this.cost = cost;
	}

	public Boolean getPendingChanges() {
		return pendingChanges;
	}

	public void setPendingChanges(Boolean pendingChanges) {
		this.pendingChanges = pendingChanges;
	}

	public Boolean getRemoved() {
		return removed;
	}

	public void setRemoved(Boolean removed) {
		this.removed = removed;
	}
}

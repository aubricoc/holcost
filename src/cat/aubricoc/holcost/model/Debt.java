package cat.aubricoc.holcost.model;

public class Debt {

	private Dude dude;
	
	private Double payedAmount;
	
	private Double spentAmount;
	
	private Double debtAmount;

	public Dude getDude() {
		return dude;
	}

	public void setDude(Dude dude) {
		this.dude = dude;
	}

	public Double getPayedAmount() {
		return payedAmount;
	}

	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
	}

	public Double getSpentAmount() {
		return spentAmount;
	}

	public void setSpentAmount(Double spentAmount) {
		this.spentAmount = spentAmount;
	}

	public Double getDebtAmount() {
		return debtAmount;
	}

	public void setDebtAmount(Double debtAmount) {
		this.debtAmount = debtAmount;
	}
	
	@Override
	public String toString() {
		return this.dude.getName() + " ::: " + this.debtAmount;
	}
}

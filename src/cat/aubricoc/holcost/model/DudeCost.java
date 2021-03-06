package cat.aubricoc.holcost.model;

import com.canteratech.apa.annotation.Entity;
import com.canteratech.apa.annotation.Id;

@Entity
public class DudeCost {

	@Id
	private Dude dude;
	
	@Id
	private Cost cost;

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
}

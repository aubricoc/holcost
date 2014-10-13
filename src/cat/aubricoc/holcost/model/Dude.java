package cat.aubricoc.holcost.model;

import com.canteratech.apa.annotation.Column;
import com.canteratech.apa.annotation.Entity;
import com.canteratech.apa.annotation.GeneratedValue;
import com.canteratech.apa.annotation.Id;
import com.canteratech.apa.annotation.OrderBy;

@Entity
public class Dude {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@OrderBy
	private String name;

	@Column(nullable = false)
	private Holcost holcost;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Holcost getHolcost() {
		return holcost;
	}

	public void setHolcost(Holcost holcost) {
		this.holcost = holcost;
	}

	@Override
	public String toString() {
		return this.name;
	}
}

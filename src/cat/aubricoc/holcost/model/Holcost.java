package cat.aubricoc.holcost.model;

import cat.aubricoc.holcost.db.enums.Column;
import cat.aubricoc.holcost.db.enums.Entity;
import cat.aubricoc.holcost.db.enums.GeneratedValue;
import cat.aubricoc.holcost.db.enums.Id;
import cat.aubricoc.holcost.db.enums.OrderBy;

@Entity
public class Holcost {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	@OrderBy
	private String name;

	@Column(nullable = false)
	private Boolean active;

	public Holcost() {
		super();
	}

	public Holcost(Long id) {
		this.id = id;
	}

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

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	@Override
	public String toString() {
		return this.name;
	}
}

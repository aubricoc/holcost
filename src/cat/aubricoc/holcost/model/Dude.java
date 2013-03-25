package cat.aubricoc.holcost.model;

public class Dude {

	private Long id;
	
	private String name;
	
	private Long holcostId;

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

	public Long getHolcostId() {
		return holcostId;
	}

	public void setHolcostId(Long holcostId) {
		this.holcostId = holcostId;
	}

	@Override
	public String toString() {
		return this.name;
	}
}

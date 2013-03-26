package cat.aubricoc.holcost.model;

public class Holcost {

	private Long id;

	private String name;

	private Boolean active;

	private Long serverId;

	private Boolean pendingChanges;

	private Boolean removed;

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

	public Long getServerId() {
		return serverId;
	}

	public void setServerId(Long serverId) {
		this.serverId = serverId;
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

	@Override
	public String toString() {
		return this.name;
	}
}

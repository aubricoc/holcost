package cat.aubricoc.holcost.model;

public class Dude {

	private Long id;

	private String name;

	private Long holcostId;

	private String email;

	private Boolean isUser;

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

	public Long getHolcostId() {
		return holcostId;
	}

	public void setHolcostId(Long holcostId) {
		this.holcostId = holcostId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getIsUser() {
		return isUser;
	}

	public void setIsUser(Boolean isUser) {
		this.isUser = isUser;
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

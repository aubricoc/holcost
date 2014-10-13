package cat.aubricoc.holcost.model;

import java.util.Date;
import java.util.List;

import com.canteratech.apa.annotation.Column;
import com.canteratech.apa.annotation.Entity;
import com.canteratech.apa.annotation.GeneratedValue;
import com.canteratech.apa.annotation.Id;
import com.canteratech.apa.annotation.OrderBy;
import com.canteratech.apa.annotation.Transient;

@Entity
public class Cost {

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private Double amount;

	@Column(nullable = false)
	@OrderBy
	private Date date;

	@Column(nullable = false)
	private Dude payer;

	@Column(nullable = false)
	private Holcost holcost;

	@Transient
	private List<Dude> participants;

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

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Dude getPayer() {
		return payer;
	}

	public void setPayer(Dude payer) {
		this.payer = payer;
	}

	public Holcost getHolcost() {
		return holcost;
	}

	public void setHolcost(Holcost holcost) {
		this.holcost = holcost;
	}

	public List<Dude> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Dude> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return this.name + " - " + this.amount;
	}
}

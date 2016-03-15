package br.com.mdd.domain.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.joda.time.LocalDate;

@Entity
@Table(name = "income")
public class Income extends Entry {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "income_id")
	private Integer id;
	
	@ManyToOne
	private Category category;
	
	private Boolean received;
	
	@ManyToOne
	private User user;

	public Income(String name, BigDecimal value, LocalDate dueDate) {
		this.setName(name);
		this.setValue(value);
		this.setDueDate(dueDate);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public Boolean getReceived() {
		return received;
	}

	public void setReceived(Boolean received) {
		this.received = received;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

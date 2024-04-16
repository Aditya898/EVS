package com.evs.admin.model;

import java.util.List;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author ymarni
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EVSAdminElection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	@Pattern(regexp = "[A-Z]{2}[0-9]{3}",message = "Election code must match the pattern")
	@Column(nullable = false)
	private String electionCode;
	@Column(nullable = false)
	private String electionName;
	@Column(nullable = false)
	private String state;
	@Column(nullable = false)
	private String constituency;
	@OneToMany(cascade = CascadeType.ALL)
	private List<EVSAdminParty> evsAdminParties;
}

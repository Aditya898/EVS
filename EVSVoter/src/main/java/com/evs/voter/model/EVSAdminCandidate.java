package com.evs.voter.model;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * @author ymarni
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
public class EVSAdminCandidate {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String candidateId;
	private String name;
	private String address;
	private int age;
	private String gender;
	private String constituency;
	private long contactNo;
	@ManyToOne(cascade = CascadeType.ALL)
	private EVSAdminParty evsAdminParty;
}

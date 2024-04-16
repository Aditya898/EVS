package com.evs.voter.model;

import java.time.LocalDate;
import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "Voter")
public class EVSVoter {

	@Id
	private String id;
	private String voterId;
	private String userName;
	private String firstName;
	private String lastName;
	private String gender;
	private String email;
	private LocalDate dateOfBirth;
	private int age;
	private String contactNo;
	private String address;
	private String nationality;
	private String evsVoterStatus;
	private byte[] aadharCard;	

}

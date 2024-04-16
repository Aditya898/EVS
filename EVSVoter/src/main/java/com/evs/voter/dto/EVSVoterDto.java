package com.evs.voter.dto;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVSVoterDto {

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

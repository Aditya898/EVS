package com.evs.voter.dto;


import com.evs.voter.model.EVSAdminParty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EVSAdminCandidateDto {
	private long id;
	private String candidateId;
	private String name;
	private String address;
	private int age;
	private String gender;
	private String constituency;
	private long contactNo;
	private EVSAdminParty evsAdminParty;
}

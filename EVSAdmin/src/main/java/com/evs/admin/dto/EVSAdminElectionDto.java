package com.evs.admin.dto;

import java.util.List;

import com.evs.admin.model.EVSAdminParty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author ymarni
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVSAdminElectionDto {
	private long id;
	private String electionCode;
	private String electionName;
	private String state;
	private String constituency;
	private List<EVSAdminParty> evsAdminParties;
}

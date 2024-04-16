package com.evs.voter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVSVoterCastVoteDto {

	private long id;
	private String voterId;
	private String electionCode;
	private String partyCode;
	private long noOfVotes;

}

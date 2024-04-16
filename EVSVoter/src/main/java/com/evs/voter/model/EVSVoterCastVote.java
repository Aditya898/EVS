package com.evs.voter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVSVoterCastVote {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String voterId;
	private String electionCode;
	private String partyCode;
	private long noOfVotes;

}

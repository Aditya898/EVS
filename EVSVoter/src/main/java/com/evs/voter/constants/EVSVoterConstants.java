package com.evs.voter.constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

public class EVSVoterConstants {

	public static final String STATUS_WHILE_REGISTERING_PENDING = "PENDING";
	public static final String STATUS_AFTER_REGISTERING_APPROVED = "APPROVED";
	public static final String STATUS_AFTER_REGISTERING_DECLINED = "DECLINED";
}

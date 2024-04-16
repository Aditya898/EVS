package com.evs.voter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EVSAdminPartyDto {
    private long id;
    private String partyCode;
    private String partyName;
    private String leader;
    private String symbol;
    private boolean nationalParty;
    private boolean stateParty;
    
    
}

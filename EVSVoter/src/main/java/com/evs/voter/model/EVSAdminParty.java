package com.evs.voter.model;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * @author aditaitkar
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EVSAdminParty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String partyCode;
    private String partyName;
    private String leader;
    private String symbol;
    private boolean nationalParty;
    private boolean stateParty;
    
}

package com.kalandyk.server.neo4j.entity;

import com.kalandyk.api.model.ConfirmationType;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

/**
 * Created by kamil on 1/13/14.
 */
@NodeEntity
public class ConfirmationEntity extends AbstractEntity {

    public static final String CONFIRMATION_APPLICANT_RELATION = "triggers";
    public static final String CONFIRMATION_RELATED_DEBT_RELATION = "concern";

    @RelatedTo(type = CONFIRMATION_RELATED_DEBT_RELATION, elementClass = DebtEntity.class, direction = Direction.OUTGOING)
    private DebtEntity connectedDebt;

    @RelatedTo(type = CONFIRMATION_APPLICANT_RELATION, elementClass = UserEntity.class, direction = Direction.OUTGOING)
    private UserEntity requestApplicant;

    private ConfirmationType confirmationType;

    public DebtEntity getConnectedDebt() {
        return connectedDebt;
    }

    public void setConnectedDebt(DebtEntity connectedDebt) {
        this.connectedDebt = connectedDebt;
    }

    public UserEntity getRequestApplicant() {
        return requestApplicant;
    }

    public void setRequestApplicant(UserEntity requestApplicant) {
        this.requestApplicant = requestApplicant;
    }

    public ConfirmationType getConfirmationType() {
        return confirmationType;
    }

    public void setConfirmationType(ConfirmationType confirmationType) {
        this.confirmationType = confirmationType;
    }
}

package com.kalandyk.server.service;

import com.kalandyk.api.model.Debt;
import com.kalandyk.api.model.DebtState;
import com.kalandyk.api.model.User;
import com.kalandyk.exception.DebtControlException;
import com.kalandyk.server.neo4j.entity.DebtEntity;
import org.dozer.Mapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/DebtControlTest-context.xml"})
@Transactional
//@Ignore//Mock AuthUtil
public class DebtServiceIntegrationTest extends DebtTestPreparation {

    private final static String DEBT_DESC = "DebtSampleDescription";
    @Autowired
    @InjectMocks
    private DebtService debtService;

    @Autowired
    private UserService userService;
    @Autowired
    private Mapper mapper;
    @Mock
    private AuthenticationService authenticationService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        prepareTwoUsers(userService);
        when(authenticationService.getAuthenticatedUser()).thenReturn(getUserA());
    }

    @Test
    public void shouldCreateDebt() throws Exception {
        Debt debt = generateDebt();

        DebtEntity debtEntity = debtService.createDebt(getUserA(), mapToEntity(debt));
        assertNotNull(debtEntity);
        assertEquals(debtEntity.getDescription(), DEBT_DESC);
        assertEquals(debtEntity.getCreditor(), getUserB());
        assertEquals(debtEntity.getDebtor(), getUserA());
        assertNotNull(debtEntity.getHistory().getEvents());
        assertEquals(debtEntity.getHistory().getEvents().size(), 1);
        assertEquals(debtEntity.getDebtState(), DebtState.NOT_CONFIRMED_DEBT);
    }

    @Test
    public void shouldReturnProperDebtStates() throws DebtControlException {
        DebtEntity debt = getDebtSampleEntity();
        debt = debtService.makeDecisionRegardingAddingDebtRequest(debt, false);
        assertEquals(debt.getDebtState(), DebtState.DELETED);
        debt = getDebtSampleEntity();
        assertEquals(debt.getDebtState(), DebtState.NOT_CONFIRMED_DEBT);
        debt = debtService.makeDecisionRegardingAddingDebtRequest(debt, true);
        assertEquals(debt.getDebtState(), DebtState.CONFIRMED_NOT_REPAID_DEBT);
        try {
            debtService.makeDecisionRegardingAddingDebtRequest(debt, false);
            fail("Should not go here");
        } catch (Exception e) {
        }
        debt = debtService.requestDebtRepaying(debt);
        assertEquals(debt.getDebtState(), DebtState.CONFIRMED_DEBT_WITH_PENDING_REPAYMENT_APPROVAL);
        debt = debtService.cancelDebtRepayingRequest(debt);
        assertEquals(debt.getDebtState(), DebtState.CONFIRMED_NOT_REPAID_DEBT);
        debt = debtService.requestDebtRepaying(debt);
        debt = debtService.makeDecisionRegardingRepayDebtRequest(debt, false);
        assertEquals(debt.getDebtState(), DebtState.CONFIRMED_NOT_REPAID_DEBT);
        debt = debtService.requestDebtRepaying(debt);
        debt = debtService.makeDecisionRegardingRepayDebtRequest(debt, true);
        assertEquals(debt.getDebtState(), DebtState.CONFIRMED_REPAID_DEBT);
        assertEquals(debt.getHistory().getEvents().size(), 8);
    }

    private DebtEntity mapToEntity(Debt debt) {
        return mapper.map(debt, DebtEntity.class);
    }

    private DebtEntity getDebtSampleEntity() throws DebtControlException {
        return debtService.createDebt(getUserA(), mapToEntity(generateDebt()));
    }

    private Debt generateDebt() {
        Debt debt = new Debt();
        debt.setDescription(DEBT_DESC);
        debt.setDebtor(mapper.map(getUserA(), User.class));
        debt.setCreditor(mapper.map(getUserB(), User.class));
        return debt;
    }
}

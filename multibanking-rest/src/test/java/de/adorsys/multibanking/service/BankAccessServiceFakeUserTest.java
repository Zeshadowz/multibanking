package de.adorsys.multibanking.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import de.adorsys.multibanking.exception.ResourceNotFoundException;
import de.adorsys.multibanking.service.config.BaseServiceTest;
import de.adorsys.multibanking.service.old.TestConstants;
import de.adorsys.onlinebanking.mock.MockBanking;
import figo.FigoBanking;

@RunWith(SpringRunner.class)
public class BankAccessServiceFakeUserTest extends BaseServiceTest {

    @MockBean
    private FigoBanking figoBanking;
    @MockBean
    private MockBanking mockBanking;
    @MockBean
    private OnlineBankingServiceProducer bankingServiceProducer;
    @Autowired
    private UserService userService;
    @Autowired
    private BankAccessService bankAccessService;
    @Autowired
    private DeleteExpiredUsersScheduled userScheduler;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
    	TestConstants.setup();
    }

    @Before
    public void beforeTest() throws IOException {
    	MockitoAnnotations.initMocks(this);
        when(bankingServiceProducer.getBankingService(anyString())).thenReturn(mockBanking);
    }
    
    @After
    public void after(){
    	if(userContext!=null)
    		System.out.println(userContext.getRequestCounter());
    }

    @Test
    public void when_delete_bankAccesd_user_notExist_should_throw_exception() {
    	// Inject a user, without creating that user in the storage.
    	auth("fakeUser", "fakePassword");
    	
        thrown.expect(ResourceNotFoundException.class);
        // "badLogin", 
        boolean deleteBankAccess = bankAccessService.deleteBankAccess("badAccess");
        assertThat(deleteBankAccess).isEqualTo(false);
    }

    @Test
    @Ignore
    public void cleaup_users_job() {
    	randomAuthAndUser(new Date());
//        importBanks();
        userScheduler.deleteJob();
        thrown.expect(ResourceNotFoundException.class);
        userService.readUser();
    }
}
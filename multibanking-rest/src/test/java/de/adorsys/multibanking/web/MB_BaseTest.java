package de.adorsys.multibanking.web;

import de.adorsys.multibanking.domain.BankEntity;
import de.adorsys.multibanking.service.BankService;
import de.adorsys.multibanking.web.base.BankLoginTuple;
import de.adorsys.multibanking.web.base.BaseControllerIT;
import de.adorsys.multibanking.web.base.UserPasswordTuple;
import org.adorsys.cryptoutils.storeconnectionfactory.ExtendedStoreConnectionFactory;
import org.adorsys.encobject.service.api.ExtendedStoreConnection;
import org.junit.Assume;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.Optional;

/**
 * Created by peter on 07.05.18 at 17:34.
 */
public class MB_BaseTest extends BaseControllerIT {
    @Autowired
    public BankService bankService;
    public UserPasswordTuple userPasswordTuple;
    public BankLoginTuple theBeckerTuple = new BankLoginTuple("19999999", "m.becker", "12345");

    @Before
    public void setupBank() throws Exception {
        ExtendedStoreConnection c = ExtendedStoreConnectionFactory.get();
        c.listAllBuckets().forEach(bucket -> {
            if (! bucket.getObjectHandle().getContainer().equals("bp-system")) {
                c.deleteContainer(bucket);
            }
        });
        userPasswordTuple = new UserPasswordTuple("peter", "allwaysTheSamePassword");
        auth(userPasswordTuple);

        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("mock_bank.json");
        bankService.importBanks(inputStream);
        Optional<BankEntity> bankEntity = bankService.findByBankCode("19999999");
        Assume.assumeTrue(bankEntity.isPresent());
    }

}

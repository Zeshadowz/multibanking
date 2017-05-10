package de.adorsys.multibanking.domain;

import de.adorsys.multibanking.encrypt.Encrypted;
import domain.Booking;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by alexg on 07.02.17.
 */
@Data
@Document
@CompoundIndexes({
        @CompoundIndex(name = "booking_unique_index", def = "{'externalId': 1, 'accountId': 1}", unique = true)
})
@Encrypted(fields = {"amount", "usage", "balance", "otherAccount.blzHbciAccount",
        "otherAccount.numberHbciAccount", "otherAccount.nameHbciAccount"})
public class BookingEntity extends Booking {

    @Id
    private String id;
    @Indexed
    private String accountId;
    private BookingCategoryEntity category;

    public BookingEntity id(String id) {
        this.id = id;
        return this;
    }

    public BookingEntity accountId(String accountId) {
        this.accountId = accountId;
        return this;
    }
}

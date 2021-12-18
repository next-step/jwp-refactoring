package kitchenpos.table.domain;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    @Column
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        Assert.isTrue(isGreaterThanZero(numberOfGuests), "손님 수는 0명 이상이여야 합니다.");

        this.numberOfGuests = numberOfGuests;
    }

    private boolean isGreaterThanZero(int numberOfGuests) {
        return numberOfGuests >= 0;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

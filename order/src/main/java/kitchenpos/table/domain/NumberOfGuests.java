package kitchenpos.table.domain;

import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {
    private static final int MIN_VALUE = 0;

    @Column
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        Assert.isTrue(isGreaterThanMinValue(numberOfGuests), "손님 수는 0명 이상이여야 합니다.");

        this.numberOfGuests = numberOfGuests;
    }

    private boolean isGreaterThanMinValue(int numberOfGuests) {
        return numberOfGuests >= MIN_VALUE;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

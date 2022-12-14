package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.common.exception.InvalidParameterException;

@Embeddable
public class NumberOfGuests {
    @Column(name = "number_of_guests", nullable = false)
    private int count;

    protected NumberOfGuests() {}

    private NumberOfGuests(int count) {
        validate(count);
        this.count = count;
    }

    private void validate(int count) {
        if (count < 0) {
            throw new InvalidParameterException("최소 인원 수는 0명 이상입니다.");
        }
    }

    public static NumberOfGuests from(int count) {
        return new NumberOfGuests(count);
    }

    public int value() {
        return count;
    }
}

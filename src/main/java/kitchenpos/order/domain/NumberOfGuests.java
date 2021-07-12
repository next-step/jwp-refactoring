package kitchenpos.order.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        verifyAvailable(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void verifyAvailable(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 음수가 될 수 없습니다.");
        }
    }

    public int value() {
        return numberOfGuests;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) o;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

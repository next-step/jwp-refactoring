package kitchenpos.table.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class NumberOfGuests {

    private int numberOfGuests;

    public NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        checkIsNegative(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public int numberOfGuests() {
        return numberOfGuests;
    }

    private void checkIsNegative(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손닙 수는 음수를 입력할 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        NumberOfGuests that = (NumberOfGuests) object;
        return numberOfGuests == that.numberOfGuests;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfGuests);
    }
}

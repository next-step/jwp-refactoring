package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOdGuests {
    @Column(name = "number_of_guests")
    private int numberOfGuests;

    protected NumberOdGuests() {
    }

    public NumberOdGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public int toInt() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuestsIsLessThanZero(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validateNumberOfGuestsIsLessThanZero(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("수정 가능한 인원이 0보다 작을 수 없습니다.");
        }
    }
}

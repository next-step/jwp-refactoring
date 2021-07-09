package kitchenpos.domain;

import javax.persistence.Embeddable;

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
}

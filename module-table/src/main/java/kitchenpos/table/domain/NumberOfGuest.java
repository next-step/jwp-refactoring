package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuest {
    private static final int EMPTY_GUEST = 0;

    @Column(nullable = false)
    private int numberOfGuests;

    protected NumberOfGuest() {
    }

    public NumberOfGuest(Integer numberOfGuests) {
        validate(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validate(Integer numberOfGuests) {
        if(numberOfGuests == null || numberOfGuests < EMPTY_GUEST){
            throw new IllegalArgumentException("[ERROR] 방문 손님 수가 0명 미만일 수 없습니다.");
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}

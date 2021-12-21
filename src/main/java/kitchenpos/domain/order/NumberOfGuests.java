package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private static final int MIN = 0;

    @Column(name = "numberOfGuests")
    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    private NumberOfGuests(int numberOfGuests) {
        valid(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public static NumberOfGuests of(int numberOfGuests) {
        return new NumberOfGuests(numberOfGuests);
    }

    private void valid(int numberOfGuests) {
        if (numberOfGuests < MIN) {
            throw new IllegalArgumentException(String.format("%s명 이하의 손님을 설정 할 수 없습니다.", MIN));
        }
    }

    public int value() {
        return numberOfGuests;
    }

    public NumberOfGuests changeNumberOfGuests(int changeNumberOfGuests) {
        return of(changeNumberOfGuests);
    }

}

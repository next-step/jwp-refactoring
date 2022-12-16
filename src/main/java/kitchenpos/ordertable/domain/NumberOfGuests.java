package kitchenpos.ordertable.domain;

import javax.persistence.Embeddable;

@Embeddable
public class NumberOfGuests {

    private int numberOfGuests;

    protected NumberOfGuests() {
    }

    public NumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님의 수는 최소 0명 이상만 설정 가능합니다[numberOfGuests:" + numberOfGuests + "]");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    @Override
    public String toString() {
        return "NumberOfGuests{" +
                "numberOfGuests=" + numberOfGuests +
                '}';
    }
}

package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class GuestNumber {
    private static final int MIN_NUMBER = 0;

    private int guestNumber;

    protected GuestNumber() {}

    public GuestNumber(int guestNumber) {
        checkGreaterThanZero(guestNumber);
        this.guestNumber = guestNumber;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void checkGreaterThanZero(int guestNumber) {
        if (guestNumber < MIN_NUMBER) {
            throw new IllegalArgumentException("고객이 0명 미만 입니다");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestNumber)) return false;
        GuestNumber that = (GuestNumber) o;
        return guestNumber == that.guestNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(guestNumber);
    }
}

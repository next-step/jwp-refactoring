package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import org.springframework.util.Assert;

@Embeddable
public class Headcount {

    @Column(name = "number_of_guests", nullable = false)
    private int value;

    protected Headcount() {
    }

    private Headcount(int value) {
        Assert.isTrue(isZeroOrPositive(value), String.format("인원 수(%d)는 반드시 0이상 이어야 합니다.", value));
        this.value = value;
    }

    public static Headcount from(int value) {
        return new Headcount(value);
    }

    public int value() {
        return value;
    }

    private boolean isZeroOrPositive(long value) {
        return value >= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Headcount headcount = (Headcount) o;
        return value == headcount.value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}

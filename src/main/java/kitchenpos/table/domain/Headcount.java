package kitchenpos.table.domain;

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

    private boolean isZeroOrPositive(long value) {
        return value >= 0;
    }
}

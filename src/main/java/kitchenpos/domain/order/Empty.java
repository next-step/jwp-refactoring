package kitchenpos.domain.order;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Empty {

    @Column(name = "empty")
    private boolean empty;

    protected Empty() {
    }

    private Empty(boolean empty) {
        this.empty = empty;
    }

    public static Empty of(boolean empty) {
        return new Empty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void validNotEmpty() {
        if (!isEmpty()) {
            throw new IllegalArgumentException("빈 테이블이 아닙니다.");
        }
    }
}

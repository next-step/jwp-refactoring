package kitchenpos.domain.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableEmpty {
    @Column(nullable = false)
    private boolean empty;

    public OrderTableEmpty() {
    }

    private OrderTableEmpty(boolean empty) {
        this.empty = empty;
    }

    public static OrderTableEmpty of(boolean empty) {
        return new OrderTableEmpty(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

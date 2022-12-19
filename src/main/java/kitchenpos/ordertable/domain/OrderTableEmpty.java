package kitchenpos.ordertable.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class OrderTableEmpty {

    @Column(nullable = false)
    private boolean empty;

    protected OrderTableEmpty() {
    }

    public OrderTableEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public String toString() {
        return "OrderTableEmpty{" +
                "empty=" + empty +
                '}';
    }
}

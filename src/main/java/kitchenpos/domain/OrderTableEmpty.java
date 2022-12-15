package kitchenpos.domain;

import javax.persistence.Embeddable;

@Embeddable
public class OrderTableEmpty {

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

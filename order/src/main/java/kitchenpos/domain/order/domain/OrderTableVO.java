package kitchenpos.domain.order.domain;

public class OrderTableVO {

    private boolean empty;

    public OrderTableVO(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

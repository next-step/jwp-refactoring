package kitchenpos.dto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-16
 */
public class OrderTableEmptyChangeRequest {

    private boolean empty;

    public boolean isEmpty() {
        return empty;
    }

    protected OrderTableEmptyChangeRequest() {
    }

    public OrderTableEmptyChangeRequest(boolean empty) {
        this.empty = empty;
    }
}

package kitchenpos.table.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : ChangeEmptyRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ChangeEmptyRequest {
    private boolean empty;

    private ChangeEmptyRequest() {
    }

    private ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public static ChangeEmptyRequest of(boolean empty) {
        return new ChangeEmptyRequest(empty);
    }

    public boolean isEmpty() {
        return empty;
    }
}

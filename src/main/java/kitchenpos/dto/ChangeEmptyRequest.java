package kitchenpos.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : ChangeEmptyRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ChangeEmptyRequest {
    private boolean empty;

    public ChangeEmptyRequest() {
    }

    public ChangeEmptyRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

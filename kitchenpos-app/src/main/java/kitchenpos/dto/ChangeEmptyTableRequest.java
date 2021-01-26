package kitchenpos.dto;

import javax.validation.constraints.NotNull;

public class ChangeEmptyTableRequest {
    @NotNull
    private boolean empty;

    protected ChangeEmptyTableRequest() {}

    public ChangeEmptyTableRequest(boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}

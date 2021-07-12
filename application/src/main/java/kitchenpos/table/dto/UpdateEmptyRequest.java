package kitchenpos.table.dto;

public class UpdateEmptyRequest {
    private Boolean empty;

    public UpdateEmptyRequest() { }

    public UpdateEmptyRequest(Boolean empty) {
        this.empty = empty;
    }

    public Boolean isEmpty() {
        return empty;
    }
}

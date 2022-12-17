package kitchenpos.dto;

public class ChangeEmptyRequest {
    private boolean isEmpty;

    public ChangeEmptyRequest(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public boolean isEmpty() {
        return isEmpty;
    }
}

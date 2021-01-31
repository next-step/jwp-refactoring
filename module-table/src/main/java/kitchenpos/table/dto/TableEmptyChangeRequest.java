package kitchenpos.table.dto;

public class TableEmptyChangeRequest {

    private Long id;
    private boolean empty;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}

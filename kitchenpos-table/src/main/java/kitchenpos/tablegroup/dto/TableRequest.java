package kitchenpos.tablegroup.dto;

public class TableRequest {

    private final Long id;

    protected TableRequest() {
        this.id = null;
    }

    public TableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

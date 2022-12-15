package kitchenpos.dto;

public class TableRequest {
    private Long id;

    public TableRequest() {
    }

    public TableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

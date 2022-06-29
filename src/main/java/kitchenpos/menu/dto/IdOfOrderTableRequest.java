package kitchenpos.menu.dto;

public class IdOfOrderTableRequest {
    private Long id;

    public IdOfOrderTableRequest() {
    }

    public IdOfOrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

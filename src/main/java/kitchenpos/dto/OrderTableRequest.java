package kitchenpos.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderTableRequest {
    private Long id;

    public OrderTableRequest() {
    }

    public OrderTableRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}

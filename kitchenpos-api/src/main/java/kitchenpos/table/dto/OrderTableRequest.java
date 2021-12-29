package kitchenpos.table.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderTableRequest {
    private Long id;

    private OrderTableRequest() {
    }

    private OrderTableRequest(Long id) {
        this.id = id;
    }

    public static OrderTableRequest of(Long id) {
        return new OrderTableRequest(id);
    }

    public Long getId() {
        return id;
    }
}

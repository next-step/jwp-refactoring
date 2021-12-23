package kitchenpos.table.dto;

/**
 * packageName : kitchenpos.dto
 * fileName : OrderTableRequest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
//FIXME 생성자 제한하기
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

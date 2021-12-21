package kitchenpos.exception;

/**
 * packageName : kitchenpos.exception
 * fileName : OrderTableNotFoundException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderTableNotFoundException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "주문테이블이 존재하지 않습니다.";

    public OrderTableNotFoundException() {
        super(message);
    }
}

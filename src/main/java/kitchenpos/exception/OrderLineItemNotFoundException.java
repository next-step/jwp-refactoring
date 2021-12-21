package kitchenpos.exception;

/**
 * packageName : kitchenpos.exception
 * fileName : OrderLineItemNotFoundException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class OrderLineItemNotFoundException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "주문정보가 존재하지않습니다.";

    public OrderLineItemNotFoundException() {
        super(message);
    }

}

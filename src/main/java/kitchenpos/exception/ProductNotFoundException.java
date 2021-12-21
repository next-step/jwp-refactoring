package kitchenpos.exception;

/**
 * packageName : kitchenpos.exception
 * fileName : ProductNotFoundException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ProductNotFoundException extends RuntimeException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(message);
    }
}

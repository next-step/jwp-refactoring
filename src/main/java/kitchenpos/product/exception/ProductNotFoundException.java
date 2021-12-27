package kitchenpos.product.exception;

import kitchenpos.common.exception.ServiceException;

/**
 * packageName : kitchenpos.exception
 * fileName : ProductNotFoundException
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
public class ProductNotFoundException extends ServiceException {
    private static final Long serialVersionUID = 1L;
    public static final String message = "상품이 존재하지 않습니다.";

    public ProductNotFoundException() {
        super(message);
    }
}

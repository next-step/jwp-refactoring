package kitchenpos.application.creator;

import java.math.BigDecimal;
import kitchenpos.dto.ProductCreateRequest;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class ProductHelper {

    public static ProductCreateRequest createRequest(String name, int price) {
        return new ProductCreateRequest(name, BigDecimal.valueOf(price));
    }

}

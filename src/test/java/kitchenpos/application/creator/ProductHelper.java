package kitchenpos.application.creator;

import java.math.BigDecimal;
import kitchenpos.dto.ProductDto;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class ProductHelper {

    public static ProductDto create(String name, int price) {
        ProductDto product = new ProductDto();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}

package kitchenpos.application.creator;

import java.math.BigDecimal;
import kitchenpos.domain.Product;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-11
 */
public class ProductHelper {

    public static Product create(String name, int price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));
        return product;
    }
}

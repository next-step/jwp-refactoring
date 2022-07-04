package kitchenpos.product.domain;

import kitchenpos.common.Price;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ProductValidator {
    public void validatePrice(Product product) {
        if (Objects.isNull(product.getPrice()) || product.getPrice().compareTo(new Price()) < 0) {
            throw new IllegalArgumentException();
        }
    }
}

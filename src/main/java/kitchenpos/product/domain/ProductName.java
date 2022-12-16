package kitchenpos.product.domain;

import javax.persistence.Embeddable;
import org.springframework.util.StringUtils;

@Embeddable
public class ProductName {

    private String name;

    protected ProductName() {
    }

    public ProductName(String name) {
        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

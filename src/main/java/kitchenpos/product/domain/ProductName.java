package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ProductName {
    @Column(nullable = false)
    private String name;

    protected ProductName() {

    }

    public ProductName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    protected ProductResponse() {
    }

    private ProductResponse(Long id, Name name, Price price) {
        this.id = id;
        this.name = name.value();
        this.price = price.value();
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.id(), product.name(), product.price());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

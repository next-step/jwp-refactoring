package kitchenpos.dto.response;

import kitchenpos.domain.product.Product;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductViewResponse {
    private Long id;
    private String name;
    private BigDecimal price;

    public static ProductViewResponse of(Product product) {
        return new ProductViewResponse(product.getId(), product.getName().toString(), product.getPrice().toBigDecimal());
    }

    public ProductViewResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductViewResponse that = (ProductViewResponse) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}

package kitchenpos.product.dto;

import java.math.BigDecimal;
import java.util.Objects;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price.price();
    }

    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice());
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
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
        return Objects.equals(id, that.id) && Objects.equals(name, that.name)
            && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}

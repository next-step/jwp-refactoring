package kitchenpos.menu.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class MenuProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(
            final Long id,
            final String name,
            final BigDecimal price,
            final Long quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(final Long productId, String productName, BigDecimal productPrice, Long menuProductQuantity) {
        return new MenuProductResponse(
                productId,
                productName,
                productPrice,
                menuProductQuantity
        );
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

    public Long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProductResponse that = (MenuProductResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(price, that.price) &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, quantity);
    }

    @Override
    public String toString() {
        return "MenuProductResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}

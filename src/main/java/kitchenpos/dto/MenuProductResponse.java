package kitchenpos.dto;

import kitchenpos.domain.MenuProductEntity;

import java.util.Objects;

public class MenuProductResponse {
    private Long id;
    private String name;
    private Long price;
    private Integer quantity;

    protected MenuProductResponse() {
    }

    public MenuProductResponse(Long id, String name, Long price, Integer quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public static MenuProductResponse of(MenuProductEntity menuProduct) {
        return new MenuProductResponse(
                menuProduct.getId(),
                menuProduct.getProduct().getName(),
                menuProduct.getProduct().getUnitPrice(),
                menuProduct.getQuantity()
        );
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
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

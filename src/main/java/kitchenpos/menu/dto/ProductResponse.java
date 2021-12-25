package kitchenpos.menu.dto;

import java.util.Objects;

import kitchenpos.menu.domain.Product;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    
    private ProductResponse() {
    }

    private ProductResponse(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    
    public static ProductResponse of(Long id, String name, int price) {
        return new ProductResponse(id, name, price);
    }
    
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice().intValue());
    }
    
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse that = (ProductResponse) o;
        return Objects.equals(name, that.name) && Objects.equals(price, that.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}

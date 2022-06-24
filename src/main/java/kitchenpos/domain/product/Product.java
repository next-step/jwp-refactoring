package kitchenpos.domain.product;

import kitchenpos.dto.product.ProductResponse;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private ProductName name;

    @Embedded
    private ProductPrice price;

    public Product() {

    }

    public Product(String name, BigDecimal price) {
        this(null, name, price);
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = new ProductName(name);
        this.price = new ProductPrice(price);
    }

    public static Product of(ProductResponse productResponse) {
        return new Product(productResponse.getId(), productResponse.getName(), productResponse.getPrice());
    }

    public void validate() {
        price.minimumCheck();
    }

    public BigDecimal calculateAmount(long quantity) {
        return price.calculateAmount(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }
}

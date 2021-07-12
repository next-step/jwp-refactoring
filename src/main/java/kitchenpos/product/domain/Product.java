package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductName productName;
    @Embedded
    private ProductPrice productPrice;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.productName = new ProductName(name);
        this.productPrice = new ProductPrice(price);
    }

    public Long getId() {
        return id;
    }

    public ProductName getProductName() {
        return this.productName;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}

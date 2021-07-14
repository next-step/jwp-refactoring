package kitchenpos.product.domain;

import java.math.BigDecimal;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.domain.Name;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name productName;
    @Embedded
    private ProductPrice productPrice;

    public Product() {
    }

    public Product(String name, BigDecimal price) {
        this.productName = new Name(name);
        this.productPrice = new ProductPrice(price);
    }

    public Long getId() {
        return id;
    }

    public Name getProductName() {
        return this.productName;
    }

    public ProductPrice getProductPrice() {
        return productPrice;
    }
}

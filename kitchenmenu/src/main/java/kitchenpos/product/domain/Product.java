package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menuconstants.MenuErrorMessages;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
    @Embedded
    private ProductPrice productPrice;

    public Product() {}

    public Product(String name, ProductPrice productPrice) {
        this.name = name;
        this.productPrice = productPrice;
    }

    public Product(Long id, String name, ProductPrice productPrice) {
        this.id = id;
        this.name = name;
        this.productPrice = productPrice;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return productPrice;
    }

    public BigDecimal getPriceValue() {
        return productPrice.getPrice();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name)
                && Objects.equals(productPrice, product.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, productPrice);
    }

    public void validateName() {
        if (name.isEmpty()) {
            throw new IllegalArgumentException(MenuErrorMessages.PRODUCT_NAME_CANNOT_BE_EMPTY);
        }
    }
}

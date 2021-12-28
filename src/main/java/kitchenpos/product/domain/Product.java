package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private ProductPrice productPrice;

    protected Product() {

    }

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.productPrice = new ProductPrice(price);
    }

    public static Product create(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPriceProduct() {
        return productPrice;
    }

    public BigDecimal getPrice() {
        return productPrice.getPrice();
    }

    public long multiplyQuantity(Long quantity) {
        return this.productPrice.multiply(new BigDecimal(quantity)).longValue();
    }

    public boolean matchId(Long productId) {
        return this.id.equals(productId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(productPrice, product.productPrice);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, productPrice);
    }
}

package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(Product product) {
        this(product.id, product.name, product.price);
    }

    private Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
        validateNonNullFields();
    }

    private void validateNonNullFields() {
        if (name == null || price == null) {
            throw new IllegalArgumentException("이름, 가격은 상품의 필수 사항입니다.");
        }
    }

    public static Product of(String name, long price) {
        return new Product(name, Price.from(price));
    }

    public Price calculateTotal(long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }
}

package kitchenpos.domain;

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
    @Embedded
    private ProductName name;
    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Long id, ProductName name, Price price) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(ProductName name, Price price) {
        validatePrice(price);
        this.name = name;
        this.price = price;
    }

    private void validatePrice(Price price) {
        if (price == null) {
            throw new IllegalArgumentException("상품의 가격은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }
}

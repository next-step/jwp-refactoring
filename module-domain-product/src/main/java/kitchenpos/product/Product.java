package kitchenpos.product;

import kitchenpos.common.exception.Message;
import kitchenpos.common.domain.Price;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    Price price;

    protected Product() {
    }

    public Product(String name, Price price) {
        this(0L, name, price);
    }

    public Product(Long id, String name, Price price) {
        validateName(name);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validateName(String name) {
        if (name == null) {
            throw new IllegalArgumentException(Message.ERROR_PRODUCT_NAME_REQUIRED.showText());
        }
    }

    public boolean hasSameIdAs(Long productId) {
        return this.id.equals(productId);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

}

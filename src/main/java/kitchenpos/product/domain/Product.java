package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.ObjectUtils;

@Entity
public class Product {
    private static final String NAME_NOT_ALLOW_NULL_OR_EMPTY = "상품명은 비어있거나 공백일 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    protected Product() {}

    public Product(String name, BigDecimal price) {
        validate(name);
        this.name = name;
        this.price = new Price(price);
    }

    private void validate(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException(NAME_NOT_ALLOW_NULL_OR_EMPTY);
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }
}

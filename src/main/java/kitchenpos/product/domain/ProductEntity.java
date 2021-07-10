package kitchenpos.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

import static java.util.Objects.isNull;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private BigDecimal price;

    protected ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal price) {
        nameValidCheck(name);
        priceValidCheck(price);
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void priceValidCheck(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("가격이 없거나 음수인 상품은 등록할 수 없습니다.");
        }
    }

    private void nameValidCheck(String name) {
        if (isNull(name)) {
            throw new IllegalArgumentException("상품 이름은 필수로 입력되어야 합니다.");
        }
    }

}

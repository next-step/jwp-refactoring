package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.MustHaveName;

@Entity
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MustHaveName name;

    @Embedded
    private Price price;

    public Product() {
    }

    private Product(String name, Integer price) {
        this.name = MustHaveName.valueOf(name);
        this.price = Price.valueOf(price);
    }

    public static Product of(String name, Integer price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }
    public BigDecimal getPrice() {
        return price.get();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = MustHaveName.valueOf(name);
    }

    public void setPrice(Integer price) {
        this.price = Price.valueOf(price);
    }

    public boolean equalName(String name) {
        return this.name.equals(name);
    }

    public boolean equalPrice(BigDecimal price) {
        return this.price.equals(price);
    }
}

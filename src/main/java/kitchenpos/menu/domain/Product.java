package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {}

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = Name.from(name);
        this.price = Price.from(price);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public String getNameValue() {
        return name.value();
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceVale() {
        return price.value();
    }

    public static class Builder {

        private Long id;
        private String name;
        private BigDecimal price;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Product build() {
            return new Product(id, name, price);
        }
    }
}

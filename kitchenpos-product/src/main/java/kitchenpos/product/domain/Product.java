package kitchenpos.product.domain;

import static javax.persistence.GenerationType.*;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class Product {
    @GeneratedValue(strategy = IDENTITY)
    @Id
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    public Product() {
    }

    public Product(Name name, Price price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

}

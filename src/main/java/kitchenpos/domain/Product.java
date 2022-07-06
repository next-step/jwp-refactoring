package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.springframework.util.ObjectUtils;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {

    }

    public Product(String name) {
        this.name = new Name(name);
    }


    public Product(String name, Price price) {
        validPrice(price);
        this.name = new Name(name);
        this.price = price;
    }


    public Product(String name, BigDecimal price) {
        this(name, Price.of(price));
    }

    public Product(Long id, String name, Price price) {
        this(name, price);
        this.id = id;

    }


    public Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }



    private void validPrice(Price price) {
        if (ObjectUtils.isEmpty(price)) {
            throw new IllegalArgumentException("가격은 필수 입니다.");
        }
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.value();
    }


    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceValue() {
        return price.value();
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
        return Objects.equals(getName(), product.getName()) && Objects.equals(getPrice(),
                product.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getPrice());
    }
}


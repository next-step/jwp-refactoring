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
    private String name;

    @Embedded
    private Price price;

    protected Product() {

    }

    public Product(String name) {
        validName(name);
        this.name = name;
    }


    public Product(String name, Price price) {
        validName(name);
        validPrice(price);
        this.name = name;
        this.price = price;
    }


    public Product(String name, BigDecimal price) {
        this(name, Price.of(price));
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }


    public Product(Long id, String name, BigDecimal price) {
        this(name, price);
        this.id = id;
    }


    private void validName(String name) {
        if (ObjectUtils.isEmpty(name)) {
            throw new IllegalArgumentException("상품의 이름은 필수 입니다.");
        }
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
        return name;
    }

    public void setName(final String name) {
        this.name = name;
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


package kitchenpos.domain.menu;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    public static Product of(String name, BigDecimal price) {
        return new Product(null, name, price);
    }
    // for jpa
    public Product() {
    }

    private Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        setPrice(price);
    }

    private void setPrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Invalid price");
        }
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

    public void changePrice(BigDecimal price) {
        setPrice(price);
    }

    public BigDecimal multiply(BigDecimal factor){
        return this.price.multiply(factor);
    }
}

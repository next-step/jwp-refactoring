package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import kitchenpos.menu.domain.MenuProduct;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;
    private BigDecimal price;

    @OneToMany(mappedBy = "product")
    private List<MenuProduct> menuProducts;

    public Product() {
    }

    private Product(String name, BigDecimal price) {
        this.name = Name.of(name);
        this.price = price;
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = Name.of(name);
        this.price = price;
    }

    private Product(Long id) {
        this.id = id;
    }

    public static Product of(String name, Integer price) {
        if (price == null) {
            throw new IllegalArgumentException();
        }
        return new Product(name, BigDecimal.valueOf(price));
    }

    public static Product of(Long id) {
        return new Product(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = Name.of(name);
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }
}

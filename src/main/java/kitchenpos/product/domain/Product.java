package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.menu.domain.Amount;
import kitchenpos.menu.domain.MenuProductQuantity;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    public Product(String name, ProductPrice price) {
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, ProductPrice price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ProductPrice getPrice() {
        return price;
    }

    public Amount createAmount(MenuProductQuantity quantity) {
        return new Amount(price, quantity);
    }
}

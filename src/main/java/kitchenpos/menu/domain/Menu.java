package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.generic.price.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    @Column(nullable = false)
    private Price price = new Price();

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, Price price, MenuProducts menuProducts) {
        this(null, name, price, menuProducts);
    }

    Menu(Long id, String name, Price price, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
    }

    public void setMenuGroupId(MenuValidator validator, Long menuGroupId) {
        this.menuGroupId = menuGroupId;
        validator.validate(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isSatisfiedBy(MenuOption menuOption) {
        if (!this.name.equals(menuOption.getName())) {
            return false;
        }

        return this.price.hasSameValueAs(menuOption.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Menu menu = (Menu)o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

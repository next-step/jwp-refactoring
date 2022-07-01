package kitchenpos.menu.domain;

import kitchenpos.embeddableEntity.Name;
import kitchenpos.embeddableEntity.Price;
import kitchenpos.menu.dto.MenuRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, new Name(name), new Price(price), menuGroupId, new MenuProducts());
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, new Name(name), new Price(price), menuGroupId, menuProducts);
    }

    public Menu(String name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        this(null, new Name(name), price, menuGroupId, menuProducts);
    }

    public Menu(Long id, Name name, Price price, Long menuGroupId, MenuProducts menuProducts) {
        if (price.moreExpensiveThan(menuProducts.totalPrice())) {
            throw new IllegalArgumentException();
        }
        menuProducts.setMenu(this);

        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(MenuRequest menuRequest, MenuProducts menuProducts) {
        return new Menu(menuRequest.getName(), menuRequest.getPrice(), menuRequest.getMenuGroupId(), menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId()) && Objects.equals(getName(), menu.getName()) && Objects.equals(getPrice(), menu.getPrice()) && Objects.equals(getMenuGroupId(), menu.getMenuGroupId()) && Objects.equals(getMenuProducts(), menu.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}

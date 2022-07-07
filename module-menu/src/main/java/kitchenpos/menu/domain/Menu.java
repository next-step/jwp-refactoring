package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.dto.MenuRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Price price, MenuProducts menuProducts) {
        this(null, new Name(name), price, null, menuProducts);
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
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
    }

    public static Menu of(MenuRequest request) {
        return new Menu(
                request.getName(),
                request.getPrice(),
                request.getMenuGroupId(),
                getMenuProducts(request));
    }

    private static MenuProducts getMenuProducts(MenuRequest request) {
        List<MenuProduct> menuProducts = request.getMenuProducts()
                .stream()
                .map(p -> new MenuProduct(
                        p.getProductId(),
                        p.getQuantity()))
                .collect(Collectors.toList());

        return new MenuProducts(menuProducts);
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

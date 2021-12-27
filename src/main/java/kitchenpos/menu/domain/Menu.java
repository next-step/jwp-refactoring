package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.BadRequestException;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup,
        List<MenuProduct> menuProducts) {
        validate(menuGroup, menuProducts, price);
        this.id = id;
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private void validate(MenuGroup menuGroup, List<MenuProduct> menuProducts, BigDecimal price) {
        validateMenuGroup(menuGroup);
        validateMenuProducts(menuProducts);
        validateMenuPrice(new Price(price), menuProducts);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private void validateMenuPrice(Price price, List<MenuProduct> menuProducts) {
        BigDecimal sumPrice = sumProductsPrice(menuProducts);
        if (price.isGreaterThanSumPrice(sumPrice)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private BigDecimal sumProductsPrice(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProduct::multiplyQuantityToPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Menu menu = (Menu)o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
            && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup)
            && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}

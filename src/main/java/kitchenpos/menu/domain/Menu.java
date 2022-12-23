package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menugroup.domain.MenuGroup;


@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @Column(nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.name = new Name(name);
        validatePrice(price);
        this.price = new Price(price);
        validateMenu(menuGroupId);
        validatePriceSum(menuProducts.totalMenuPrice());
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
        menuProducts.setMenu(this);
    }

    private void validatePrice(BigDecimal price) {
        if (price == null) {
            throw new IllegalArgumentException(ErrorEnum.PRICE_IS_NOT_NULL.message());
        }
    }

    public static Menu of(long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, MenuProducts.of(menuProducts));
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroupId, MenuProducts.of(menuProducts));
    }

   public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroup.getId(), menuProducts);
    }

    private void validateMenu(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new IllegalArgumentException(ErrorEnum.REQUIRED_MENU.message());
        }
    }

    private void validatePriceSum(Price totalPrice) {
        if (price.isBiggerThan(totalPrice)) {
            throw new IllegalArgumentException(ErrorEnum.MENU_PRICE_OVER_TOTAL_PRICE.message());
        }
    }

    public void create(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.addMenuProduct(this, menuProduct);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public void setName(final Name name) {
        this.name = name;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(final Price price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }
//    public void setMenuProducts(MenuProducts menuProducts) {
//        validatePrice(menuProducts.totalMenuPrice());
//        menuProducts.setMenu(this);
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return id.equals(menu.id) && name.equals(menu.name) && price.equals(menu.price) && menuGroupId.equals(
                menu.menuGroupId) && menuProducts.equals(menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId, menuProducts);
    }
}

package kitchenpos.menu.domain;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.menu.exceptions.InputMenuDataErrorCode;
import kitchenpos.menu.exceptions.InputMenuDataException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @Column(name = "name", nullable = false)
    private Name name;

    @Embedded
    @Column(name = "price", nullable = false)
    private Price price;

    @Column(name = "menuGroupId")
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {

    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId);
        checkValidMenuProducts(menuProducts);
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, menuGroupId, menuProducts);
        this.id = id;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        checkValidMenuGroup(menuGroupId);
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }


    public void validSum(BigDecimal sumPrice) {
        BigDecimal amount = this.getPrice().getPrice();
        if (amount.compareTo(sumPrice) > 0) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_SUM_OF_MENU_PRICE_IS_LESS_THAN_SUM_OF_PRODUCTS);
        }
    }

    private void checkValidMenuGroup(Long menuGroupId) {
        if (menuGroupId == null) {
            throw new InputMenuDataException(InputMenuDataErrorCode.YOU_MUST_INPUT_MENU_GROUP_ID);
        }

        if (menuGroupId < 0) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_MENU_GROUP_ID_IS_LESS_THAN_ZERO);
        }
    }

    private void checkValidMenuProducts(List<MenuProduct> menuProducts) {
        if (CollectionUtils.isEmpty(menuProducts)) {
            throw new InputMenuDataException(InputMenuDataErrorCode.THE_MENU_MUST_HAVE_PRODUCT);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Menu)) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

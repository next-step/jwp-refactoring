package kitchenpos.menu.domain;

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
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

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
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_to_menu_group"))
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {}

    private Menu(Long id, String name, BigDecimal bigDecimalPrice, MenuGroup menuGroup,
                 MenuProducts menuProducts) {
        Price price = Price.from(bigDecimalPrice);
        validateMenuGroup(menuGroup);
        validateMenuProducts(price, menuProducts);
        menuProducts.setUpMenu(this);
        this.id = id;
        this.name = Name.from(name);
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup,
                          List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, MenuProducts.from(menuProducts));
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup,
                          MenuProducts menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if(menuGroup == null) {
            throw new IllegalArgumentException(ErrorCode.메뉴_그룹은_비어있을_수_없음.getErrorMessage());
        }
    }

    private void validateMenuProducts(Price price, MenuProducts menuProducts) {
        if(price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException(ErrorCode.메뉴의_가격은_메뉴상품들의_가격의_합보다_클_수_없음.getErrorMessage());
        }
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
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}

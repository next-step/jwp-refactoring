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
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

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
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        validateMenuProducts(Price.from(price), menuProducts);
        validateMenuGroup(menuGroup);
        menuProducts.addMenu(this);
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu from(String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
    }

    private static void validateMenuGroup(MenuGroup menuGroup) {
        if (Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException("메뉴그룹이 있어야 합니다.");
        }
    }

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String nameValue() {
        return name.value();
    }

    public BigDecimal priceValue() {
        return price.value();
    }

    public long menuGroupId() {
        return menuGroup.id();
    }

    public List<MenuProduct> readOnlyMenuProducts() {
        return menuProducts.readOnlyMenuProducts();
    }

    private void validateMenuProducts(Price price, MenuProducts menuProducts) {
        if (price.compareTo(menuProducts.totalPrice()) > 0) {
            throw new IllegalArgumentException("메뉴 가격은 상품의 총 금액을 넘길 수 없습니다.");
        }
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
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.MustHaveName;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.common.domain.Price;

@Entity
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MustHaveName name;

    @Embedded
    private Price price;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    private Menu(String name, Integer price, MenuGroup menuGroup) {
        this.name = MustHaveName.valueOf(name);
        this.price = Price.fromInteger(price);
        validateMenuGroup(menuGroup);
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, Integer price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public void removeMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.remove(menuProduct);
    }

    protected void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);

        if (!menuProduct.equalMenu(this)) {
            menuProduct.setMenu(this);
        }
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct: menuProducts) {
            addMenuProduct(menuProduct);
        }
        validatePrice();
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    private void validatePrice() {
        if (price.isGreaterThan(this.menuProducts.getTotalPrice())) {
            throw new InvalidArgumentException("메뉴의 총 가격은 구성하는 상품의 총가격보다 작거나 같아야 합니다.");
        }
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new InvalidArgumentException("메뉴그룹은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(final String name) {
//        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public void setPrice(final BigDecimal price) {
//        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuGroupId(final Long menuGroupId) {
//        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
//        this.menuProducts = menuProducts;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu other = (Menu) o;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

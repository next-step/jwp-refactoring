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
import kitchenpos.common.domain.Price;
import kitchenpos.exception.InvalidArgumentException;

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
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
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

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        for (MenuProduct menuProduct : menuProducts) {
            this.menuProducts.add(menuProduct);
        }
        this.menuProducts.validatePrice(price);
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    private void validateMenuGroup(MenuGroup menuGroup) {
        if (menuGroup == null) {
            throw new InvalidArgumentException("메뉴그룹은 필수입니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
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

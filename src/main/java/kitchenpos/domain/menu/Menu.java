package kitchenpos.domain.menu;

import kitchenpos.exception.BadRequestException;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.utils.Message.INVALID_MENU_PRICE;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = MenuName.from(name);
        this.price = MenuPrice.from(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.from(menuProducts);

        checkPrice(this.menuProducts.totalAmount());
        this.menuProducts.setup(this);
    }

    private void checkPrice(BigDecimal totalAmount) {
        if (this.price.isLessOrEqualTotalAmount(totalAmount)) {
            throw new BadRequestException(INVALID_MENU_PRICE);
        }
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }
}

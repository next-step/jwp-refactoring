package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private Price price;
    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    public Menu(String name, Integer price) {
        this.name = name;
        this.price = Price.from(price);
    }

    public Menu(String name, Integer price, MenuGroup menuGroup) {
        this(name, price);
        this.menuGroup = menuGroup;
    }

    public Menu(String name, Integer price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(name, price, new MenuGroup(menuGroupId));

        this.menuProducts = MenuProducts.of(this, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public BigDecimal getPriceBigDecimal() {
        return price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }
}

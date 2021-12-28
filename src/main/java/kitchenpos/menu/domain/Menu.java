package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;
    @Embedded
    private MenuPrice price;
    @Embedded
    private MenuProductGroup menuProducts;

    @Column(nullable = false)
    private Long menuGroupId;

    protected Menu() {
    }

    private Menu(MenuName name, MenuPrice price, MenuProductGroup menuProducts, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuProducts = menuProducts;
        this.menuGroupId = menuGroupId;
    }

    private Menu(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        this(MenuName.of(name), MenuPrice.of(price), MenuProductGroup.of(menuProducts), menuGroupId);
    }

    private Menu(String name, int price, Long menuGroupId, MenuProductGroup menuProductGroup) {
        this(MenuName.of(name), MenuPrice.of(price), menuProductGroup, menuGroupId);
    }

    private Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(MenuName.of(name), MenuPrice.of(price), MenuProductGroup.of(menuProducts), menuGroupId);
        this.id = id;
    }

    public static Menu generate(long id, String name, List<MenuProduct> menuProducts, Long menuGroupId, BigDecimal price) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, int price, long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu create(int price, String name, MenuProductGroup menuProducts, long menuGroupId) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public boolean matchPrice(int targetPrice) {
        return this.price.matchPrice(targetPrice);
    }

    public boolean matchName(String targetName) {
        return this.name.matchName(targetName);
    }
}

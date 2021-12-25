package kitchenpos.menu.domain;

import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    private Name name;

    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    public Menu() {
    }

    private Menu(Long id) {
        this.id = id;
    }

    private Menu(String name, Integer price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = MenuGroup.of(menuGroupId);
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public Menu(Long id, String name, Integer price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = MenuGroup.of(menuGroupId);
        this.menuProducts = MenuProducts.of(menuProducts);
    }

    public static Menu of(Long id) {
        return new Menu(id);
    }

    public static Menu of(String name, Integer price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
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

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public MenuProducts getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final MenuProducts menuProducts) {
        this.menuProducts = menuProducts;
    }
}

package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import kitchenpos.core.domain.Name;
import kitchenpos.core.domain.Price;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id")
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = new MenuProducts(menuProducts, this.price, this);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getItems();
    }
}

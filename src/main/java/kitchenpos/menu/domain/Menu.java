package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = new MenuName(name);
        this.price = new MenuPrice(price);
        this.menuGroupId = menuGroupId;
        addMenuProducts(menuProducts);
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.changeMenu(this));
        this.menuProducts = new MenuProducts(menuProducts);
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
}

package kitchenpos.domain.menu;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(null, name, price, menuGroup, menuProducts);
    }
    
    public static Menu of(Long id, String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Price getPrice() {
        return this.price;
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }
    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts;
    }

    public void changeMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void chnageMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public void changePrice(Price price) {
        this.price = price;
    }

    public void changeMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

}

package kitchenpos.domain;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

    private Menu(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
    }

    public static Menu  of(String name, Price price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroup, menuProducts);
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

}

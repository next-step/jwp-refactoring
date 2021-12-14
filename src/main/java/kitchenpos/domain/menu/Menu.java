package kitchenpos.domain.menu;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    private Menu(Long id, String name, Price price, MenuGroup menuGroup) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = new ArrayList<>();
    }

    public static Menu of(String name, Price price, MenuGroup menuGroup) {
        return new Menu(null, name, price, menuGroup);
    }

    public static Menu of(String name, Price price) {
        return new Menu(null, name, price, null);
    }

    public static Menu of(Long id, String name, Price price) {
        return new Menu(id, name, price, null);
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

    public boolean isEqualMenuId(Long menuId) {
        return this.id.equals(menuId);
    }
}

package kitchenpos.menu.domain;

import kitchenpos.menu.util.MenuValidator;
import kitchenpos.wrap.Price;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(Long id){
        this.id = id;
    }

    public Menu(Long id, String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public Menu(String name, Price price, MenuGroup menuGroup, MenuProducts menuProducts) {
        this(null, name, price, menuGroup, menuProducts);
    }

    public void validateToMenu(MenuValidator menuValidator) {
        menuValidator.validateToMenu(this);
        menuProducts.registerProduct(this);
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

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getMenuProducts();
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(price, menu.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}

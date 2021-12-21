package kitchenpos.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@EntityListeners(value = MenuPricePolicyListener.class)
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @ManyToOne
    private MenuGroup menuGroup;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Long id, String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup, menuProducts);
        this.id = id;
    }

    public Menu(String name, long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this(name, price, menuGroup);
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Menu(String name, long price, MenuGroup menuGroup) {
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
    }

    public void addProduct(Product product, long quantity) {
        menuProducts.add(this, product, quantity);
    }

    public long getTotalProductPrice() {
        return menuProducts.getTotalPrice();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.value();
    }

    public long getPrice() {
        return price.longValue();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return id.equals(menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

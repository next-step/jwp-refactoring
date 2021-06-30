package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.product.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @ManyToOne
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "seq", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts;

    public Menu() {
        menuProducts = new ArrayList<>();
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this(name, price, menuGroup, new ArrayList<>());
    }

    public Menu(String name, Long price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = new Price(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
        menuProduct.addedBy(this);
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

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}

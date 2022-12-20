package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;
    private MenuPrice price = new MenuPrice();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_menu_group"))
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {}

    public Menu(String name, Integer price, MenuGroup menuGroup) {
        validate(name, menuGroup);
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    private void validate(String name, MenuGroup menuGroup) {
        if(Objects.isNull(name) || name.isEmpty()) {
            throw new IllegalArgumentException(ErrorMessage.MENU_REQUIRED_NAME.getMessage());
        }
        if(Objects.isNull(menuGroup)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_REQUIRED_MENU_GROUP.getMessage());
        }
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.addMenuProduct(this, menuProduct);
    }

    public void create(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
        this.menuProducts.validatePrice(this.price.value());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public Set<MenuProduct> getMenuProducts() {
        return menuProducts.values();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(name, menu.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

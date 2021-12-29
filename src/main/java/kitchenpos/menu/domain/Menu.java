package kitchenpos.menu.domain;

import java.util.List;
import java.util.Objects;

import javax.persistence.Embedded;
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
    
    @Embedded
    private MenuPrice price;
    
    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;
    
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();
    
    protected Menu() {
    }
    
    private Menu(String name, long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroup = menuGroup;
    }

    public static Menu of(String name, long price, MenuGroup menuGroup) {
        return new Menu(name, price, menuGroup);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MenuPrice getPrice() {
        return price;
    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }
    
    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void addMenuProducts(final List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> {
            menuProduct.setMenuId(this.id);
            this.menuProducts.add(menuProduct);
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

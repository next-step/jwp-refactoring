package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();


    public Menu() {
    }

    public Menu(String name, BigDecimal price, MenuGroup menuGroup) {
        this.name = name;
        this.price = new MenuPrice(price);
        this.menuGroup = menuGroup;
    }

    public void validateProductsPrice() {
        price.compareTo(menuProducts.validateProductsPrice());
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.menuProducts = new MenuProducts(menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price.setPrice(price);
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroup.setId(menuGroupId);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.setMenuProducts(menuProducts);
    }


}

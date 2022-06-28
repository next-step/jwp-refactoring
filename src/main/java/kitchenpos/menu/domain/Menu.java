package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.*;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;

    @OneToMany(mappedBy = "menu", cascade = ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId, new ArrayList<>());
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this(null, name, price, menuGroupId, menuProducts);
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public void addMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
        menuProduct.setMenu(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void addMenuProducts(List<MenuProductRequest> menuProductRequests) {
        menuProductRequests.forEach(menuProductRequest ->
                    this.addMenuProduct(menuProductRequest.toMenuProduct(this)));

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(getId(), menu.getId()) && Objects.equals(getName(), menu.getName()) && Objects.equals(getPrice(), menu.getPrice()) && Objects.equals(getMenuGroupId(), menu.getMenuGroupId()) && Objects.equals(getMenuProducts(), menu.getMenuProducts());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice(), getMenuGroupId(), getMenuProducts());
    }
}

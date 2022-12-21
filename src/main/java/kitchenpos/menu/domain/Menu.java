package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = setMenu(menuProducts, this);
    }

    private List<MenuProduct> setMenu(List<MenuProduct> menuProducts, Menu menu){
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(menu));
        return menuProducts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return this.price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}

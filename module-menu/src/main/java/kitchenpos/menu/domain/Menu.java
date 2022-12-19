package kitchenpos.menu.domain;

import kitchenpos.core.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    @Column(nullable = false)
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu(Long id, String name, BigDecimal price, Long menuGroupId) {
        this.id = id;
        this.name =  requireNonNull(name, "name");
        this.price = new Price(price);
        this.menuGroupId = requireNonNull(menuGroupId, "menuGroupId");
    }

    public Menu(String name, BigDecimal price, Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    protected Menu() {
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(this, menuProducts);
    }

    public OrderedMenu toOrderedMenu() {
        return new OrderedMenu(id, name, price);
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

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.get();
    }
}

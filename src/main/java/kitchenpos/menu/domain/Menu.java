package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @JoinColumn(name = "menu_group_id")
    private Long menuGroupId;

    @Embedded
    private final MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = Price.of(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts.add(menuProducts);
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
        return menuProducts.value();
    }
}

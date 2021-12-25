package kitchenpos.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id", nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public Menu() {

    }

    private Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name.getName();
    }

    public void setName(final String name) {
        this.name = Name.of(name);
    }

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public void setPrice(final BigDecimal price) {
        this.price = Price.of(price);
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroup.setId(menuGroupId);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

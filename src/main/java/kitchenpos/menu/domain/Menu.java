package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_group_id_of_menu"))
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    private Menu(Long id) {
        this.id = id;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroup = MenuGroup.of(menuGroupId);
        this.menuProducts = menuProducts;
    }

    public Menu(Long id, String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = MenuGroup.of(menuGroupId);
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id) {
        return new Menu(id);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId,
        List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
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
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroup = MenuGroup.of(menuGroupId);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

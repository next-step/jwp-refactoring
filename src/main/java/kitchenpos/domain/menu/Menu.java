package kitchenpos.domain.menu;

import com.google.common.collect.Lists;
import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = Lists.newArrayList();

    protected Menu() {}

    private Menu(String name, BigDecimal price, Long menuGroupId) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    private Menu(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId) {
        return new Menu(name, price, menuGroupId);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price) {
        return new Menu(name, price);
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
        return this.menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void appendMenuProduct(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }
}

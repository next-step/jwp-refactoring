package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @Embedded
    private MenuName name;

    @Embedded
    private MenuPrice price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id_v2", foreignKey = @ForeignKey(name = "fk_menu_menu_group_v2"), nullable = false)
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    @Deprecated
    private Long menuGroupId;

    protected Menu() {
    }

    private Menu(Long id, MenuName name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = MenuPrice.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId) {
        return new Menu(id, MenuName.from(name), price, menuGroupId, null);
    }

    public static Menu of(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, MenuName.from(name), price, menuGroupId, menuProducts);
    }

    public static Menu of(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(null, MenuName.from(name), price, menuGroupId, menuProducts);
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

    public BigDecimal getPrice() {
        return price.getPrice();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

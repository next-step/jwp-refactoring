package kitchenpos.menu.domain;

import io.micrometer.core.instrument.util.StringUtils;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @Embedded
    private MenuProducts menuProducts;

    protected Menu(){

    }
    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        validateMenuName(name);

        this.id = id;
        this.name = name;
        this.price = Price.of(price);
        this.menuGroup = menuGroup;
        this.menuProducts = MenuProducts.of(this, menuProducts);
    }

    public static Menu of(long id, String name, BigDecimal price) {
        return new Menu(id, name, price, null, null);
    }

    public static Menu of(long id, String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(id, name, price, menuGroup, null);
    }

    public static Menu of(long id, String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroup, menuProducts);
    }

    public static Menu of(String name, BigDecimal price, MenuGroup menuGroup) {
        return new Menu(null, name, price,menuGroup, new ArrayList<>());
    }

    private static void validateMenuName(String name){
        if(StringUtils.isBlank(name)){
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroup.getId();
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts.setMenuProducts(menuProducts);
    }

    public void checkPriceValid() {
        if (!this.price.isLessOrEqualTo(this.menuProducts.getSum())) {
            throw new IllegalArgumentException();
        }
    }

    public BigDecimal getPrice() {
        return this.price.getValue();
    }

    public MenuGroup getMenuGroup() {
        return this.menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return this.menuProducts.getMenuProducts();
    }
}

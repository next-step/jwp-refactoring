package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;
//    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    public Menu() {
    }

    public Menu(Long id, String name, BigDecimal price, MenuGroup menuGroup, MenuProducts menuProducts) {
//    public Menu(Long id, String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
//        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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

//    public void setMenuGroupId(final Long menuGroupId) {
//        this.menuGroupId = menuGroupId;
//    }

    public MenuGroup getMenuGroup() {
        return menuGroup;
    }

    public void setMenuGroup(MenuGroup menuGroup) {
        this.menuGroup = menuGroup;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
    }

    public void setMenuProducts(MenuProducts menuProducts){
        this.menuProducts = menuProducts;
    }
}

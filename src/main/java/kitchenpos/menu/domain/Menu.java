package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    private MenuGroup menuGroup;

    protected Menu() {}

    public Menu(Long id, String name, BigDecimal price,
                Long menuGroupId, List<MenuProduct> menuProducts) { // FIXME
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Menu of(Long id, String name, BigDecimal price,
                          Long menuGroupId, List<MenuProduct> menuProducts) {
        return new Menu(id, name, price, menuGroupId, menuProducts);
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
        return null;
    }

    public List<MenuProduct> getMenuProducts() {
        return null;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {

    }

}

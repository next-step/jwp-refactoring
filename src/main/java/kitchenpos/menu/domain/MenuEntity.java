package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_group_id")
    private MenuGroupEntity menuGroup;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected MenuEntity() {
    }

    private MenuEntity(String name, BigDecimal price, MenuGroupEntity menuGroup) {
        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroup = menuGroup;
    }

    public static MenuEntity of(String name, BigDecimal price, MenuGroupEntity menuGroup) {
        return new MenuEntity(name, price, menuGroup);
    }

    public void registerMenuProducts(List<MenuProductEntity> menuProducts) {
        this.menuProducts = new MenuProducts(menuProducts, price);
        this.menuProducts.mapInto(this);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getValue();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        if (menuGroup != null) {
            return menuGroup.getId();
        }

        return null;
    }

    public List<MenuProductEntity> getMenuProducts() {
        return menuProducts.getItems();
    }
}

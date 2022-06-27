package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @JoinColumn(name = "menu_group_id", foreignKey = @ForeignKey(name = "fk_menu_menu_group"), nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts;

    protected Menu() {
    }

    private Menu(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        menuProducts.addMenu(this);
        this.name = Name.from(name);
        this.price = Price.from(price);
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    public static Menu from(String name, BigDecimal price, Long menuGroupId, MenuProducts menuProducts) {
        return new Menu(name, price, menuGroupId, menuProducts);
    }

    public Long id() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name name() {
        return name;
    }

    public Price price() {
        return price;
    }

    public Long menuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> readOnlyMenuProducts() {
        return menuProducts.readOnlyMenuProducts();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

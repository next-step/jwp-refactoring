package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    private Long menuGroupId;

    @OneToMany(mappedBy = "menu_product", orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public Menu() {
    }

    public Menu(final Long id, final String name, final Long price, final MenuGroup menuGroup,
        final List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(BigDecimal.valueOf(price));
        this.menuGroupId = menuGroup.getId();
        this.menuProducts = menuProducts;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public void setMenuGroupId(final Long menuGroupId) {
        this.menuGroupId = menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void setMenuProducts(final List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }
}

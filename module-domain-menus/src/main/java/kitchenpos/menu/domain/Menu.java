package kitchenpos.menu.domain;

import kitchenpos.Price;
import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    @Column(name = "menu_group_id")
    private Long menuGroupId;

    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(final String name, final BigDecimal price, final Long menuGroupId) {
        this(null, name, price, menuGroupId);
    }

    public Menu(final Long id, final String name, final BigDecimal price, final Long menuGroupId) {
        this.id = id;
        this.name = name;
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<Long> getProductIds() {
        return menuProducts.list().stream()
                .map(menuProduct -> menuProduct.getProductId())
                .collect(Collectors.toList());
    }

    protected void appendMenuProducts(final MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public void validationByPrice(Map<Long, Price> productPriceMap) {
        this.menuProducts.validationByPrice(price, productPriceMap);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Menu that = (Menu) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

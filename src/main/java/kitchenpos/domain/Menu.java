package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.infrastructure.jpa.MoneyConverter;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-13
 */
@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Convert(converter = MoneyConverter.class)
    private Money price;

    private Long menuGroupId;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProduct = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, Money price, Long menuGroupId, List<MenuProduct> menuProduct) {
        if (Objects.isNull(price) || price.isLessThen(Money.ZERO)) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProduct = menuProduct;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProduct() {
        return menuProduct;
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

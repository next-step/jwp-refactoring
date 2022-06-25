package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Menu {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @Column
    private Long menuGroupId;

    @OneToMany
    private List<MenuProduct> menuProducts;

    protected Menu() {
    }

    public Menu(Long id, String name, Long price, Long menuGroupId,
                List<MenuProduct> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
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
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name)
                && Objects.equals(price, menu.price) && Objects.equals(menuGroupId, menu.menuGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroupId);
    }
}

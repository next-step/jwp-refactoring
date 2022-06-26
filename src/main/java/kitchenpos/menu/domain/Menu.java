package kitchenpos.menu.domain;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;

@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;
    @Column(name = "menu_group_id", nullable = false)
    private Long menuGroupId;
    @Embedded
    private MenuProducts menuProducts = new MenuProducts();

    protected Menu() {
    }

    public Menu(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        validateMenu(menuGroupId);

        this.name = new Name(name);
        this.price = new Price(price);
        this.menuGroupId = menuGroupId;

        addMenuProducts(menuProducts);
    }

    private void validateMenu(Long menuGroupId) {
        requireNonNull(menuGroupId, "메뉴 그룹이 존재하지 않습니다.");
        if (menuGroupId <= 0) {
            throw new IllegalArgumentException("메뉴 그룹 설정이 잘못 되었습니다.");
        }
    }

    private void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(menuProduct -> menuProduct.setMenu(this));
        this.menuProducts.addMenuProducts(menuProducts);

        validateMenuPrice();
    }

    private void validateMenuPrice() {
        BigDecimal sum = menuProducts.totalPrice();

        if (price.isGreaterThan(sum)) {
            throw new IllegalArgumentException("메뉴의 금액은 상품의 총합보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts.getMenuProducts();
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

    @Override
    public String toString() {
        return "Menu{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", menuGroupId=" + menuGroupId +
            ", menuProducts=" + menuProducts +
            '}';
    }

}

package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.exception.InvalidPriceException;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;

@Entity
@Table(name = "menu")
public class Menu {
    private static final Long MIN_PRICE = 0L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Long price;

    @ManyToOne
    @JoinColumn(name = "menu_group_id")
    private MenuGroup menuGroup;

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected Menu() {
    }

    public Menu(String name, Long price, MenuGroup menuGroup) {
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
    }

    public Menu(Long id, String name, Long price, MenuGroup menuGroup,
                List<MenuProduct> menuProducts) {
        validatePrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroup = menuGroup;
        this.menuProducts = menuProducts;
    }

    private void validatePrice(Long price) {
        if (price == null || price < MIN_PRICE) {
            throw new InvalidPriceException();
        }
    }

    public void addProduct(Product product, Long quantity) {
        final MenuProduct menuProduct = new MenuProduct(this, product, quantity);
        this.menuProducts.add(menuProduct);
    }

    public void validateProductsTotalPrice() {
        final long total = this.menuProducts.stream()
                .map(MenuProduct::price)
                .mapToLong(Long::intValue).sum();
        if (total < this.price) {
            throw new InvalidPriceException("제품의 합은 메뉴 가격보다 클 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public MenuResponse toMenuResponse() {
        return new MenuResponse(this.id, this.name, this.price, this.menuGroup.toMenuGroupResponse(),
                this.menuProducts);
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
                && Objects.equals(price, menu.price) && Objects.equals(menuGroup, menu.menuGroup)
                && Objects.equals(menuProducts, menu.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, menuGroup, menuProducts);
    }
}

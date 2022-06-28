package kitchenpos.domain.menu;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        validateAddMenuProducts(menuProducts);

        menuProducts.forEach(this::addMenuProduct);
    }

    private void validateAddMenuProducts(List<MenuProduct> menuProducts) {
        requireNonNull(menuProducts, "메뉴에 사용할 상품이 존재하지 않습니다.");

        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴에 사용할 상품이 존재하지 않습니다.");
        }
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        requireNonNull(menuProduct, "메뉴에 사용할 상품이 존재하지 않습니다.");

        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}

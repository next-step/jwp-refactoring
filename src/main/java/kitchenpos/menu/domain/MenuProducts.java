package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public static MenuProducts of(MenuProduct... menuProducts) {
        return new MenuProducts(Arrays.asList(menuProducts));
    }

    public static MenuProducts of(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public boolean isEmpty() {
        return menuProducts.isEmpty();
    }

    public <T> List<T> mapList(Function<MenuProduct, T> function) {
        return menuProducts.stream()
            .map(function)
            .collect(Collectors.toList());
    }

    public boolean isSatisfiedBy(List<MenuProductOption> menuProductOptions) {
        if (menuProducts.size() != menuProductOptions.size()) {
            return false;
        }

        return menuProductOptions.stream().allMatch(this::isSatisfiedBy);
    }

    private boolean isSatisfiedBy(MenuProductOption menuProductOption) {
        return menuProducts.stream()
            .anyMatch(menuProduct -> menuProduct.isSatisfiedBy(menuProductOption));
    }
}

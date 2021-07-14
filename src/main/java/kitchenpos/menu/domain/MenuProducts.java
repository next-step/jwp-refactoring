package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import kitchenpos.generic.price.domain.Price;
import kitchenpos.generic.exception.CalculationFailedException;

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
        checkArgument(menuProducts);
        this.menuProducts = menuProducts;
    }

    private void checkArgument(List<MenuProduct> menuProducts) {
        if (menuProducts.isEmpty()) {
            throw new IllegalArgumentException("제품 매핑 정보는 1개 이상 존재해야 합니다.");
        }
    }

    public Price summation() {
        return menuProducts.stream()
            .map(MenuProduct::getTotalPrice)
            .reduce(Price::add)
            .orElseThrow(() -> new CalculationFailedException("단품 가격의 합계를 계산하지 못했습니다."));
    }

    public boolean containsAll(MenuProducts menuProducts) {
        return this.menuProducts.containsAll(menuProducts.menuProducts);
    }

    public void forEach(Consumer<MenuProduct> consumer) {
        menuProducts.forEach(consumer);
    }

    public <T> List<T> mapList(Function<MenuProduct, T> function) {
        return menuProducts.stream()
            .map(function)
            .collect(Collectors.toList());
    }

    public boolean isSatisfiedBy(List<MenuDetailOption> menuDetailOptions) {
        if (menuProducts.size() != menuDetailOptions.size()) {
            return false;
        }

        return menuDetailOptions.stream().allMatch(this::isSatisfiedBy);
    }

    private boolean isSatisfiedBy(MenuDetailOption menuDetailOption) {
        return menuProducts.stream()
            .anyMatch(menuProduct -> menuProduct.isSatisfiedBy(menuDetailOption));
    }
}

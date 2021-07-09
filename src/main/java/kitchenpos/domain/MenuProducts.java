package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.exception.CalculationFailedException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public MenuProducts withMenu(Menu menu) {
        return new MenuProducts(menuProducts.stream()
            .map(menuProduct -> menuProduct.withMenu(menu))
            .collect(Collectors.toList()));
    }

    public void forEach(Consumer<MenuProduct> consumer) {
        menuProducts.forEach(consumer);
    }

    public <T> List<T> mapList(Function<MenuProduct, T> function) {
        return menuProducts.stream()
            .map(function)
            .collect(Collectors.toList());
    }
}

package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.domain.Price;
import org.springframework.util.Assert;

@Embeddable
class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "menu")
    private List<MenuProduct> products;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> products) {
        Assert.notNull(products, "메뉴 상품 리스트는 필수입니다.");
        Assert.noNullElements(products,
            () -> String.format("메뉴 상품 리스트(%s)에 null이 포함될 수 없습니다.", products));
        this.products = products;
    }

    static MenuProducts from(List<MenuProduct> products) {
        return new MenuProducts(products);
    }

    List<MenuProduct> list() {
        return Collections.unmodifiableList(products);
    }

    Price sumPrice() {
        return products.stream()
            .map(MenuProduct::price)
            .reduce(Price::sum)
            .orElse(Price.ZERO);
    }

    boolean isNotEmpty() {
        return !products.isEmpty();
    }

    void changeMenu(Menu menu) {
        Assert.notNull(menu, "변경하려는 메뉴는 필수입니다.");
        products.forEach(menuProduct ->
            menuProduct.changeMenu(menu));
    }

    @Override
    public String toString() {
        return "MenuProducts{" +
            "products=" + products +
            '}';
    }
}

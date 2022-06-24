package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private final List<MenuProduct> menuProducts = new ArrayList<>();

    // JPA 기본 생성자 이므로 사용 금지
    protected MenuProducts() {
    }

    public void addMenuProducts(List<MenuProduct> menuProducts) {
        menuProducts.forEach(this::addMenuProduct);
    }

    public BigDecimal totalPrice() {
        return menuProducts.stream()
            .map(MenuProduct::totalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateIfNull(List<MenuProduct> menuProducts) {
        if (menuProducts == null || menuProducts.isEmpty()) {
            throw new IllegalArgumentException("메뉴에 사용할 상품이 존재하지 않습니다.");
        }
    }

    private void addMenuProduct(MenuProduct menuProduct) {
        validateIfNull(Collections.singletonList(menuProduct));
        menuProducts.add(menuProduct);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}

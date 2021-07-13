package kitchenpos.menu.domain;

import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = calculateSum(menuProducts, price);
        validSum(sum, price);
        this.menuProducts = menuProducts;
    }

    private BigDecimal calculateSum(List<MenuProduct> menuProducts, BigDecimal price) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.multiplyPrice();
            sum = sum.add(menuProduct.multiplyPrice());
        }
        return sum;
    }

    private void validSum(BigDecimal sum, BigDecimal price) {
        if (price.compareTo(sum) > 0) {
            throw new IllegalArgumentException("메뉴가격은 메뉴에 등록된 상품 가격의 합보다 작거나 같아야합니다.");
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}

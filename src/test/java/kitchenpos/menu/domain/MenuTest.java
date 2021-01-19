package kitchenpos.menu.domain;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {
    private MenuGroup 치킨;
    private Menu 후라이드치킨;
    private Menu 양념치킨;
    private List<MenuProduct> 후라이드치킨메뉴상품 = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        치킨 = new MenuGroup("치킨");
        후라이드치킨메뉴상품.add(new MenuProduct(new Product("후라이드상품", new BigDecimal(10000)), 1L));
        후라이드치킨메뉴상품.add(new MenuProduct(new Product("치킨무", new BigDecimal(500)), 2L));
        후라이드치킨 = new Menu("후라이드치킨", new BigDecimal(17000), 치킨, 후라이드치킨메뉴상품);
       양념치킨 = new Menu("양념치킨", new BigDecimal(-19000), 치킨);

    }

    @Test
    @DisplayName("유효성검사 예외처리")
    public void validationCheck() {
        assertThatThrownBy(() -> {
           양념치킨.validationCheck();
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("상품가격 총합 비교 예외처리")
    public void comparePrice() {
        assertThatThrownBy(() -> {
            후라이드치킨.checkPrice();
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

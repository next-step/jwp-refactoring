package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MenuTest {
    private MenuGroup 치킨;
    private List<MenuProduct> 후라이드치킨메뉴상품 = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        치킨 = new MenuGroup("치킨");
        후라이드치킨메뉴상품.add(new MenuProduct(new Product("후라이드상품", new BigDecimal(10000)), 1L));
        후라이드치킨메뉴상품.add(new MenuProduct(new Product("치킨무", new BigDecimal(500)), 2L));
    }

    @Test
    @DisplayName("유효성검사 예외처리")
    public void validationCheck() {
        assertThatThrownBy(() -> {
            new Menu("양념치킨", new BigDecimal(-19000), 치킨);
        }).isInstanceOf(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Menu("양념치킨", new BigDecimal(-19000), 치킨));
        assertThat(exception.getMessage()).isEqualTo("입력된 가격이 올바르지 않습니다.");
    }

    @Test
    @DisplayName("상품가격 총합 비교 예외처리")
    public void comparePrice() {
        assertThatThrownBy(() -> {
            new Menu("후라이드치킨", new BigDecimal(17000), 치킨, new MenuProducts(후라이드치킨메뉴상품));
        }).isInstanceOf(IllegalArgumentException.class);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> new Menu("후라이드치킨", new BigDecimal(17000), 치킨, new MenuProducts(후라이드치킨메뉴상품)));
        assertThat(exception.getMessage()).isEqualTo("상품가격 총합과 메뉴의 가격이 올바르지 않습니다.");
    }
}

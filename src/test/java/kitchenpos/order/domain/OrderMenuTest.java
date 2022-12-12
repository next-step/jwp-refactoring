package kitchenpos.order.domain;

import static kitchenpos.menu.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.order.domain.OrderMenuTestFixture.generateOrderMenu;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 메뉴 관련 도메인 테스트")
public class OrderMenuTest {

    private Product 감자튀김;
    private Product 불고기버거;
    private Product 콜라;
    private MenuGroup 햄버거세트;
    private MenuProduct 감자튀김상품;
    private MenuProduct 불고기버거상품;
    private MenuProduct 콜라상품;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        콜라 = generateProduct(2L, "콜라", BigDecimal.valueOf(1500L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
        감자튀김상품 = generateMenuProduct(1L, null, 감자튀김, 1L);
        콜라상품 = generateMenuProduct(2L, null, 콜라, 1L);
        불고기버거상품 = generateMenuProduct(3L, null, 불고기버거, 1L);
    }

    @DisplayName("주문 메뉴는 메뉴를 통해 생성된다.")
    @Test
    void createOrderMenu() {
        // given
        String name = "불고기버거세트";
        BigDecimal price = BigDecimal.valueOf(8500);
        Menu 불고기버거세트 = generateMenu(1L, name, price, 햄버거세트, Arrays.asList(감자튀김상품, 불고기버거상품, 콜라상품));

        // when
        OrderMenu 불고기버거세트주문메뉴 = generateOrderMenu(불고기버거세트);

        // then
        assertAll(
                () -> assertThat(불고기버거세트주문메뉴.getName()).isEqualTo(불고기버거세트.getName()),
                () -> assertThat(불고기버거세트주문메뉴.getPrice()).isEqualTo(불고기버거세트.getPrice())
        );
    }
}

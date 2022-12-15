package kitchenpos.menu.domain;

import static kitchenpos.menugroup.domain.MenuGroupTestFixture.generateMenuGroup;
import static kitchenpos.menu.domain.MenuProductTestFixture.generateMenuProduct;
import static kitchenpos.menu.domain.MenuTestFixture.generateMenu;
import static kitchenpos.product.domain.ProductTestFixture.generateProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.math.BigDecimal;
import java.util.Collections;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 관련 도메인 테스트")
public class MenuProductTest {

    private Product 감자튀김;
    private Product 불고기버거;
    private MenuGroup 햄버거세트;

    @BeforeEach
    void setUp() {
        감자튀김 = generateProduct(1L, "감자튀김", BigDecimal.valueOf(3000L));
        불고기버거 = generateProduct(3L, "불고기버거", BigDecimal.valueOf(4000L));
        햄버거세트 = generateMenuGroup(1L, "햄버거세트");
    }

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void createMenuProduct() {
        // given
        long quantity = 2L;

        // when
        MenuProduct menuProduct = generateMenuProduct(감자튀김, quantity);

        // then
        assertAll(
                () -> assertThat(menuProduct.getProductId()).isEqualTo(감자튀김.getId()),
                () -> assertThat(menuProduct.getQuantity()).isEqualTo(Quantity.from(quantity))
        );
    }

    @DisplayName("메뉴 상품의 메뉴를 설정한다.")
    @Test
    void setUpMenuInMenuProduct() {
        // given
        MenuProduct 감자튀김상품 = generateMenuProduct(감자튀김, 1L);
        MenuProduct 불고기버거상품 = generateMenuProduct(불고기버거, 1L);
        Menu 불고기버거세트 = generateMenu(1L, "불고기버거세트", BigDecimal.valueOf(4000), 햄버거세트, Collections.singletonList(불고기버거상품));

        assertThat(감자튀김상품.getMenu()).isNull();

        // when
        감자튀김상품.setUpMenu(불고기버거세트);

        // then
        assertThat(감자튀김상품.getMenu()).isEqualTo(불고기버거세트);
    }

    @DisplayName("메뉴 상품의 수량이 음수이면 메뉴 상품을 생성할 수 없다.")
    @Test
    void createMenuProductThrowErrorWhenQuantityIsSmallerThanZero() {
        // given
        long quantity = -1L;

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> generateMenuProduct(감자튀김, quantity))
                .withMessage(ErrorCode.수량은_0보다_작을_수_없음.getErrorMessage());
    }
}

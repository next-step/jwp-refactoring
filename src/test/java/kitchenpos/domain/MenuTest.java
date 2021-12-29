package kitchenpos.domain;

import kitchenpos.common.exceptions.MenuGroupRequiredException;
import kitchenpos.common.exceptions.MenuProductSumPriceException;
import kitchenpos.common.exceptions.NegativePriceException;
import kitchenpos.common.exceptions.NoRequiredNameException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 도메인 테스트")
class MenuTest {

    private MenuGroup 메뉴그룹;
    private Menu 메뉴;
    private Product 상품;
    private MenuProduct 메뉴상품;

    @BeforeEach
    void setUp() {
        메뉴그룹 = MenuGroup.of("메뉴그룹");
        메뉴 = Menu.of("메뉴", BigDecimal.ONE, 메뉴그룹);
        상품 = Product.of("상품", new BigDecimal("12000"));
        메뉴상품 = MenuProduct.of(메뉴, 상품,2L);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(메뉴).isEqualTo(Menu.of("후라이드", BigDecimal.ONE, MenuGroup.of("추천메뉴")));
    }

    @DisplayName("등록 시, 메뉴 그룹이 필요하다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> Menu.of("메뉴", new BigDecimal("1000"), null))
                .isInstanceOf(MenuGroupRequiredException.class);
    }

    @DisplayName("등록 시, 가격 정보가 필요하다")
    @Test
    void validateTest2() {
        assertThatThrownBy(() -> Menu.of("메뉴", null, 메뉴그룹))
                .isInstanceOf(NegativePriceException.class);
    }

    @DisplayName("등록 시, 이름이 필요하다")
    @Test
    void validateTest3() {
        assertThatThrownBy(() -> Menu.of(null, BigDecimal.ONE, 메뉴그룹))
                .isInstanceOf(NoRequiredNameException.class);
    }

    @DisplayName("메뉴 가격이 구성품의 가격의 합보다 높으면 안된다")
    @Test
    void checkOverPriceTest() {
        
        final Menu 메뉴 = Menu.of("메뉴", BigDecimal.valueOf(50_000), 메뉴그룹);
        메뉴.addMenuProducts(Collections.singletonList(메뉴상품));

        assertThatThrownBy(메뉴::checkOverPrice)
                .isInstanceOf(MenuProductSumPriceException.class);

    }
}

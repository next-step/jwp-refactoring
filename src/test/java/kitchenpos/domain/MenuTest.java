package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyMenuGroupException;
import kitchenpos.common.exceptions.GreaterProductSumPriceException;
import kitchenpos.common.exceptions.EmptyNameException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
        메뉴그룹 = MenuGroup.from("메뉴그룹");
        메뉴 = Menu.of("메뉴", 1, 메뉴그룹);
        상품 = Product.of("상품", 12000);
        메뉴상품 = MenuProduct.of(메뉴, 상품,2L);
    }

    @DisplayName("생성 테스트")
    @Test
    void createTest() {
        assertThat(메뉴).isEqualTo(Menu.of("메뉴", 1, MenuGroup.from("추천메뉴")));
    }

    @DisplayName("등록 시, 메뉴 그룹이 필요하다")
    @Test
    void validateTest() {
        assertThatThrownBy(() -> Menu.of("메뉴", 1000, null))
                .isInstanceOf(EmptyMenuGroupException.class);
    }

    @DisplayName("등록 시, 이름이 필요하다")
    @Test
    void validateTest3() {
        assertThatThrownBy(() -> Menu.of(null, 1, 메뉴그룹))
                .isInstanceOf(EmptyNameException.class);
    }

    @DisplayName("메뉴 가격이 구성품의 가격의 합보다 높으면 안된다")
    @Test
    void checkOverPriceTest() {
        
        final Menu 메뉴 = Menu.of("메뉴", 50000, 메뉴그룹);

        assertThatThrownBy(() -> 메뉴.addMenuProducts(Collections.singletonList(메뉴상품)))
                .isInstanceOf(GreaterProductSumPriceException.class);
    }
}

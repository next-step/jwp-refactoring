package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static org.assertj.core.api.Assertions.assertThat;
import static kitchenpos.product.domain.ProductTestFixture.상품;

@DisplayName("메뉴 상품 목록 일급 컬렉션 테스트")
public class MenuProductBagTest {

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final MenuProductBag 메뉴_쌍품_목록 = MenuProductBag.from(
                Arrays.asList(
                        메뉴_상품(상품("상품").getId(), 1),
                        메뉴_상품(상품("상품2").getId(), 1)));
        //when, then:
        assertThat(메뉴_쌍품_목록.getMenuProductList()).hasSize(2);
    }
}

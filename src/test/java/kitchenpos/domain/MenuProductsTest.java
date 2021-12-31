package kitchenpos.domain;

import kitchenpos.fixture.TestMenuProductsFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 상품들 관련 테스트")
class MenuProductsTest {

    @DisplayName("주어진 가격이 총 구성품 가격보다 높은지 확인할 수 있다")
    @Test
    void isOverPriceTest() {
        final Product 상품 = Product.of("상품", 12000);
        final MenuGroup 메뉴그룹 = MenuGroup.from("메뉴그룹");
        final Menu 메뉴 = Menu.of("메뉴", 30000, 메뉴그룹);
        final MenuProducts 메뉴상품들 = TestMenuProductsFactory.메뉴상품들_생성(메뉴, 상품, 2);

        final boolean result = 메뉴상품들.isOverPrice(메뉴.getPrice());

        assertThat(result).isTrue();
    }
}
package kitchenpos.domain;

import kitchenpos.common.exceptions.EmptyOrderTableException;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestMenuGroupFactory;
import kitchenpos.fixture.TestOrderFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 도메인 테스트")
class OrderTest {
    @DisplayName("생성 시, 주문 테이블 입력되어야 합니다")
    @Test
    void validateTest1() {
        assertThatThrownBy(() -> TestOrderFactory.주문_생성_주문테이블_없음())
                .isInstanceOf(EmptyOrderTableException.class);
    }

    @DisplayName("주문 상품들을 추가할 수 있다")
    @Test
    void addOrderLineItemsTest() {
        final Order 주문 = TestOrderFactory.주문_생성_Cooking_단계(0, true);
        final MenuGroup 메뉴그룹 = TestMenuGroupFactory.메뉴그룹_생성("메뉴그룹");
        final Menu 메뉴 = TestMenuFactory.메뉴_생성(메뉴그룹, "메뉴", 10000);
        final OrderLineItem 주문_상품 = TestOrderFactory.주문_상품_생성(메뉴, 1);
        TestOrderFactory.주문_주문아이템_추가(주문, Collections.singletonList(주문_상품));

        assertThat(TestOrderFactory.주문_상품_목록_개수_확인(주문)).isEqualTo(1);
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void updateStatusTest() {
        final Order 주문 = TestOrderFactory.주문_생성_Cooking_단계(5, false);
        TestOrderFactory.주문_상태_cooking_meal_변경(주문);

        assertThat(TestOrderFactory.주문_상태_확인(주문)).isEqualTo(OrderStatus.MEAL);
    }
}
package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.domain.OrderTableTest.테이블_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderTest {

    @DisplayName("주문 생성에 성공한다.")
    @Test
    void 생성() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목1 = 주문_항목_생성되어_있음();
        OrderLineItem 주문_항목2 = 주문_항목_생성되어_있음();

        // when
        Order order = Order.createOrder(테이블, Arrays.asList(주문_항목1, 주문_항목2));

        // then
        assertThat(order).isNotNull();
    }

    @DisplayName("주문 항목이 1개 미만이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_주문_항목_1개_미만() {
        // given
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목 = 주문_항목_생성되어_있음();

        // when, then
        assertThatThrownBy(() -> Order.createOrder(테이블, Arrays.asList()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목은 하나 이상이어야 합니다.");

    }

    @DisplayName("빈 테이블이면 주문 생성에 실패한다.")
    @Test
    void 생성_예외_빈_테이블() {
        // given
        OrderTable 빈_테이블 = 테이블_생성되어_있음(0, true);
        OrderLineItem 주문_항목1 = 주문_항목_생성되어_있음();
        OrderLineItem 주문_항목2 = 주문_항목_생성되어_있음();

        // when, then
        assertThatThrownBy(() -> Order.createOrder(빈_테이블, Arrays.asList(주문_항목1, 주문_항목2)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블에는 주문을 등록할 수 없습니다.");
    }

    @DisplayName("주문의 상태를 변경한다.")
    @Test
    void 상태_변경() {
        // given
        Order 주문 = 주문_생성되어_있음();

        // when
        주문.changeOrderStatus(OrderStatus.MEAL);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }

    @DisplayName("계산 완료 상태이면 상태 변경에 실패한다.")
    @Test
    void 상태_변경_예외_계산_완료() {
        // given
        Order 주문 = 주문_생성되어_있음();
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.COMPLETION);

        // when, then
        assertThatThrownBy(() -> 주문.changeOrderStatus(OrderStatus.MEAL))
                .isInstanceOf(IllegalStateException.class);
    }

    static OrderLineItem 주문_항목_생성되어_있음() {
        MenuProduct 스테이크_1개 = MenuTest.메뉴_상품_생성되어_있음("스테이크", 200, 1);
        MenuProduct 샐러드_1개 = MenuTest.메뉴_상품_생성되어_있음("샐러드", 100, 1);
        MenuProduct 에이드_2개 = MenuTest.메뉴_상품_생성되어_있음("에이드", 50, 2);
        Menu menu = MenuTest.메뉴_생성되어_있음(
                "커플 메뉴",
                "양식",
                스테이크_1개, 샐러드_1개, 에이드_2개);
        return new OrderLineItem(menu, 1);
    }

    static Order 주문_생성되어_있음() {
        OrderTable 테이블 = 테이블_생성되어_있음(0, false);
        OrderLineItem 주문_항목1 = 주문_항목_생성되어_있음();
        OrderLineItem 주문_항목2 = 주문_항목_생성되어_있음();
        return Order.createOrder(테이블, Arrays.asList(주문_항목1, 주문_항목2));
    }
}

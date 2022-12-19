package kitchenpos.order.domain;

import kitchenpos.orderstatus.domain.StatusTestFixture;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.orderstatus.domain.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.table.ordertable.domain.OrderTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.menu.domain.MenuGroupTestFixture.메뉴_그룹;
import static kitchenpos.menu.domain.MenuProductTestFixture.메뉴_상품;
import static kitchenpos.menu.domain.MenuTestFixture.메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static kitchenpos.product.domain.ProductTestFixture.상품;
import static kitchenpos.table.ordertable.domain.OrderTableTestFixture.두_명의_방문객이_존재하는_테이블_아이디_포함;

@DisplayName("주문 테스트")
public class OrderTest {

    private static final Status 조리_상태 = StatusTestFixture.조리_상태;
    private static final Status 계산_완료_상태 = StatusTestFixture.계산_완료_상태;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final Menu 저장된_메뉴 = 메뉴("자메이카 통다리 1인 세트",
                BigDecimal.ONE,
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE).getId(), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE).getId(), 1))));

        final OrderTable 주문_테이블 = 두_명의_방문객이_존재하는_테이블_아이디_포함();

        final Order 주문 = 주문(
                주문_테이블,
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        assertThat(주문).isEqualTo(주문(
                주문_테이블,
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void 주문_상태_변경_성공() {
        final Menu 저장된_메뉴 = 메뉴("자메이카 통다리 1인 세트",
                BigDecimal.ONE,
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE).getId(), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE).getId(), 1))));

        final Order 주문 = 주문(
                두_명의_방문객이_존재하는_테이블_아이디_포함(),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        주문.changeStatus(계산_완료_상태);

        assertThat(주문.isStatus(계산_완료_상태)).isTrue();
    }

    @DisplayName("주문 상태 변경 예외 - 계산 완료 상태 주문")
    @Test
    void 주문_상태_변경_예외_계산_완료_상태_주문() {
        final Menu 저장된_메뉴 = 메뉴("자메이카 통다리 1인 세트",
                BigDecimal.ONE,
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE).getId(), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE).getId(), 1))));

        final Order 주문 = 주문(
                두_명의_방문객이_존재하는_테이블_아이디_포함(),
                계산_완료_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        assertThatIllegalArgumentException().isThrownBy(() -> 주문.changeStatus(조리_상태));
    }

    public static Order 주문(OrderTable orderTable, Status orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemList) {
        return Order.of(orderTable.getId(), orderStatus, orderedTime, orderLineItemList);
    }
}

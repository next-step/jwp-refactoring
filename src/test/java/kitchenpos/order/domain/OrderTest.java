package kitchenpos.order.domain;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹;
import static kitchenpos.table.application.TableServiceTest.주문_테이블;
import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static kitchenpos.order.domain.OrderTableTest.두_명의_방문객;
import static kitchenpos.order.domain.OrderTableTest.빈_상태;
import static kitchenpos.product.domain.ProductTest.상품;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 테스트")
public class OrderTest {

    public static final OrderStatus 조리_상태 = OrderStatus.COOKING;
    public static final OrderStatus 식사_상태 = OrderStatus.MEAL;
    public static final OrderStatus 계산_완료_상태 = OrderStatus.COMPLETION;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        final Menu 저장된_메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE), 1))));

        final Order 주문 = 주문(
                주문_테이블(두_명의_방문객, 빈_상태),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        assertThat(주문).isEqualTo(주문(
                주문_테이블(두_명의_방문객, 빈_상태),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L)))));
    }

    @DisplayName("주문 상태 변경 성공")
    @Test
    void 주문_상태_변경_성공() {
        final Menu 저장된_메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE), 1))));

        final Order 주문 = 주문(
                주문_테이블(두_명의_방문객, 빈_상태),
                조리_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        주문.changeStatus(OrderStatus.COMPLETION);

        assertThat(주문.isStatus(OrderStatus.COMPLETION)).isTrue();
    }

    @DisplayName("주문 상태 변경 예외 - 계산 완료 상태 주문")
    @Test
    void 주문_상태_변경_예외_계산_완료_상태_주문() {
        final Menu 저장된_메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹("추천 메뉴").getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품("통다리", BigDecimal.ONE), 5),
                        메뉴_상품(상품("콜라", BigDecimal.ONE), 1))));

        final Order 주문 = 주문(
                주문_테이블(두_명의_방문객, 빈_상태),
                계산_완료_상태,
                LocalDateTime.now(),
                OrderLineItemBag.from(Arrays.asList(new OrderLineItem(저장된_메뉴.getId(), 1L))));

        assertThatIllegalArgumentException().isThrownBy(() -> 주문.changeStatus(OrderStatus.COOKING));
    }

    public static Order 주문(OrderTable orderTable, OrderStatus orderStatus, LocalDateTime orderedTime,
            OrderLineItemBag orderLineItemList) {
        return new Order(orderTable, orderStatus, orderedTime, orderLineItemList);
    }
}

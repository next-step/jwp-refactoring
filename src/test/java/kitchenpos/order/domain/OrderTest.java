package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.Collections;

import static kitchenpos.common.domain.PriceTest.가격_생성;
import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.menu.domain.QuantityTest.수량_생성;
import static kitchenpos.order.OrderGenerator.*;
import static kitchenpos.product.ProductGenerator.상품_생성;
import static kitchenpos.table.TableGenerator.주문_테이블_생성;
import static kitchenpos.table.domain.NumberOfGuestsTest.손님_수_생성;
import static org.assertj.core.api.Assertions.*;

public class OrderTest {
    private final long 메뉴_그룹_아이디 = 0;
    private final long 상품_아이디 = 0;
    private final OrderTable 주문_테이블 = 주문_테이블_생성(손님_수_생성(10));
    private final MenuProduct 메뉴_상품 = 메뉴_상품_생성(상품_아이디, 수량_생성(1L));
    private final Menu 메뉴 = 메뉴_생성("메뉴", 1_000, 메뉴_그룹_아이디, 메뉴_상품_목록_생성(Collections.singletonList(메뉴_상품)));

    @DisplayName("주문 생성 시 주문 물품이 포함되어 있지 않으면 예외가 발생해야 한다")
    @Test
    void createOrderByNotIncludeOrderItemsTest() {
        assertThatIllegalArgumentException().isThrownBy(() -> 주문_생성(주문_테이블, 주문_물품_목록_생성()));
    }

    @DisplayName("정상 상태로 주문을 생성하면 정상 생성되어야 한다")
    @Test
    void createOrderTest() {
        OrderLineItem 주문_물품 = 주문_물품_생성(메뉴, 1);
        주문_테이블.updateEmpty(false);
        assertThatNoException().isThrownBy(() -> 주문_생성(주문_테이블, 주문_물품_목록_생성(주문_물품)));
    }

    @DisplayName("완료 상태의 주문의 상태를 변경하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void orderChangeStatusByCompletionStateTest(OrderStatus orderStatus) {
        // given
        OrderLineItem 주문_물품 = 주문_물품_생성(메뉴, 1);
        주문_테이블.updateEmpty(false);
        Order 주문 = 주문_생성(주문_테이블, 주문_물품_목록_생성(주문_물품));
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        // then
        assertThatIllegalArgumentException().isThrownBy(() -> 주문.changeOrderStatus(orderStatus));
    }

    @DisplayName("완료가 아닌 주문의 상태를 변경하면 정상 변경되어야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL", "COMPLETION" })
    void orderChangeStatusTest(OrderStatus orderStatus) {
        // given
        OrderLineItem 주문_물품 = 주문_물품_생성(메뉴, 1);
        주문_테이블.updateEmpty(false);
        Order 주문 = 주문_생성(주문_테이블, 주문_물품_목록_생성(주문_물품));

        // when
        주문.changeOrderStatus(orderStatus);

        // then
        assertThat(주문.getOrderStatus()).isEqualTo(orderStatus);
    }
}

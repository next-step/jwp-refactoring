package kitchenpos.order.domain;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_생성;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_상품_생성;
import static kitchenpos.menu.application.MenuServiceTest.메뉴_생성;
import static kitchenpos.product.application.ProductServiceTest.상품_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class OrderTest {
    private Product 후라이드;
    private MenuProduct 후라이드_원플원;

    private MenuGroup 추천_메뉴;
    private Menu 후라이드_세트_메뉴;

    private Order 주문;
    private OrderTable 주문_테이블;
    private OrderLineItem 주문_목록_추천_치킨;

    @BeforeEach
    void init() {
        // given
        주문_테이블 = 주문_테이블_생성(1L, 4, false);

        후라이드 = 상품_생성(1L, "후라이드", 16_000L);
        후라이드_원플원 = 메뉴_상품_생성(후라이드.getId(), 1L);

        추천_메뉴 = 메뉴_그룹_생성(1L, "추천메뉴");
        후라이드_세트_메뉴 = 메뉴_생성(1L, "후라이드_원플원", 16_000L, 추천_메뉴, Arrays.asList(후라이드_원플원));

        주문_목록_추천_치킨 = 주문_목록_생성(주문, 후라이드_세트_메뉴, 2);
    }

    @Test
    @DisplayName("주문 목록이 없이 주문을 생성할 경우 - 오류")
    void createOrderIfOrderLineItemsIsEmpty() {
        // given
        OrderTable 빈_주문_테이블 = 주문_테이블_생성(2L, 4, true);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList());

        // when then
        assertThatThrownBy(() -> 주문_생성(1L, 빈_주문_테이블, orderLineItems))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 주문 테이블로 주문을 생성할 경우 - 오류")
    void createOrderIfOrderTableIsEmpty() {
        // given
        OrderTable 빈_주문_테이블 = 주문_테이블_생성(2L, 4, true);
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));

        // when then
        assertThatThrownBy(() -> 주문_생성(1L, 빈_주문_테이블, orderLineItems))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문의 상태를 변경한다.")
    void changeOrderStatus() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, orderLineItems);

        주문.updateOrderStatus(OrderStatus.MEAL.name());

        assertThat(주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("저장된 '주문 완료' 상태의 주문을 변경할 경우 - 오류")
    void changeOrderStatusIfOrderStatusIsCompletion() {
        // given
        OrderLineItems orderLineItems = new OrderLineItems(Arrays.asList(주문_목록_추천_치킨));
        주문 = 주문_생성(1L, 주문_테이블, OrderStatus.COMPLETION, orderLineItems);

        // when then
        assertThatThrownBy(() -> 주문.updateOrderStatus(OrderStatus.MEAL.name()))
            .isInstanceOf(IllegalArgumentException.class);
    }

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderLineItems);
    }

    public static Order 주문_생성(Long id, OrderTable orderTable, OrderStatus orderStatus, OrderLineItems orderLineItems) {
        return new Order(id, orderTable, orderStatus, orderLineItems);
    }

    public static OrderTable 주문_테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public static OrderLineItem 주문_목록_생성(Order order, Menu menu, int quantity) {
        return new OrderLineItem(order, menu, quantity);
    }
}

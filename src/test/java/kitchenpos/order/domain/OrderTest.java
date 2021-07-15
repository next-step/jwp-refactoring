package kitchenpos.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 관련 기능 테스트")
public class OrderTest {
    private TableGroup tableGroup;
    private Menu menu;
    private OrderTable orderTable;
    private OrderLineItem orderLineItem;
    @BeforeEach
    void setUp() {
        tableGroup = new TableGroup(1L);
        menu = new Menu(1L, "뿌링클치즈볼", new BigDecimal(18000), new MenuGroup());

        orderTable = 주문테이블_생성(1L, tableGroup, 3, false);
        orderLineItem = 주문_목록_생성(1L, menu.getId(), 3L);
    }

    @DisplayName("주문을 등록한다.")
    @Test
    void create() {
        Order createdOrder = 주문_생성_요청(orderTable, OrderStatus.COOKING);

        주문_등록됨(createdOrder);
    }

    @DisplayName("주문 상태를 변경한다.")
    @Test
    void changedStatus() {
        Order createdOrder = 주문_생성_요청(orderTable, OrderStatus.COOKING);

        주문_상태_변경_요청(createdOrder, OrderStatus.MEAL);

        주문_상태_변경됨(createdOrder);
    }

    @DisplayName("주문이 완료된 경우 상태변경이 불가능하다.")
    @Test
    void changeException() {
        Order createdOrder = 주문_생성_요청(orderTable, OrderStatus.COMPLETION);

        완료된_주문_변경시_예외_발생(createdOrder, OrderStatus.COOKING);
    }

    private void 주문_등록됨(Order createdOrder) {
        assertThat(createdOrder).isNotNull();
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING);
    }

    public static OrderTable 주문테이블_생성(Long id, TableGroup tableGroup, int numberOfGuest, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuest, empty);
    }


    private void 주문_상태_변경됨(Order createdOrder) {
        assertThat(createdOrder.getOrderStatus()).isEqualTo(OrderStatus.MEAL);
    }


    public static OrderLineItem 주문_목록_생성(Long seq, Long menuId, Long quantity) {
        return new OrderLineItem(seq, menuId, quantity);
    }

    public static Order 주문_생성_요청(OrderTable orderTable, OrderStatus orderStatus) {
        return new Order(orderTable, orderStatus);
    }

    private void 완료된_주문_변경시_예외_발생(Order createdOrder, OrderStatus cooking) {
        assertThatThrownBy(() -> {
            createdOrder.updateOrderStatus(cooking);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("완료된 주문은 상태를 변경할 수 없습니다.");
    }

    private void 주문_상태_변경_요청(Order createdOrder, OrderStatus meal) {
        createdOrder.updateOrderStatus(meal);
    }

}

package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.exception.CannotChangeOrderStatusException;
import kitchenpos.order.exception.CannotOrderException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테스트")
public class OrderTest {

    public static final OrderTable 주문테이블 = new OrderTable(1L, 2, false);
    public static final OrderLineItem 첫번째_주문항목 = new OrderLineItem(1L, 1L, 1);
    public static final OrderLineItem 두번째_주문항목 = new OrderLineItem(1L, 2L, 1);
    public static final List<OrderLineItem> 주문_항목_목록 = new ArrayList<>(Arrays.asList(첫번째_주문항목, 두번째_주문항목));

    @DisplayName("주문 항목은 1개 이상이어야 한다.")
    @Test
    void create_Fail_01() {
        // Given
        List<OrderLineItem> 비어있는_주문_항목_목록 = new ArrayList<>();
        Long 주문테이블ID = 주문테이블.getId();

        // When & Then
        assertThatThrownBy(() -> new Order(주문테이블ID, 비어있는_주문_항목_목록))
            .isInstanceOf(CannotOrderException.class);
    }

    @DisplayName("주문상태가 계산완료인 주문은 변경할 수 없다.")
    @Test
    void changeOrderStatus_Fail() {
        // Given
        Order order = new Order(1L, 주문테이블.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), 주문_항목_목록);

        // When & Then
        assertThatThrownBy(() -> order.changeOrderStatus(OrderStatus.MEAL))
            .isInstanceOf(CannotChangeOrderStatusException.class);
    }

}

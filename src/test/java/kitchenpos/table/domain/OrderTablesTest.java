package kitchenpos.table.domain;

import static kitchenpos.order.domain.OrderTest.주문_항목_목록;
import static kitchenpos.order.domain.OrderTest.주문테이블;
import static kitchenpos.table.application.TableServiceTest.두명;
import static kitchenpos.table.domain.TableGroupTest.첫번째_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.exception.CannotUngroupOrderTableException;
import kitchenpos.table.exception.OutOfOrderTableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문테이블 목록 테스트")
public class OrderTablesTest {

    @DisplayName("단체 지정될 주문테이블은 2개 이상이어야 한다.")
    @Test
    void create_Fail() {
        List<OrderTable> 비어있는_주문테이블_목록 = new ArrayList<>();
        assertThatThrownBy(() -> new OrderTables(비어있는_주문테이블_목록))
            .isInstanceOf(OutOfOrderTableException.class);

        List<OrderTable> 한개만_있는_주문테이블_목록 = new ArrayList<>(Arrays.asList(첫번째_주문테이블));
        assertThatThrownBy(() -> new OrderTables(한개만_있는_주문테이블_목록))
            .isInstanceOf(OutOfOrderTableException.class);
    }

    @DisplayName("진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.")
    @Test
    void ungroup_Fail() {
        // Given
        Order 첫번째_주문 = new Order(1L, 주문테이블.getId(), OrderStatus.COOKING, LocalDateTime.now(), 주문_항목_목록);
        Order 두번째_주문 = new Order(2L, 주문테이블.getId(), OrderStatus.COMPLETION, LocalDateTime.now(), 주문_항목_목록);
        List<OrderTable> 주문테이블_목록 = new ArrayList<>();
        주문테이블_목록.add(new OrderTable(1L, 두명, 첫번째_주문));
        주문테이블_목록.add(new OrderTable(2L, 두명, 두번째_주문));

        // When & Then
        OrderTables orderTables = new OrderTables(주문테이블_목록);
        assertThatThrownBy(orderTables::upgroup)
            .isInstanceOf(CannotUngroupOrderTableException.class);
    }
}

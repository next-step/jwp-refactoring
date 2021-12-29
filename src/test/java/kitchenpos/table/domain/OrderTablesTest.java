package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;

public class OrderTablesTest {
    
    @DisplayName("목록에 새 주문 테이블을 추가한다")
    @Test
    void 주문_테이블_추가() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(5, false);
        OrderTable 두번째_테이블 = OrderTable.of(3, false);
        OrderTables 주문_테이블_목록 = OrderTables.from(new ArrayList<OrderTable>(Arrays.asList(첫번째_테이블)));
        
        // when
        주문_테이블_목록.add(두번째_테이블);
        
        // then
        assertAll(
                () -> assertThat(주문_테이블_목록.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(주문_테이블_목록.getOrderTables()).isEqualTo(Arrays.asList(첫번째_테이블, 두번째_테이블))
        );
    }
    
    @DisplayName("주문 테이블을 해제한다")
    @Test
    void 주문_테이블_해제() {
        // given
        OrderTable 테이블 = OrderTable.of(5, true);
        OrderTables 주문_테이블_목록 = OrderTables.from(new ArrayList<OrderTable>(Arrays.asList(테이블)));
        
        // when
        주문_테이블_목록.ungroup();
        
        // then
        assertAll(
                () -> assertThat(주문_테이블_목록.getOrderTables().size()).isEqualTo(0),
                () -> assertThat(테이블.isEmpty()).isFalse()
        );
    }
}

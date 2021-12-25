package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TableGroupTest {
    
    @Test
    @DisplayName("단체지정이 잘 생성되는지 확인")
    void 단체지정_생성() {
        // given
        List<OrderTable> 주문_테이블_목록 = new ArrayList<OrderTable>();
        주문_테이블_목록.add(OrderTable.of(3, true));
        주문_테이블_목록.add(OrderTable.of(5, true));
        
        // when
        TableGroup 단체_지정 = TableGroup.from(주문_테이블_목록);
        
        // then
        assertThat(단체_지정).isEqualTo(TableGroup.from(주문_테이블_목록));
    }
    
    @DisplayName("단체지정은 최소 두 테이블 이상만 가능 - 예외처리")
    @Test
    void 단체지정_테이블_확인() {
        // given, when, then
        assertThatThrownBy(() -> {
            TableGroup.from(Arrays.asList(OrderTable.of(null, 3, true)));
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("단체지정은 최소 두 테이블 이상만 가능합니다");
    }
    
    @DisplayName("단체지정이 해제되는지 확인")
    @Test
    void 단체지정_해제() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(null, 3, false);
        OrderTable 두번째_테이블 = OrderTable.of(null, 5, false);
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        첫번째_테이블.updateTableGroup(단체지정);
        두번째_테이블.updateTableGroup(단체지정);
        
        // when
        단체지정.ungroup();

        // then
        assertAll(
                () -> assertThat(첫번째_테이블.getTableGroupId()).isNull(),
                () -> assertThat(두번째_테이블.getTableGroupId()).isNull()
        );
    }
    
    @DisplayName("조리중, 식사중인 주문 테이블은 단체지정을 해제할 수 없다")
    @Test
    void 조리중_식사중_테이블_단체지정_해제_불가() {
        // given
        OrderTable 첫번째_테이블 = OrderTable.of(null, 3, false);
        OrderTable 두번째_테이블 = OrderTable.of(null, 5, false);
        첫번째_테이블.addOrders(Arrays.asList(Order.of(null, OrderStatus.COOKING, new ArrayList<OrderLineItem>())));
        
        TableGroup 단체지정 = TableGroup.from(Arrays.asList(첫번째_테이블, 두번째_테이블));
        첫번째_테이블.updateTableGroup(단체지정);
        두번째_테이블.updateTableGroup(단체지정);
        
        // when, then
        assertThatThrownBy(() -> {
            단체지정.ungroup();
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessage("조리중, 식사중인 주문 테이블은 단체지정을 해제할 수 없습니다");
    }

}

package kitchenpos.domain.tableGroup;

import kitchenpos.domain.orderTable.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class GroupingServiceTest {
    private GroupingService groupingService = new GroupingService();

    @DisplayName("주문 테이블들을 단체 지정 할 수 있다.")
    @Test
    void groupingTest() {
        TableGroup tableGroup = new TableGroup(1L, LocalDateTime.now(), Arrays.asList(
                new OrderTableInTableGroup(1L), new OrderTableInTableGroup(2L)));
        OrderTable orderTable1 = new OrderTable(0, true);
        OrderTable orderTable2 = new OrderTable(0, true);

         TableGroup grouped = groupingService.group(tableGroup, Arrays.asList(orderTable1, orderTable2));

         assertThat(grouped.getCreatedDate()).isNotNull();
         assertThat(grouped.getOrderTables()).hasSize(2);
         assertThat(orderTable1.getTableGroupId()).isNotNull();
         assertThat(orderTable2.getTableGroupId()).isNotNull();
    }
}
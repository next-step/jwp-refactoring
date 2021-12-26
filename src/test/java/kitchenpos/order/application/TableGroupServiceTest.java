package kitchenpos.order.application;

import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Table Group 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTableGroupTest(){
        //given
        OrderTable orderTable = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        TableGroup tableGroup1 = mock(TableGroup.class);

        //when
        when(tableGroup1.getOrderTables()).thenReturn(Arrays.asList(orderTable, orderTable2));

        //then
        assertThat(tableGroup1.getOrderTables()).contains(orderTable, orderTable2);
    }

    @Test
    @DisplayName("테이블 그룹을 해제한다.")
    void deleteGroupTest(){
        //given
        OrderTable orderTable = mock(OrderTable.class);
        OrderTable orderTable2 = mock(OrderTable.class);
        TableGroup tableGroup1 = new TableGroup(LocalDateTime.now(), Arrays.asList(orderTable, orderTable2));

        //when
        tableGroup1.cancleGroup();

        assertThat(tableGroup1.getOrderTables().size()).isEqualTo(0);
    }
}

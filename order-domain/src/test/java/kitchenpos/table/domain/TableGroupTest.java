package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.anyOf;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@ExtendWith(MockitoExtension.class)
public class TableGroupTest {

    @Mock
    private TableValidator tableValidator;

    @Test
    @DisplayName("주문 테이블 그룹화")
    public void toGroup() {
        //given
        doNothing().when(tableValidator).validateTableGroupCreatable(ArgumentMatchers.any(OrderTables.class));
        TableGroup tableGroup = new TableGroup();
        OrderTables orderTables = new OrderTables(Arrays.asList(
            new OrderTable(1L, 5, true),
            new OrderTable(2L, 3, true)
        ));

        //when
        orderTables.toGroup(tableValidator, tableGroup);

        //then
        assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNotNull();
        assertThat(tableGroup.getOrderTables().getOrderTables()).containsAll(
            orderTables.getOrderTables());
    }

    @Test
    @DisplayName("주문 테이블 그룹해제")
    public void ungroup() {
        //given
        doNothing().when(tableValidator).validateTableGroupCreatable(ArgumentMatchers.any(OrderTables.class));
        doNothing().when(tableValidator).validateUngroupPossible(ArgumentMatchers.any(OrderTables.class));

        TableGroup tableGroup = new TableGroup();
        OrderTables orderTables = new OrderTables(Arrays.asList(
            new OrderTable(1L, 5, true),
            new OrderTable(2L, 3, true)
        ));
        orderTables.toGroup(tableValidator, tableGroup);

        //when
        orderTables.ungroup(tableValidator);

        //then
        assertThat(orderTables.getOrderTables().get(0).getTableGroup()).isNull();
        assertThat(orderTables.getOrderTables().get(1).getTableGroup()).isNull();
    }
}

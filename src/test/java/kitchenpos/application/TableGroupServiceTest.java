package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 생성한다.")
    @Test
    public void createTableGroup() {

        //given
        TableGroup given = new TableGroup(1l, createOrderTables());
        when(orderTableDao.findAllByIdIn(any())).thenReturn(createOrderTables());
        when(tableGroupDao.save(any())).thenReturn(given);
        //when
        TableGroup result = tableGroupService.create(given);
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 보다 적으면 에러")
    @Test
    public void createTableGroupWithLessOrderTables() {
        //given
        TableGroup given = new TableGroup(1l, Arrays.asList(new OrderTable(1l, null, 1, true)));

        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 모두 등록되어 있지 않으면 에러")
    @Test
    public void createTableGroupWithNoExistOrderTables() {
        //given
        TableGroup given = new TableGroup(1l, createOrderTables());
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(new OrderTable(1l, null, 1, true)));
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈값 아니면 에러")
    @Test
    public void createTableGroupWithEmptyOrderTables() {
        //given
        TableGroup given = new TableGroup(1l, createNotEmptyOrderTables());
        when(orderTableDao.findAllByIdIn(any())).thenReturn(createNotEmptyOrderTables());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 테이블이 단체 지정이 되어 있으면 에러")
    @Test
    public void createTableGroupExist() {
        //given
        TableGroup given = new TableGroup(1l, createHasTableGroupOrderTables());
        when(orderTableDao.findAllByIdIn(any())).thenReturn(createHasTableGroupOrderTables());
        //when
        //then
        assertThatThrownBy(() -> tableGroupService.create(given)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    public void ungroup(){
        //given
        OrderTable orderTable = new OrderTable(1l, 1l, 1, true);
        OrderTable orderTable2 = new OrderTable(2l, 1l, 1, true);

        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(Arrays.asList(orderTable, orderTable2));
        //when
        tableGroupService.ungroup(1l);
        //then
        assertAll(() -> assertThat(orderTable.getTableGroupId()).isNull(),
            () -> assertThat(orderTable2.getTableGroupId()).isNull());
    }

    @DisplayName("조리, 식사 중인 상태인 경우에는 해제할 수 없다.")
    @Test
    public void ungroupWithStatus(){
        //given
        when(orderTableDao.findAllByTableGroupId(any())).thenReturn(createOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);
        //when
        //then
        assertThatThrownBy(() ->  tableGroupService.ungroup(1l)).isInstanceOf(IllegalArgumentException.class);

    }

    private List<OrderTable> createOrderTables() {
        return Arrays.asList(new OrderTable(1l, null, 1, true), new OrderTable(2l, null, 1, true));
    }

    private List<OrderTable> createNotEmptyOrderTables() {
        return Arrays.asList(new OrderTable(1l, null, 1, false), new OrderTable(2l, null, 1, false));
    }

    private List<OrderTable> createHasTableGroupOrderTables() {
        return Arrays.asList(new OrderTable(1l, 1l, 1, true), new OrderTable(2l, 1l, 1, true));
    }
}

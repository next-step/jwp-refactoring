package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TableServiceTest extends BaseTest {
    @Autowired
    private TableService tableService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블 등록")
    @Test
    void create() {
        List<OrderTable> orderTables = tableService.list();
        OrderTable expected = orderTables.get(0);

        OrderTable actual = tableService.create(expected);

        assertThat(actual.getNumberOfGuests()).isEqualTo(expected.getNumberOfGuests());
        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경")
    @Test
    void changeEmpty() {
        TableGroup params = tableGroupDao.findById(1L).get();
        List<OrderTable> orderTables = tableService.list();
        params.setOrderTables(orderTables);
        OrderTable expected = orderTables.get(0);

        OrderTable actual = tableService.changeEmpty(expected.getId(), expected);

        assertThat(actual.isEmpty()).isEqualTo(expected.isEmpty());
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 단체 지정인 경우")
    @Test
    void validExistTableGroup() {
        TableGroup params = tableGroupDao.findById(1L).get();
        List<OrderTable> orderTables = tableService.list();
        params.setOrderTables(orderTables);
        tableGroupService.create(params);
        OrderTable expected = orderTables.get(0);

        assertThatThrownBy(()->{
            tableService.changeEmpty(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 비어있는지 여부 변경 예외 - 요리중이거나 식사중인 경우")
    @Test
    void validOrderStatusCookingOrMeal() {
        TableGroup params = tableGroupDao.findById(1L).get();
        List<OrderTable> orderTables = tableService.list();
        params.setOrderTables(orderTables);
        OrderTable expected = orderTables.get(0);
        OrderDao orderDao = mock(OrderDao.class);
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .thenReturn(true);

        assertThatThrownBy(()->{
            tableService.changeEmpty(expected.getId(), expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
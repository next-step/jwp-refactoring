package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private TableGroup tableGroup;
    private OrderTable orderTable1;
    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setNumberOfGuests(4);
        orderTable1.setEmpty(true);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setNumberOfGuests(2);
        orderTable2.setEmpty(true);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
    }

    @Test
    @DisplayName("단체지정 등록")
    void create() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupDao.save(any())).thenReturn(tableGroup);
        when(orderTableDao.save(any())).thenReturn(orderTable1, orderTable2);

        assertThat(tableGroupService.create(tableGroup)).isNotNull();
    }

    @Test
    @DisplayName("단체지정 등록시 주문테이블이 2개 미만이면 등록할 수 없음")
    void callExceptionCreate() {
        when(orderTableDao.findAllByIdIn(any())).thenReturn(Arrays.asList(orderTable1));

        assertThatThrownBy(() -> {
            tableGroupService.create(tableGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체지정 삭제")
    void ungroup() {
        when(orderTableDao.findAllByTableGroupId(any()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderTableDao.save(any())).thenReturn(orderTable1, orderTable2);

       tableGroupService.ungroup(tableGroup.getId());
    }

    @Test
    @DisplayName("단체지정 삭제시 주문상태가 조리, 식사일 경우 삭제할 수 없음")
    void callExceptionUngroup() {
        when(orderTableDao.findAllByTableGroupId(any()))
                .thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
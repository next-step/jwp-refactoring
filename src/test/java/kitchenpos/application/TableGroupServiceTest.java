package kitchenpos.application;

import static java.util.Collections.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@SpringBootTest
class TableGroupServiceTest {
    @MockBean
    private OrderDao orderDao;
    @MockBean
    private OrderTableDao orderTableDao;
    @MockBean
    private TableGroupDao tableGroupDao;

    private TableGroupService tableGroupService;
    private TableGroup 테이블그룹_1번_2번;
    OrderTable 테이블_1번, 테이블_2번;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        테이블_1번 = new OrderTable();
        테이블_1번.setId(1L);
        테이블_1번.setEmpty(true);

        테이블_2번 = new OrderTable();
        테이블_2번.setId(2L);
        테이블_2번.setEmpty(true);

        테이블그룹_1번_2번 = new TableGroup();
        테이블그룹_1번_2번.setId(1L);
        테이블그룹_1번_2번.setOrderTables(Arrays.asList(테이블_1번, 테이블_2번));
    }

    @DisplayName("단체 지정 생성 성공")
    @Test
    void create() {
        when(orderTableDao.findAllByIdIn(테이블그룹_1번_2번.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(toList()))).thenReturn(테이블그룹_1번_2번.getOrderTables());
        when(tableGroupDao.save(테이블그룹_1번_2번)).thenReturn(테이블그룹_1번_2번);
        when(orderTableDao.save(any())).thenReturn(any());

        assertThat(tableGroupService.create(테이블그룹_1번_2번)).isEqualTo(테이블그룹_1번_2번);
    }

    @DisplayName("단체 지정 생성 실패 : 빈테이블이 아닌 경우")
    @Test
    void createFailCauseNotEmpty() {
        테이블_1번.setEmpty(false);
        when(orderTableDao.findAllByIdIn(테이블그룹_1번_2번.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(toList()))).thenReturn(테이블그룹_1번_2번.getOrderTables());

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_1번_2번)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 생성 실패 : 주문 테이블 크기가 2보다 작을 경우")
    @Test
    void createFailCauseTableSize() {
        when(orderTableDao.findAllByIdIn(테이블그룹_1번_2번.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(toList()))).thenReturn(singletonList(테이블_1번));

        assertThatThrownBy(() -> tableGroupService.create(테이블그룹_1번_2번)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제 성공")
    @Test
    void ungroup() {
        when(orderTableDao.findAllByTableGroupId(테이블그룹_1번_2번.getId())).thenReturn(테이블그룹_1번_2번.getOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(테이블그룹_1번_2번.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(toList()), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(any())).thenReturn(any());
        tableGroupService.ungroup(테이블그룹_1번_2번.getId());
    }

    @DisplayName("주문상태로 인한 단체 지정 해제 실패")
    @Test
    void ungroupFailCauseOrderStatusInCookingOrMeal() {
        when(orderTableDao.findAllByTableGroupId(테이블그룹_1번_2번.getId())).thenReturn(테이블그룹_1번_2번.getOrderTables());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(테이블그룹_1번_2번.getOrderTables().stream()
            .map(OrderTable::getId)
            .collect(toList()), Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(테이블그룹_1번_2번.getId())).isInstanceOf(IllegalArgumentException.class);
    }
}
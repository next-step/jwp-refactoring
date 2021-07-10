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
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("테이블 그룹 관련 기능 테스트")
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

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable();
        테이블1.setId(1L);
        테이블1.setEmpty(true);
        테이블1.setNumberOfGuests(0);
        테이블1.setTableGroupId(null);

        테이블2 = new OrderTable();
        테이블2.setId(2L);
        테이블2.setEmpty(true);
        테이블2.setNumberOfGuests(0);
        테이블2.setTableGroupId(null);
    }

    @Test
    void 테이블_그룹_생성() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(테이블1, 테이블2));

        when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(테이블1, 테이블2));
        when(tableGroupDao.save(tableGroup)).thenReturn(tableGroup);

        when(orderTableDao.save(테이블1)).thenReturn(테이블1);
        when(orderTableDao.save(테이블1)).thenReturn(테이블2);

        TableGroup expected = tableGroupService.create(tableGroup);
        assertThat(expected.getId()).isEqualTo(tableGroup.getId());
        assertThat(expected.getOrderTables().size()).isEqualTo(2);
        Set<Long> tableGroupIds = expected.getOrderTables().stream().map(OrderTable::getTableGroupId).collect(Collectors.toSet());
        assertThat(tableGroupIds.size()).isEqualTo(1);
        assertThat(tableGroupIds).contains(1L);
    }

    @Test
    void 주문테이블이_비어있는값으로_테이블그룹_생성_요청_시_에러_발생() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Collections.emptyList());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블이_1개만있을때_테이블그룹_생성_요청_시_에러_발생() {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(테이블1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 요청한_주문_테이블들과_실제_조회한_주문테이블의_갯수가_다를때_에러발생() {
        OrderTable 테이블3 = new OrderTable();
        테이블3.setId(99L);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(테이블1, 테이블3));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 주문테이블의_상태가_비어있지않는_주문테이블의_테이블_그룹_생성_요청_시_에러발생() {
        테이블1.setEmpty(false);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(테이블1, 테이블2));
        when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(테이블1, 테이블2));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 이미_테이블_그룹에_소속된_주문테이블이_포함된_주문테이블_리스트를_이용하여_테이블_그룹_생성_요청_시_에러발생() {
        테이블1.setTableGroupId(2L);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(테이블1, 테이블2));
        when(orderTableDao.findAllByIdIn(Arrays.asList(1L, 2L))).thenReturn(Arrays.asList(테이블1, 테이블2));
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 테이블_그룹_그룹핑_해제() {
        테이블1.setTableGroupId(1L);
        테이블1.setTableGroupId(1L);
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(테이블1, 테이블2));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), orderStatuses)).thenReturn(false);
        when(orderTableDao.save(테이블1)).thenReturn(테이블1);
        when(orderTableDao.save(테이블2)).thenReturn(테이블2);

        tableGroupService.ungroup(1L);
    }

    @Test
    void 테이블_그룹에_소속된_주문_테이블의_상태가_조리중_또는_식사중인_경우_에러발생() {
        테이블1.setTableGroupId(1L);
        테이블1.setTableGroupId(2L);
        when(orderTableDao.findAllByTableGroupId(1L)).thenReturn(Arrays.asList(테이블1, 테이블2));
        List<String> orderStatuses = Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name());
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(1L, 2L), orderStatuses)).thenReturn(true);
        assertThatThrownBy(() -> tableGroupService.ungroup(1L)).isInstanceOf(IllegalArgumentException.class);
    }
}

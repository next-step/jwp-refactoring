package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.dao.OrderDao;
import kitchenpos.order.dao.OrderTableDao;
import kitchenpos.table.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.application.TableGroupService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 서비스에 대한 테스트")
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

    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블_2;
    private TableGroup 테이블_그룹;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTable.of(1L, null, 3, true);
        주문_테이블_2 = OrderTable.of(2L, null, 5, true);
        테이블_그룹 = TableGroup.of(1L, null, Arrays.asList(주문_테이블, 주문_테이블_2));
    }

    @DisplayName("주문 테이블을 단체지정하면 정상적으로 단체지정 되어야한다")
    @Test
    void create_test() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문_테이블.getId(), 주문_테이블_2.getId())))
            .thenReturn(테이블_그룹.getOrderTables());
        when(tableGroupDao.save(테이블_그룹))
            .thenReturn(테이블_그룹);

        when(orderTableDao.save(주문_테이블))
            .thenReturn(주문_테이블);
        when(orderTableDao.save(주문_테이블_2))
            .thenReturn(주문_테이블_2);

        // when
        TableGroup result = tableGroupService.create(테이블_그룹);

        // then
        List<OrderTable> 그룹_ID_있는_테이블 = result.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertAll(
            () -> assertThat(result.getCreatedDate()).isNotNull(),
            () -> assertThat(그룹_ID_있는_테이블).hasSize(2),
            () -> assertThat(result.getOrderTables().size()).isEqualTo(테이블_그룹.getOrderTables().size())
        );
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test() {
        // given
        테이블_그룹.setOrderTables(Collections.emptyList());

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 없거나 2개 미만이면 예외가 발생한다")
    @Test
    void create_exception_test2() {
        // given
        테이블_그룹.setOrderTables(Arrays.asList(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 모두 존재하지 않으면 예외갑 발생한다")
    @Test
    void create_exception_test3() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문_테이블.getId(), 주문_테이블_2.getId())))
            .thenReturn(Arrays.asList(주문_테이블));

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정시 단체지정할 테이블이 비어있지 않거나 "
        + "이미 테이블 그룹이 존재하면 예외가 발생한다")
    @Test
    void create_exception_test4() {
        // given
        주문_테이블.setEmpty(false);
        주문_테이블_2.setTableGroupId(3L);
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문_테이블.getId(), 주문_테이블_2.getId())))
            .thenReturn(테이블_그룹.getOrderTables());

        // then
        assertThatThrownBy(() -> {
            tableGroupService.create(테이블_그룹);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("테이블 단체지정을 해제하면 정상적으로 해제되어야 한다")
    @Test
    void ungroup_test() {
        // given
        주문_테이블.setTableGroupId(3L);
        주문_테이블_2.setTableGroupId(3L);
        when(orderTableDao.findAllByTableGroupId(테이블_그룹.getId()))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블_2));

        when(orderTableDao.save(주문_테이블))
            .thenReturn(주문_테이블);
        when(orderTableDao.save(주문_테이블_2))
            .thenReturn(주문_테이블_2);

        // when
        tableGroupService.ungroup(테이블_그룹.getId());

        // then
        List<OrderTable> 그룹_ID가_존재하는_테이블_목록 = 테이블_그룹.getOrderTables().stream()
                .filter(it -> Objects.nonNull(it.getTableGroupId()))
                .collect(Collectors.toList());

        assertThat(그룹_ID가_존재하는_테이블_목록).hasSize(0);
    }

    @DisplayName("단체지정을 해제시 테이블의 주문이 요리중, 식사중인 상태가 있으면 예외가 발생한다")
    @Test
    void ungroup_exception_test() {
        // given
        when(orderTableDao.findAllByTableGroupId(테이블_그룹.getId()))
            .thenReturn(Arrays.asList(주문_테이블, 주문_테이블_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
            Arrays.asList(주문_테이블.getId(), 주문_테이블_2.getId()),
            Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())
        )).thenReturn(true);

        // then
        assertThatThrownBy(() -> {
            tableGroupService.ungroup(테이블_그룹.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

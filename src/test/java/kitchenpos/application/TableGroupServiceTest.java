package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import kitchenpos.table.application.TableGroupService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroup 단체지정;

    @BeforeEach
    void setUp() {
        주문테이블1 = OrderTable.of(1L, null, 0, true);
        주문테이블2 = OrderTable.of(2L, null, 0, true);
        단체지정 = TableGroup.of(1L, LocalDateTime.MIN, Arrays.asList(주문테이블1, 주문테이블2));
    }

    @DisplayName("단체 지정을 한다.")
    @Test
    void 단체_지정() {
        // given
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()))).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(tableGroupDao.save(단체지정)).thenReturn(단체지정);
        when(orderTableDao.save(주문테이블1)).thenReturn(주문테이블1);
        when(orderTableDao.save(주문테이블2)).thenReturn(주문테이블2);

        // when
        TableGroup savedTableGroup = tableGroupService.create(단체지정);

        // then
        assertAll(
                () -> assertThat(savedTableGroup.getId()).isNotNull(),
                () -> assertThat(savedTableGroup.getOrderTables()).containsOnly(주문테이블1, 주문테이블2)
        );
    }

    @DisplayName("테이블을 2개 이상 지정하지 않으면 단체 지정을 할 수 없다.")
    @Test
    void 단일_테이블_단체_지정() {
        // given
        TableGroup tableGroup = TableGroup.of(2L, null, Collections.singletonList(주문테이블1));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("등록된 테이블이 아니면 단체 지정을 할 수 없다.")
    @Test
    void 등록되지_않은_테이블_단체_지정() {
        // given
        OrderTable 주문테이블3 = OrderTable.of(3L, null, 0, true);
        TableGroup tableGroup = TableGroup.of(4L, null, Arrays.asList(주문테이블1, 주문테이블2, 주문테이블3));
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId(), 주문테이블3.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정된 테이블은 단체 지정을 할 수 없다.")
    @Test
    void 이미_단체_지정된_테이블_단체_지정() {
        OrderTable 단체지정된테이블 = OrderTable.of(4L, 10L, 0, true);
        TableGroup tableGroup = TableGroup.of(5L, null, Arrays.asList(주문테이블1, 주문테이블2, 단체지정된테이블));
        when(orderTableDao.findAllByIdIn(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId(), 단체지정된테이블.getId())))
                .thenReturn(Arrays.asList(주문테이블1, 주문테이블2, 단체지정된테이블));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void 단체_지정_해제() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체지정.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(false);

        // when
        tableGroupService.ungroup(단체지정.getId());

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroupId()).isNull(),
                () -> assertThat(주문테이블2.getTableGroupId()).isNull()
        );
    }

    @DisplayName("조리중이거나 식사중인 테이블은 단체 지정을 해제할 수 없다.")
    @Test
    void 조리중_식사중인_테이블_단체_지정_해제() {
        // given
        when(orderTableDao.findAllByTableGroupId(단체지정.getId())).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .thenReturn(true);

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(단체지정.getId()));
    }
}

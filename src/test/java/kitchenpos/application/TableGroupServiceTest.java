package kitchenpos.application;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @DisplayName("create 실패 - orderTable이 비어있음")
    @Test
    void createFail01() {
        // given
        TableGroup tableGroup = new TableGroup();

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("create 실패 - orderTable 개수가 2개 미만")
    @Test
    void createFail02() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Collections.singletonList(new OrderTable()));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("create 실패 - orderTable 목록에 중복된 것이 존재")
    @Test
    void createFail03() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(orderTable(1L, true), orderTable(1L, true)));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(orderTable(1L, true)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("create 실패 - 저장된 orderTable 이 empty 상태가 아님")
    @Test
    void createFail04() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(orderTable(1L, true), orderTable(2L, false)));

        given(orderTableDao.findAllByIdIn(any())).willReturn(
            Lists.newArrayList(orderTable(1L, true), orderTable(2L, false)));

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("create 성공")
    @Test
    void createSuccess() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Lists.newArrayList(orderTable(1L, true), orderTable(2L, true)));

        given(orderTableDao.findAllByIdIn(any())).willReturn(
            Lists.newArrayList(orderTable(1L, true), orderTable(2L, true)));

        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setId(1L);
        savedTableGroup.setOrderTables(Lists.newArrayList(orderTable(1L, true), orderTable(2L, true)));

        given(tableGroupDao.save(any())).willReturn(savedTableGroup);

        // when
        tableGroupService.create(tableGroup);

        // then
        verify(tableGroupDao).save(any());
        verify(orderTableDao, times(tableGroup.getOrderTables().size())).save(any());
    }

    @DisplayName("ungroup 실패 - COOKING, MEAL 상태인 order 존재")
    @Test
    void ungroupFail() {
        // given
        Long targetId = 1L;

        given(orderTableDao.findAllByTableGroupId(targetId)).willReturn(Collections.singletonList(orderTable(1L, true)));

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Collections.singletonList(1L),
                                                              Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(true);

        // when
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.ungroup(targetId));
    }

    @DisplayName("ungroup 성공")
    @Test
    void ungroupSuccess() {
        // given
        Long targetId = 1L;

        List<OrderTable> savedOrderTables = Lists.newArrayList(orderTable(1L, true), orderTable(2L, true));

        given(orderTableDao.findAllByTableGroupId(targetId)).willReturn(savedOrderTables);

        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Lists.newArrayList(1L, 2L),
                                                              Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
            .willReturn(false);

        // when
        tableGroupService.ungroup(targetId);

        // then
        verify(orderTableDao, times(savedOrderTables.size())).save(any());
        assertThat(savedOrderTables).allMatch(orderTable -> orderTable.getTableGroupId() == null);
    }

    private OrderTable orderTable(Long id, boolean empty) {
        OrderTable table = new OrderTable();
        table.setId(id);
        table.setEmpty(empty);
        return table;
    }
}

package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

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

    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup.Builder().id(1L)
                                                        .orderTables(Arrays.asList(
                                                                new OrderTable.Builder(0, true).id(1L).build(),
                                                                new OrderTable.Builder(0, true).id(2L).build(),
                                                                new OrderTable.Builder(0, true).id(3L).build()))
                                                        .build();

        given(orderTableDao.findAllByIdIn(anyList())).willReturn(tableGroup.getOrderTables());
        given(tableGroupDao.save(any(TableGroup.class))).willReturn(tableGroup);

        // when
        TableGroup created = tableGroupService.create(tableGroup);

        // then
        assertThat(created.getId()).isNotNull();

        // verity
        then(orderTableDao).should(times(1)).findAllByIdIn(anyList());
        then(tableGroupDao).should(times(1)).save(any(TableGroup.class));
        then(orderTableDao).should(times(3)).save(any(OrderTable.class));
    }

    @Test
    void create_throwException_ifIncorrectSize() {
        // given
        TableGroup tableGroup = new TableGroup.Builder().id(1L)
                                                        .orderTables(Collections.singletonList(
                                                                new OrderTable.Builder(0, true).id(1L).build()))
                                                        .build();

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);

        // verify
        then(orderTableDao).should(never()).findAllByIdIn(anyList());
    }

    @Test
    void ungroup() {
        // given
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(
                new OrderTable.Builder(4, false).id(1L).build(),
                new OrderTable.Builder(2, false).id(2L).build()));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then

        // verify
        then(orderTableDao).should(times(2)).save(any(OrderTable.class));
    }

    @Test
    void ungroup_throwException_ifOrderStatusInCookingAndMeal() {
        // given
        given(orderTableDao.findAllByTableGroupId(1L)).willReturn(Arrays.asList(
                new OrderTable.Builder(4, false).id(1L).build(),
                new OrderTable.Builder(2, false).id(2L).build()));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(1L, 2L),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when
        // then
        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
                .isInstanceOf(IllegalArgumentException.class);

        // then

        // verify
        then(orderTableDao).should(never()).save(any(OrderTable.class));
    }
}
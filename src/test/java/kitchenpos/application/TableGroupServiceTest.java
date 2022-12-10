package kitchenpos.application;

import static kitchenpos.domain.OrderTableTestFixture.orderTable;
import static kitchenpos.domain.TableGroupTestFixture.tableGroup;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 테이블 비즈니스 로직 테스트")
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
    @DisplayName("단체 테이블 등록시 테이블은 비어있을 수 없다.")
    void createTableGroupByEmptyTable() {
        // given
        TableGroup tableGroup = tableGroup(1L, Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 테이블 등록시 테이블은 2개 이상이어야 한다.")
    void createTableGroupByOrderTableMoreThanOne() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Collections.singletonList(orderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 테이블 등록시 모두 등록된 테이블이어야 한다.")
    void createTableGroupByNoneOrderedTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), orderTable2.getId())))
                .willReturn(Collections.singletonList(orderTable));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 테이블 등록시 모두 비어있는 테이블이어야 한다.")
    void createTableGroupByAllIsEmptyTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, false);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable, orderTable2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 테이블 등록시 이미 단체 테이블인 테이블은 등록 할 수 없다.")
    void createTableGroupByAlreadyGroup() {
        // given
        OrderTable orderTable = orderTable(1L, 1L, 0, true);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable, orderTable2));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    @DisplayName("단체 테이블로 등록한다.")
    void createTableGroup() {
        // given
        OrderTable orderTable = orderTable(1L, null, 0, true);
        OrderTable orderTable2 = orderTable(2L, null, 0, true);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);
        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable, orderTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(TableGroup.class)
        );
    }
    
    @Test
    @DisplayName("단체 테이블 해제시 조리 중이거나 식사중인 테이블은 해제할 수 없다.")
    void ungroupTableByOrderStatusCookingOrMeal() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, false);
        OrderTable orderTable2 = orderTable(2L, null, 3, false);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), orderTable2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        // when
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()));
    }

    @Test
    @DisplayName("단체 테이블을 해제한다.")
    void ungroupTable() {
        // given
        OrderTable orderTable = orderTable(1L, null, 4, false);
        OrderTable orderTable2 = orderTable(2L, null, 3, false);
        TableGroup tableGroup = tableGroup(1L, Arrays.asList(orderTable, orderTable2));
        given(orderTableDao.save(orderTable)).willReturn(orderTable);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);
        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable.getId(), orderTable2.getId()),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertAll(
                () -> assertThat(orderTable.getTableGroupId()).isNull(),
                () -> assertThat(orderTable2.getTableGroupId()).isNull()
        );
    }
}

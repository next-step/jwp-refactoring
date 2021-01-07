package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.exceptions.orderTable.OrderTableEntityNotFoundException;
import kitchenpos.domain.exceptions.tableGroup.InvalidTableGroupTryException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    private TableGroupService tableGroupService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    private OrderTable emptyOrderTable1;
    private OrderTable emptyOrderTable2;
    private OrderTable fullAndGroupedOrderTable1;
    private OrderTable fullAndGroupedOrderTable2;
    private List<OrderTable> emptyOrderTables;
    private List<OrderTable> fullAndGroupedOrderTables;

    @BeforeEach
    void setup() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        emptyOrderTable1 = new OrderTable();
        emptyOrderTable1.setId(1L);
        emptyOrderTable1.setEmpty(true);

        emptyOrderTable2 = new OrderTable();
        emptyOrderTable1.setId(2L);
        emptyOrderTable2.setEmpty(true);

        fullAndGroupedOrderTable1 = new OrderTable();
        fullAndGroupedOrderTable1.setId(4L);
        fullAndGroupedOrderTable1.setEmpty(false);
        fullAndGroupedOrderTable1.setTableGroupId(44L);
        fullAndGroupedOrderTable2 = new OrderTable();
        fullAndGroupedOrderTable1.setId(5L);
        fullAndGroupedOrderTable2.setEmpty(false);
        fullAndGroupedOrderTable2.setTableGroupId(44L);

        emptyOrderTables = Arrays.asList(emptyOrderTable1, emptyOrderTable2);
        fullAndGroupedOrderTables = Arrays.asList(fullAndGroupedOrderTable1, fullAndGroupedOrderTable2);
    }

    @DisplayName("2개 이하의 주문테이블로 단체 지정할 수 없다.")
    @ParameterizedTest
    @MethodSource("tableGroupFailWithEmptyTableResource")
    void createTableGroupFailWithEmptyTable(List<OrderTable> orderTables) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(InvalidTableGroupTryException.class)
                .hasMessage("2개 미만의 주문 테이블로 단체 지정할 수 없다.");
    }
    public static Stream<Arguments> tableGroupFailWithEmptyTableResource() {
        return Stream.of(
                Arguments.of(new ArrayList<>()),
                Arguments.of(Collections.singletonList(new OrderTable()))
        );
    }

    @DisplayName("존재하지 않는 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithNotExistTableGroups() {
        // given
        TableGroup tableGroupWithNotExistOrderTables = new TableGroup();
        tableGroupWithNotExistOrderTables.setOrderTables(emptyOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithNotExistOrderTables))
                .isInstanceOf(OrderTableEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블로 단체 지정할 수 없습니다.");
    }

    @DisplayName("비어있지 않은 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithFullOrderTables() {
        // given
        TableGroup tableGroupWithFullOrderTables = new TableGroup();
        tableGroupWithFullOrderTables.setOrderTables(fullAndGroupedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithFullOrderTables))
                .isInstanceOf(OrderTableEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블로 단체 지정할 수 없습니다.");
    }

    @DisplayName("이미 단체 지정된 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithAlreadyGrouped() {
        // given
        TableGroup tableGroupWithAlreadyGrouped = new TableGroup();
        tableGroupWithAlreadyGrouped.setOrderTables(fullAndGroupedOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithAlreadyGrouped))
                .isInstanceOf(OrderTableEntityNotFoundException.class)
                .hasMessage("존재하지 않는 주문 테이블로 단체 지정할 수 없습니다.");
    }

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        TableGroup tableGroupRequest = new TableGroup();
        TableGroup savedTableGroup = new TableGroup();
        savedTableGroup.setCreatedDate(LocalDateTime.now());
        savedTableGroup.setId(1L);
        tableGroupRequest.setOrderTables(emptyOrderTables);

        given(orderTableDao.findAllByIdIn(Arrays.asList(emptyOrderTable1.getId(), emptyOrderTable2.getId())))
                .willReturn(emptyOrderTables);
        given(tableGroupDao.save(tableGroupRequest)).willReturn(savedTableGroup);

        // when
        TableGroup tableGroup = tableGroupService.create(tableGroupRequest);

        // then
        assertThat(tableGroup.getCreatedDate()).isNotNull();
        emptyOrderTables.forEach(emptyOrderTable -> {
            assertThat(emptyOrderTable.getTableGroupId()).isEqualTo(savedTableGroup.getId());
            assertThat(emptyOrderTable.isEmpty()).isFalse();
        });
    }

    @DisplayName("주문 상태가 조리나 식사인 단체를 해제할 수 없다.")
    @Test
    void unGroupFailWithInvalidOrderStatus() {
        // given
        Long targetTableGroup = 1L;
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willThrow(new IllegalArgumentException());

        // when, then
        assertThatThrownBy(() -> tableGroupService.ungroup(targetTableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정을 해제할 수 있다.")
    @Test
    void unGroupTest() {
        // given
        Long targetTableGroup = 1L;
        given(orderTableDao.findAllByTableGroupId(targetTableGroup)).willReturn(fullAndGroupedOrderTables);
        fullAndGroupedOrderTables.forEach(fullAndGroupedOrderTable ->
                assertThat(fullAndGroupedOrderTable1.getTableGroupId()).isNotNull());

        // when
        tableGroupService.ungroup(1L);

        // then
        fullAndGroupedOrderTables.forEach(fullAndGroupedOrderTable ->
                assertThat(fullAndGroupedOrderTable1.getTableGroupId()).isNull());
    }
}

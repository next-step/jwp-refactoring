package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    private OrderTable fullOrderTable1;
    private OrderTable fullOrderTable2;
    private List<OrderTable> emptyOrderTables;
    private List<OrderTable> fullOrderTables;

    @BeforeEach
    void setup() {
        this.tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);

        emptyOrderTable1 = new OrderTable();
        emptyOrderTable1.setEmpty(true);

        emptyOrderTable2 = new OrderTable();
        emptyOrderTable2.setEmpty(true);

        fullOrderTable1 = new OrderTable();
        fullOrderTable1.setEmpty(false);
        fullOrderTable2 = new OrderTable();
        fullOrderTable2.setEmpty(false);

        emptyOrderTables = Arrays.asList(emptyOrderTable1, emptyOrderTable2);
        fullOrderTables = Arrays.asList(fullOrderTable1, fullOrderTable2);
    }

    @DisplayName("2개 이하의 주문테이블로 단체 지정할 수 없다.")
    @ParameterizedTest
    @MethodSource("tableGroupFailWithEmptyTableResource")
    void createTableGroupFailWithEmptyTable(List<OrderTable> orderTables) {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroup)).isInstanceOf(IllegalArgumentException.class);
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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("비어있지 않은 주문 테이블들로 단체 지정할 수 없다.")
    @Test
    void createTableGroupFailWithFullOrderTables() {
        // given
        TableGroup tableGroupWithFullOrderTables = new TableGroup();
        tableGroupWithFullOrderTables.setOrderTables(fullOrderTables);

        // when, then
        assertThatThrownBy(() -> tableGroupService.create(tableGroupWithFullOrderTables))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
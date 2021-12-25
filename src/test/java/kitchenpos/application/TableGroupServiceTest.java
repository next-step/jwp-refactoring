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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("단체 관련 기능")
public class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    TableGroupService tableGroupService;
    private TableGroup tableGroup;
    OrderTable orderTable1;
    OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = TableServiceTest.테이블_등록(1L, true);
        orderTable2 = TableServiceTest.테이블_등록(2L, true);
    }

    @Test
    @DisplayName("테이블 그룹을 등록한다.")
    void create() {
        tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        TableGroup createTableGroup = tableGroupService.create(tableGroup);
        assertThat(createTableGroup.getOrderTables()).hasSize(2);
    }

    @ParameterizedTest(name = "테이블 그룹을 등록할 때 테이블 갯수가 2보다 작으면 실패한다.")
    @MethodSource("tableGroupProvider")
    void createWithUnderTwoOrderTable(List<OrderTable> orderTables) {
        TableGroup tableGroup = 단체_지정(1L, orderTables);

        단체_지정_실패(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 DB에 없으면 실패한다.")
    void createWithDifferentOrderTable() {
        tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1));

        단체_지정_실패(tableGroup);
    }

    @Test
    @DisplayName("테이블 그룹으로 등록하려는 테이블이 비어있지 않거나 다른 그룹에 등록되어 있으면 실패한다.")
    void createWithNotEmptyOrderTableOrNonNullTableGroupId() {
        orderTable1.setEmpty(false);
        orderTable2.setTableGroupId(2L);
        tableGroup = 단체_지정(1L, Arrays.asList(orderTable1, orderTable2));
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(orderTable1, orderTable2));

        단체_지정_실패(tableGroup);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.")
    void ungroup() {
        create();

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
        given(orderTableDao.save(any())).willReturn(orderTable1);
        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTable1.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("단체 지정을 해제할 때 식사중이거나 조리중인 테이블이 있으면 실패한다.")
    void ungroupWithCookingOrMealOrderTable() {
        create();

        given(orderTableDao.findAllByTableGroupId(any())).willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.ungroup(tableGroup.getId());
        });
    }

    private TableGroup 단체_지정(long id, List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }

    private void 단체_지정_실패(TableGroup tableGroup) {
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroup);
        });
    }

    static Stream<List<OrderTable>> tableGroupProvider() {
        OrderTable orderTable1 = TableServiceTest.테이블_등록(1L, true);
        return Stream.of(
                Arrays.asList(),
                Arrays.asList(orderTable1)
        );
    }
}

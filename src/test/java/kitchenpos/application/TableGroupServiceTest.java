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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

@ExtendWith(value = MockitoExtension.class)
@DisplayName("단체 지정에 대한 비즈니스 로직")
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private OrderTable orderTable1;

    private OrderTable orderTable2;

    @BeforeEach
    void setUp() {
        orderTable1 = new OrderTable();
        orderTable1.setId(1L);
        orderTable1.setEmpty(true);
        orderTable1.setTableGroupId(null);

        orderTable2 = new OrderTable();
        orderTable2.setId(2L);
        orderTable2.setEmpty(true);
        orderTable2.setTableGroupId(null);
    }

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(tableGroupDao.save(tableGroup)).willReturn(tableGroup);

        // when
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual.getId()).isEqualTo(tableGroup.getId());
        assertThat(actual.getOrderTables()).isEqualTo(tableGroup.getOrderTables());
        assertThat(actual.getCreatedDate()).isNotNull();
    }

    @DisplayName("주문 테이블이 2개 보다 작으면 생성할 수 없다.")
    @Test
    void tableSize() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @DisplayName("주문 테이블이 주문 등록 가능 상태이면 생성할 수 없다.")
    @Test
    void notEmptyTable() {
        // given
        orderTable1.setEmpty(false);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable1, orderTable2));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정이 되어 있으면 생성할 수 없다.")
    @Test
    void already() {
        // given
        orderTable1.setTableGroupId(1L);
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));

        given(orderTableDao.findAllByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())))
                .willReturn(Arrays.asList(orderTable1, orderTable2));

        // when / then
        assertThrows(IllegalArgumentException.class, () -> tableGroupService.create(tableGroup));
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void ungroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        // when
        tableGroupService.ungroup(tableGroup.getId());

        // then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("주문 테이블이 조리 중이거나 식사 중일때는 단체 지정을 삭제할 수 없다.")
    @Test
    void isCookingOrMeal() {
        // given
        boolean isCookingOrMeal = true;
        TableGroup tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setOrderTables(Arrays.asList(orderTable1, orderTable2));
        orderTable1.setTableGroupId(1L);
        orderTable2.setTableGroupId(1L);

        given(orderTableDao.findAllByTableGroupId(tableGroup.getId()))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(isCookingOrMeal);

        // when / then
        assertThrows(IllegalArgumentException.class,
                () -> tableGroupService.ungroup(tableGroup.getId()));

    }
}

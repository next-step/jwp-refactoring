package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import kitchenpos.application.TableGroupService;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("테이블 그룹 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    private final TableGroup tableGroup = new TableGroup();
    private final OrderTable orderTable_1 = new OrderTable();
    private final OrderTable orderTable_2 = new OrderTable();

    @BeforeEach
    void setUp() {
        orderTable_1.setId(1L);
        orderTable_1.setEmpty(true);
        orderTable_2.setId(2L);
        orderTable_2.setEmpty(true);
    }

    @Test
    @DisplayName("단체를 지정할 수 있다.")
    void create() {
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        assertNull(tableGroup.getCreatedDate());

        when(orderTableDao.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(tableGroupDao.save(any(TableGroup.class)))
            .thenReturn(tableGroup);

        TableGroup saved = tableGroupService.create(this.tableGroup);

        assertAll(() -> {
            assertNotNull(saved.getCreatedDate());
            assertThat(saved.getOrderTables())
                .extracting(OrderTable::getId)
                .containsExactly(1L, 2L);
        });
    }

    @Test
    @DisplayName("단체 지정은 테이블이 2개 이상이어야 한다.")
    void createValidateTableSize() {
        assertNull(tableGroup.getOrderTables());

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);

        tableGroup.setOrderTables(Arrays.asList(orderTable_1));
        assertThat(tableGroup.getOrderTables().size()).isEqualTo(1);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("등록되어 있는 테이블만 단체 지정이 가능하다.")
    void createValidateRegisteredTable() {
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        when(orderTableDao.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("빈 테이블만 단체 지정이 가능하다.")
    void createValidateEmptyTable() {
        orderTable_1.setEmpty(false);
        tableGroup.setOrderTables(Arrays.asList(orderTable_1, orderTable_2));

        when(orderTableDao.findAllByIdIn(any()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("단체 지정을 해제한다.(삭제한다)")
    void upgroup() {
        orderTable_1.setTableGroupId(1L);
        orderTable_2.setTableGroupId(1L);

        assertAll(() -> {
            assertNotNull(orderTable_1.getTableGroupId());
            assertNotNull(orderTable_2.getTableGroupId());
        });

        when(orderTableDao.findAllByTableGroupId(anyLong()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(false);
        when(orderTableDao.save(orderTable_1))
            .thenReturn(orderTable_1);
        when(orderTableDao.save(orderTable_2))
            .thenReturn(orderTable_2);

        tableGroupService.ungroup(1L);

        assertAll(() -> {
            assertNull(orderTable_1.getTableGroupId());
            assertNull(orderTable_2.getTableGroupId());
        });
    }

    @Test
    @DisplayName("주문 진행중인 테이블이 있으면 단체 지정을 해제할 수 없다.")
    void upgroupValidate() {
        orderTable_1.setTableGroupId(1L);
        orderTable_2.setTableGroupId(1L);

        assertAll(() -> {
            assertNotNull(orderTable_1.getTableGroupId());
            assertNotNull(orderTable_2.getTableGroupId());
        });

        when(orderTableDao.findAllByTableGroupId(anyLong()))
            .thenReturn(Arrays.asList(orderTable_1, orderTable_2));
        when(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList()))
            .thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(1L))
            .isInstanceOf(IllegalArgumentException.class);
    }
}
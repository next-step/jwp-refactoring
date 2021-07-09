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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    private OrderTable table;
    private OrderTable table2;
    private OrderTable table3;
    private TableGroup tableGroup;
    private TableGroup tableGroup2;

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;

    @InjectMocks
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        table = new OrderTable();
        table.setId(1L);
        table.setNumberOfGuests(0);
        table.setEmpty(true);

        table2 = new OrderTable();
        table2.setId(2L);
        table2.setNumberOfGuests(0);
        table2.setEmpty(true);

        table3 = new OrderTable();
        table3.setId(3L);
        table3.setNumberOfGuests(0);
        table3.setEmpty(false);

        tableGroup = new TableGroup();
        tableGroup.setId(1L);
        tableGroup.setCreatedDate(LocalDateTime.now());

        tableGroup2 = new TableGroup();
        tableGroup2.setId(2L);
        tableGroup2.setCreatedDate(LocalDateTime.now());
    }

    @DisplayName("테이블 그룹을 생성한다")
    @Test
    void create() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(table, table2));
        given(tableGroupDao.save(any())).willReturn(tableGroup);

        // when
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(table);
        orderTables.add(table2);
        tableGroup.setOrderTables(orderTables);
        TableGroup actual = tableGroupService.create(tableGroup);

        // then
        assertThat(actual).isEqualTo(tableGroup);
    }
    @DisplayName("테이블그룹생성 - 테이블 그룹은 최소 두 개 이상의 테이블로 구성되어야 한다")
    @Test
    void create_illegalCount() {
        // given when
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(table);
        tableGroup.setOrderTables(orderTables);
        tableGroup2.setOrderTables(Collections.emptyList());

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup2));
    }

    @DisplayName("테이블그룹생성 - 테이블 그룹에 속한 테이블은 서로 달라야 한다")
    @Test
    void create_containsSameTable() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(table));

        // when
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(table);
        orderTables.add(table2);
        tableGroup.setOrderTables(orderTables);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @DisplayName("테이블그룹생성 - 테이블 그룹에 속한 테이블은 비어있지 않아야 한다")
    @Test
    void create_containsEmptyTable() {
        // given
        given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(table, table3));

        // when
        List<OrderTable> orderTables = new ArrayList<>();
        orderTables.add(table);
        orderTables.add(table3);
        tableGroup.setOrderTables(orderTables);

        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }
}
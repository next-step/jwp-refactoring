package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class TableGroupServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private TableGroupDao tableGroupDao;
    @InjectMocks
    private TableGroupService tableGroupService;

    @DisplayName("주문 테이블들을 단체 지정할 수 있다.")
    @Test
    void createTableGroup() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Arrays.asList(empty_orderTable1, empty_orderTable2));
        given(tableGroupDao.save(any())).willReturn(tableGroup);
        given(orderTableDao.save(orderTable1)).willReturn(orderTable1);
        given(orderTableDao.save(orderTable2)).willReturn(orderTable2);

        TableGroup result = tableGroupService.create(tableGroup);

        assertThat(result.getCreatedDate()).isNotNull();
        assertThat(result.getOrderTables().get(0).getTableGroupId()).isNotNull();
        assertThat(result.getOrderTables().get(0).isEmpty()).isFalse();
        assertThat(result.getOrderTables().get(1).getTableGroupId()).isNotNull();
        assertThat(result.getOrderTables().get(1).isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 갯수가 2개 미만일 경우 지정할 수 없다.")
    @Test
    void createTableGroupException1() {
        assertThatThrownBy(() -> tableGroupService.create(tableGroup_orderTables_추가(tableGroup, Collections.singletonList(orderTable1))))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블들의 수와 저장되어 있는 주문 테이블의 수가 일치하지 않으면 지정할 수 없다.")
    @Test
    void createTableGroupException2() {
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(Collections.singletonList(empty_orderTable1));

        assertThatThrownBy(() -> tableGroupService.create(tableGroup))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 빈 테이블이 아닌 경우 지정할 수 없다.")
    @Test
    void createTableGroupException3() {
        List<OrderTable> orderTables = Arrays.asList(orderTable1, empty_orderTable2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup_orderTables_추가(tableGroup, orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 이미 단체 지정되어 있는 경우 지정할 수 없다.")
    @Test
    void createTableGroupException4() {
        List<OrderTable> orderTables = Arrays.asList(orderTable_groupId_추가(empty_orderTable1, tableGroup.getId(), true), empty_orderTable2);
        given(orderTableDao.findAllByIdIn(anyList())).willReturn(orderTables);

        assertThatThrownBy(() -> tableGroupService.create(tableGroup_orderTables_추가(tableGroup, orderTables)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("들어간 주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.")
    @Test
    void ungroupTableGroupException() {
        given(orderTableDao.findAllByTableGroupId(anyLong())).willReturn(그룹으로_묶여있는_orderTables);
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(anyList(), anyList())).willReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

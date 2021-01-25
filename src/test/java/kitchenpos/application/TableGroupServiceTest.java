package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@SpringBootTest
class TableGroupServiceTest {

    private List<OrderTable> orderTables;
    private OrderTable 테이블_1번;
    private OrderTable 테이블_2번;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableService tableService;

    @MockBean
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        테이블_1번 = 테이블을_생성한다(0, true);
        테이블_2번 = 테이블을_생성한다(0, true);
        orderTables = new ArrayList<>();
        orderTables.add(테이블_1번);
        orderTables.add(테이블_2번);
    }

    @DisplayName("테이블 그룹을 생성한다 : 각 테이블들의 테이블 그룹 id를 등록하고 상태를 비어있지않음으로 변경한다")
    @Test
    void create() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroup(orderTables));

        List<OrderTable> orderTables = tableGroup.getOrderTables();

        orderTables.forEach(group -> {
            assertThat(group.getTableGroup().getId()).isEqualTo(tableGroup.getId());
            assertThat(group.isEmpty()).isEqualTo(false);
        });
    }

    @DisplayName("테이블 그룹을 생성한다 : 테이블 사이즈가 2미만이면 익셉션 발생")
    @Test
    void createException() {
        assertThatThrownBy(() -> 테이블_그룹을_생성한다(new TableGroup()))
                .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("각 주문 테이블의 테이블 그룹 id를 지운다")
    @Test
    void ungroup() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroup(orderTables));
        List<Long> orderTableIds = tableGroup.getOrderTables().stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList());

        테이블_그룹을_비운다(tableGroup.getId());

        orderTableIds.forEach(id -> {
            assertThat(tableGroupService.findOrderTableById(id).getTableGroup()).isNull();
        });
    }

    @DisplayName("테이블 그룹을 제거한다 : OrderStatus가 COOKING, MEAL이면 익셉션 발생")
    @Test
    void ungroupException() {
        TableGroup tableGroup = 테이블_그룹을_생성한다(new TableGroup(orderTables));

        when(orderRepository.existsByOrderTableInAndOrderStatusIn(anyList(), anyList())).thenReturn(true);

        assertThatThrownBy(() -> tableGroupService.ungroup(tableGroup.getId()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    private OrderTable 테이블을_생성한다(int numberOfGuest, boolean empty) {
        return tableService.create(new OrderTable(numberOfGuest, empty));
    }

    private void 테이블_그룹을_비운다(Long id) {
        tableGroupService.ungroup(id);
    }

    private TableGroup 테이블_그룹을_생성한다(TableGroup tableGroup) {
        return tableGroupService.create(tableGroup);
    }

}
package kitchenpos.application;

import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TableGroupServiceTest extends BaseTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Autowired
    private OrderTableDao orderTableDao;

    @DisplayName("단체 지정")
    @Test
    void create() {
        assertThat(createTableGroup()).isNotNull();
    }

    @DisplayName("주문 테이블이 없는 경우")
    @Test
    void validateEmptyOrderTables() {
        TableGroup expected = tableGroupDao.findById(1L).get();
        expected.setOrderTables(new ArrayList<>());

        assertThatThrownBy(() -> {
            tableGroupService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 검증이 안될 경우")
    @Test
    void validateValifyOrderTables() {
        TableGroup expected = tableGroupDao.findById(1L).get();
        List<OrderTable> convertOrderTables = orderTableDao.findAll().stream()
                .map(it -> updateId(it, 1L))
                .collect(Collectors.toList());

        expected.setOrderTables(convertOrderTables);

        assertThatThrownBy(() -> {
            tableGroupService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() {
        TableGroup tableGroup = createTableGroup();

        tableGroupService.ungroup(tableGroup.getId());
        
        assertThat(orderTableDao.findAllByTableGroupId(tableGroup.getId())).isEmpty();

    }

    private OrderTable updateId(OrderTable orderTable, Long id) {
        orderTable.setId(id);
        return orderTable;
    }

    private TableGroup createTableGroup() {
        TableGroup expected = tableGroupDao.findById(1L).get();
        expected.setOrderTables(orderTableDao.findAll());

        return tableGroupService.create(expected);
    }
}
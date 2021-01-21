package kitchenpos.order.application;


import kitchenpos.order.domain.OrderTable;
import kitchenpos.order.domain.OrderTableRepository;
import kitchenpos.order.domain.TableGroupRepository;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class TableGroupServiceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @DisplayName("단체 지정")
    @Test
    void create() {
        assertThat(createTableGroup()).isNotNull();
    }

    @DisplayName("주문 테이블이 없는 경우")
    @Test
    void validateEmptyOrderTables() {
        TableGroupRequest request = new TableGroupRequest(new ArrayList<>());

        assertThatThrownBy(() -> {
            tableGroupService.create(request);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 검증이 안될 경우")
    @Test
    void validateValifyOrderTables() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        TableGroupRequest expected = new TableGroupRequest(Arrays.asList(new OrderTableRequest(orderTable.getId()), new OrderTableRequest(2L)));

        assertThatThrownBy(() -> {
            tableGroupService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이미 그룹 지정이 된 주문 테이블")
    @Test
    void validateAlreadyGroup() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        TableGroupRequest expected = new TableGroupRequest(Arrays.asList(new OrderTableRequest(orderTable.getId()), new OrderTableRequest(orderTable2.getId())));
        tableGroupService.create(expected);

        assertThatThrownBy(() -> {
            tableGroupService.create(expected);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() {
        TableGroupResponse tableGroup = createTableGroup();

        tableGroupService.ungroup(tableGroup.getId());

        assertThat(orderTableRepository.findAllByTableGroupId(tableGroup.getId())).isEmpty();
    }

    private TableGroupResponse createTableGroup() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(0, true));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        TableGroupRequest expected = new TableGroupRequest(Arrays.asList(new OrderTableRequest(orderTable.getId()), new OrderTableRequest(orderTable2.getId())));
        return tableGroupService.create(expected);
    }
}
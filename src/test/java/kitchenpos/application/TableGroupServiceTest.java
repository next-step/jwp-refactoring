package kitchenpos.application;

import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import kitchenpos.repository.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TableGroupServiceTest extends AcceptanceTest {
    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable 빈테이블_1;
    private OrderTable 빈테이블_2;
    private OrderTable 비어있지_않은_테이블_1;
    private TableGroup 그룹_테이블_1;
    private TableGroup 그룹_테이블_2;
    private OrderTable 그룹_지정된_테이블_1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        빈테이블_1 = new OrderTable(1L, null,0, true);
        빈테이블_2 = new OrderTable(2L, null,0, true);
        비어있지_않은_테이블_1 = new OrderTable(9L, null,2, false);

        그룹_테이블_1 = new TableGroup(1L, LocalDateTime.of(2020, 1, 20, 03, 30));
        그룹_테이블_2 = new TableGroup(2L, LocalDateTime.of(2020, 1, 20, 03, 30));

        그룹_지정된_테이블_1 = new OrderTable(10L, 그룹_테이블_1, 0, false);
    }

    @DisplayName("단체 지정을 생성할 수 있다.")
    @Test
    void createTableGroup() {
        // given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈테이블_1.getId());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(빈테이블_2.getId());

        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1, orderTableRequest2));

        // when
        TableGroupResponse response = tableGroupService.create(tableGroupRequest);

        // then
        List<Long> tableGroupIds = response.getOrderTables().stream()
                .map(OrderTableResponse::getTableGroupId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        assertThat(tableGroupIds.size()).isEqualTo(2);

        assertThat(response.getOrderTables().stream()
                .filter(OrderTableResponse::isEmpty)
                .count()).isEqualTo(0);

        assertThat(tableGroupIds.stream()
                .distinct().count()).isEqualTo(1);
    }

    @DisplayName("주문 테이블이 2개 이상 있어야 한다.")
    @Test
    void requireOrderTableMoreThanTwo() {
        // given
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(빈테이블_1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest1));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageMatching("2개 미만의 테이블은 그룹 지정을 할 수 없습니다.");
    }

    @DisplayName("주문 테이블 상태가 비어있음이 아니면 생성할 수 없다.")
    @Test
    void notEmptyOrderTableStatus() {
        // given
        OrderTableRequest orderTableRequestEmpty = new OrderTableRequest(빈테이블_1.getId());
        OrderTableRequest orderTableRequestNotEmpty = new OrderTableRequest(비어있지_않은_테이블_1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequestEmpty, orderTableRequestNotEmpty));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageMatching("비어있지 않거나 이미 그룹 지정된 테이블은 그룹 지정할 수 없습니다.");
    }

    @DisplayName("단체 지정이 되어 있으면 생성할 수 없다.")
    @Test
    void alreadyExistTableGroupId() {
        // given
        OrderTableRequest orderTableRequest = new OrderTableRequest(빈테이블_1.getId());
        OrderTableRequest orderTableRequestHaveGroup = new OrderTableRequest(그룹_지정된_테이블_1.getId());
        TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequest, orderTableRequestHaveGroup));

        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.create(tableGroupRequest);
        }).withMessageMatching("비어있지 않거나 이미 그룹 지정된 테이블은 그룹 지정할 수 없습니다.");
    }

    @DisplayName("단체 지정을 삭제할 수 있다.")
    @Test
    void unGroup() {
        // given
        Long tableGroupId = 그룹_테이블_2.getId();
        List<OrderTable> orderTablesWithGroup = orderTableRepository.findAllByTableGroup(그룹_테이블_2);
        assertThat(orderTablesWithGroup.size()).isEqualTo(1);

        // when
        tableGroupService.unGroup(tableGroupId);

        // then
        List<OrderTable> orderTablesAfterUngroup = orderTableRepository.findAllByTableGroup(그룹_테이블_2);
        assertThat(orderTablesAfterUngroup.size()).isEqualTo(0);
    }

    @DisplayName("주문 테이블이 조리중 이거나 식사일때는 단체 지정을 해제할 수 없다.")
    @Test
    void ungroupFailWhenCookingOrMeal() {
        // when & then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> {
            tableGroupService.unGroup(그룹_지정된_테이블_1.getTableGroupId());
        }).withMessageMatching("주문 상태가 조리중이거나 식사중인 테이블의 그룹 지정은 해지할 수 없습니다.");
    }
}

package kitchenpos.tablegroup;


import kitchenpos.common.ServiceTest;
import kitchenpos.order.OrderTableServiceTestSupport;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableIdRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;

class TableGroupServiceTest extends ServiceTest {

    @Autowired
    private TableGroupService service;
    @Autowired
    private OrderTableServiceTestSupport orderTableServiceTestSupport;

    private OrderTable 빈테이블1;
    private OrderTable 빈테이블2;

    @BeforeEach
    public void setUp(@Autowired OrderTableRepository orderTableRepository) {
        빈테이블1 = orderTableRepository.save(new OrderTable(0, true));
        빈테이블2 = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        TableGroupResponse response = createTableGroup(빈테이블1, 빈테이블2);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getCreatedDate()).isNotNull(),
                () -> assertThat(response.getOrderTables()).element(0).satisfies(this::assertGroupedTable),
                () ->  assertThat(response.getOrderTables()).element(1).satisfies(this::assertGroupedTable)
        );
    }

    @DisplayName("테이블 없이 단체 지정을 생성한다.")
    @Test
    void createWithEmpty() {
        TableGroupRequest request = new TableGroupRequest(Collections.emptyList());

        assertThatThrownBy(() -> {
            service.create(request);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("단체 지정할 테이블이 없습니다.");
    }

    @DisplayName("존재하지 않는 테이블을 포함하여 단체 지정을 생성한다.")
    @Test
    void createWithNotFoundTable() {
        Long 존재하지_않는_테이블_id = Long.MAX_VALUE;
        TableGroupRequest request = new TableGroupRequest(Arrays.asList(
                new OrderTableIdRequest(빈테이블1.getId()),
                new OrderTableIdRequest(존재하지_않는_테이블_id)
        ));

        assertThatThrownBy(() -> {
            service.create(request);
        }).isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("존재하지 않는 테이블이 있습니다.");
    }

    private TableGroupResponse createTableGroup(OrderTable... orderTables) {
        TableGroupRequest request = new TableGroupRequest(Arrays.stream(orderTables)
                                                                .map(it -> new OrderTableIdRequest(it.getId()))
                                                                .collect(Collectors.toList()));
        TableGroupResponse response = service.create(request);
        return response;
    }

    private void assertGroupedTable(OrderTableResponse it) {
        assertThat(it.getTableGroupId()).isNotNull();
        assertThat(it.isEmpty()).isFalse();
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroup() {
        TableGroupResponse tableGroup = createTableGroup(빈테이블1, 빈테이블2);

        assertThatNoException().isThrownBy(() -> {
            service.ungroup(tableGroup.getId());
        });
    }

    @DisplayName("단체 지정을 해지한다.")
    @Test
    void ungroupWithNotFoundTableGroup() {
        Long 존재하지_않는_단체_지정_id = Long.MAX_VALUE;

        assertThatThrownBy(() -> {
            service.ungroup(존재하지_않는_단체_지정_id);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("계산 완료가 되지 않는 주문이 있는 단체 지정를 해지한다.")
    @Test
    void ungroupWithHasNotCompletedOrder() {
        TableGroupResponse tableGroup = createTableGroup(빈테이블1, 빈테이블2);
        orderTableServiceTestSupport.강정치킨_주문하기(빈테이블1);

        assertThatThrownBy(() -> {
            service.ungroup(tableGroup.getId());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

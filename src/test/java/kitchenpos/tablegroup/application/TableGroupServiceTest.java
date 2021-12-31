package kitchenpos.tablegroup.application;

import kitchenpos.common.exception.MinimumOrderTableNumberException;
import kitchenpos.common.exception.NotEmptyOrderTableStatusException;
import kitchenpos.common.exception.OrderStatusNotCompletedException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.ordertable.application.OrderTableServiceTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import kitchenpos.tablegroup.domain.TableGroupValidator;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


@DisplayName("테이블 그룹 서비스 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
    @Mock
    private TableGroupRepository tableGroupRepository;

    @Mock
    private TableGroupValidator tableGroupValidator;

    private TableGroupService tableGroupService;

    private TableGroup 단체테이블1번;
    private OrderTable 주문테이블1번;
    private OrderTable 주문테이블2번;
    private TableGroupRequest 단체테이블요청;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(tableGroupRepository, tableGroupValidator);

        주문테이블1번 = new OrderTable(null, 0);
        주문테이블2번 = new OrderTable(null, 0);
        단체테이블1번 = new TableGroup(1L, Lists.newArrayList(주문테이블1번, 주문테이블2번));

        단체테이블요청 = new TableGroupRequest(Lists.newArrayList(주문테이블1번, 주문테이블2번));
    }

    @DisplayName("상품 등록 테스트")
    @Test
    void createTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(단체테이블1번);

        // when
        final TableGroupResponse createdTableGroup = tableGroupService.create(단체테이블요청);

        // then
        assertAll(
                () -> assertThat(createdTableGroup.getOrderTables().get(0)).isEqualTo(주문테이블1번),
                () -> assertThat(createdTableGroup.getOrderTables().get(1)).isEqualTo(주문테이블2번)
        );
    }

    @DisplayName("테이블 그룹에 주문 테이블들을 등록한다.")
    @Test
    void createOrderTableInTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(단체테이블1번);

        // when
        final TableGroupResponse createdTableGroup = tableGroupService.create(단체테이블요청);

        // then
        assertAll(
                () -> assertThat(createdTableGroup.getOrderTables().get(0)).isEqualTo(주문테이블1번),
                () -> assertThat(createdTableGroup.getOrderTables().get(1)).isEqualTo(주문테이블2번)
        );
    }

    @DisplayName("주문 테이블에 테이블 그룹을 해제한다.")
    @Test
    void ungroupTableGroupTest() {
        when(tableGroupRepository.save(any())).thenReturn(단체테이블1번);
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.ofNullable(단체테이블1번));

        // given
        TableGroupResponse createdTableGroup = tableGroupService.create(단체테이블요청);

        // when
        tableGroupService.ungroup(createdTableGroup.getId());

        // then
        assertThat(주문테이블1번.getTableGroupId()).isEqualTo(null);
    }
}
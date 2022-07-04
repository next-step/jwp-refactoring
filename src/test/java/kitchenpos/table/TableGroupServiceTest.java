package kitchenpos.table;

import static kitchenpos.table.TableAcceptanceTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.dao.TableGroupRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {

    @InjectMocks
    TableGroupService tableGroupService;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @Mock
    TableGroupRepository tableGroupRepository;

    TableGroup 단체손님;
    OrderTable 주문테이블1;
    OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        주문테이블1 = new OrderTable(2, 빈자리);
        주문테이블2 = new OrderTable(2, 빈자리);

        단체손님 = new TableGroup(Arrays.asList(new OrderTable(2, 빈자리), new OrderTable(2, 빈자리)));
    }

    @Test
    @DisplayName("단체를 지정하여 저장한다")
    void create() {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(tableGroupRepository.save(any())).willReturn(단체손님);

        // when
        TableGroupResponse actual = tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)));

        // then
        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("단체 지정시 유효한 주문 테이블 정보를 지정해야 한다")
    void create_orderTableGroupError() {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).withMessageContaining("존재하지 않는 주문테이블입니다.");
    }

    @Test
    @DisplayName("단체 지정시 최소 2팀 이상 지정해야 한다")
    void create_singleTableGroupError() {
        // given
        given(orderTableRepository.findAllByIdIn(any())).willReturn(Collections.singletonList(주문테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Collections.singletonList(주문테이블1.getId())))
        ).withMessageContaining("주문 테이블은 2개 이상 존재하여야 합니다.");
    }

    @Test
    @DisplayName("단체 지정시 주문 테이블은 이미 주문이 진행중인 테이블이거나 이미 단체 테이블이면 안 된다")
    void create_emptyTableGroupError() {
        // given
        주문테이블1.setEmpty(false);
        given(orderTableRepository.findAllByIdIn(any())).willReturn(단체손님.getOrderTables());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.create(new TableGroupRequest(Arrays.asList(1L, 2L)))
        ).withMessageContaining("주문 진행중이거나 이미 단체 테이블인 경우 단체로 지정할 수 없습니다.");
    }

    @Test
    @DisplayName("지정한 단체를 해제한다")
    void ungroup() {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);

        // when
        tableGroupService.ungroup(1L);

        // then
        assertAll(
                () -> assertThat(주문테이블1.getTableGroup()).isNull(),
                () -> assertThat(주문테이블2.getTableGroup()).isNull()
        );
    }

    @Test
    @DisplayName("지정한 단체를 해제시 주문 상태가 계산 완료여야 한다")
    void ungroup_completionError() {
        // given
        given(orderTableRepository.findAllByTableGroupId(any())).willReturn(Arrays.asList(주문테이블1, 주문테이블2));
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> tableGroupService.ungroup(1L)
        );
    }
}

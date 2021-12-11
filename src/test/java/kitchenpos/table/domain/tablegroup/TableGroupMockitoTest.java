package kitchenpos.table.domain.tablegroup;

import kitchenpos.order.domain.order.OrderRepository;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.table.OrderTableRepository;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static kitchenpos.fixture.OrderTableDomainFixture.양식_테이블;
import static kitchenpos.fixture.OrderTableDomainFixture.한식_테이블;
import static kitchenpos.fixture.TableGroupDomainFixture.한식_양식_합석;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 그룹 관리")
class TableGroupMockitoTest {

    private OrderRepository orderRepository;
    private OrderTableRepository orderTableRepository;
    private TableGroupRepository tableGroupRepository;
    private TableGroupService tableGroupService;


    private void setUpMock() {
        orderRepository = mock(OrderRepository.class);
        orderTableRepository = mock(OrderTableRepository.class);
        tableGroupRepository = mock(TableGroupRepository.class);
        tableGroupService = new TableGroupService(orderRepository, orderTableRepository, tableGroupRepository);
    }

    @Test
    @DisplayName("테이블 그룹 성공")
    void createTableGroupSuccess() {
        // given
        setUpMock();
        when(orderTableRepository.findAllById(anyList())).thenReturn(Lists.newArrayList(한식_테이블, 양식_테이블));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(한식_양식_합석);

        // when
        final TableGroupResponse actual = tableGroupService.saveTableGroup(TableGroupRequest.from(anyList()));

        assertThat(actual).isNotNull();
    }

    @Test
    @DisplayName("테이블 그룹 해제 성공")
    void tableUnGroupSuccess() {
        /// given
        setUpMock();
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(한식_양식_합석));
        when(tableGroupRepository.save(any(TableGroup.class))).thenReturn(한식_양식_합석);

        // when
        tableGroupService.ungroup(anyLong());
    }

}

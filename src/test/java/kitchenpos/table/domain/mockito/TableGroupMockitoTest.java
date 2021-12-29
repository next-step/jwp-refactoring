package kitchenpos.table.domain.mockito;

import static kitchenpos.fixture.OrderTableFixture.*;
import static kitchenpos.fixture.TableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.jupiter.api.*;

import kitchenpos.table.application.*;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.*;
import kitchenpos.table.repository.*;

@DisplayName("단체 지정 관련(Mockito)")
class TableGroupMockitoTest {
    private OrderTableRepository orderTableRepository;
    private TableGroupRepository tableGroupRepository;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderTableRepository = mock(OrderTableRepository.class);
        tableGroupRepository = mock(TableGroupRepository.class);
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository);
    }

    @DisplayName("단체 지정 생성하기")
    @Test
    void createTest() {
        OrderTable orderTable1 = OrderTable.of(주문테이블_4명.getNumberOfGuests(), true);
        OrderTable orderTable2 = OrderTable.of(주문테이블_6명.getNumberOfGuests(), true);
        TableGroup tableGroup = TableGroup.create();

        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(orderTable1, orderTable2));
        when(tableGroupRepository.save(any())).thenReturn(tableGroup);

        assertThat(
            tableGroupService.saveTableGroup(TableGroupRequest.from(Arrays.asList(1L, 2L)))
        ).isInstanceOf(TableGroupResponse.class);
    }

    @DisplayName("단체 지정 생성시 주문 테이블 개수 1이하면 실패함")
    @Test
    void exceptionTest1() {
        assertThatThrownBy(() ->
            tableGroupService.saveTableGroup(TableGroupRequest.from(Arrays.asList(1L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문테이블의 크기와 저장된 주문테이블의 크기가 다르면 실패함")
    @Test
    void exceptionTest2() {
        when(orderTableRepository.findAllById(any())).thenReturn(Arrays.asList(주문테이블_4명, 주문테이블_6명));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹_주문테이불_4명과_주문테이블_6명);

        assertThatThrownBy(() ->
            tableGroupService.saveTableGroup(TableGroupRequest.from(Arrays.asList(1L, 2L, 3L)))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("단체 지정 삭제하기")
    @Test
    void deleteTest() {
        when(tableGroupRepository.findById(anyLong())).thenReturn(Optional.of(테이블그룹_주문테이불_4명과_주문테이블_6명));
        when(tableGroupRepository.save(any())).thenReturn(테이블그룹_주문테이불_4명과_주문테이블_6명);
        tableGroupService.ungroup(anyLong());
    }
}

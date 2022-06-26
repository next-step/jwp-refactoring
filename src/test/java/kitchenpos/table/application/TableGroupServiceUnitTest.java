package kitchenpos.table.application;

import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("단체 지정 Service 단위 테스트 - Stub")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceUnitTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    @Mock
    private OrderRepository orderRepository;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderTableRepository, tableGroupRepository, orderRepository);
    }

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        long generateTableGroupId = 1;
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L)));
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        kitchenpos.table.domain.OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);

        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        doAnswer(invocation -> 테이블_그룹_만들기(generateTableGroupId, Arrays.asList(emptyTable1, emptyTable2)))
                .when(tableGroupRepository).save(any());

        //when
        TableGroupResponse result = tableGroupService.create(request, LocalDateTime.now());

        //then
        List<OrderTableResponse> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).getEmpty()).isFalse();
        assertThat(orderTables.get(1).getEmpty()).isFalse();
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_less_then_two() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        given(orderTableRepository.findAllById(Arrays.asList(1L))).willReturn(Arrays.asList(emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }

    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }

    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L)));
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable orderTable2 = 테이블_만들기(2L, 3, false);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, orderTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }

    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));
        given(orderTableRepository.findAllById(any())).willReturn(Arrays.asList(emptyTable1, emptyTable2));
        given(tableGroupRepository.existsByOrderTables(any())).willThrow(IllegalArgumentException.class);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }
}

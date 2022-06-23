package kitchenpos.application;

import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.table.application.TableGroupService;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroupRepository;
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
class TableGroupServiceTest {

    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableGroupRepository tableGroupRepository;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        tableGroupService = new TableGroupService(orderDao, orderTableDao, orderTableRepository, tableGroupRepository);
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
        TableGroupResponse result = tableGroupService.create(request);

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
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        given(orderTableRepository.findAllById(Arrays.asList(1L))).willReturn(Arrays.asList(emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        kitchenpos.table.domain.OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(1L), 테이블_요청_만들기(2L)));
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        kitchenpos.table.domain.OrderTable orderTable2 = 테이블_만들기(2L, 3, false);
        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, orderTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        kitchenpos.table.domain.OrderTable emptyTable1 = 테이블_만들기(1L, 0, true);
        kitchenpos.table.domain.OrderTable emptyTable2 = 테이블_만들기(2L, 0, true);
        emptyTable2.setTableGroup(테이블_그룹_만들기(1L, Arrays.asList(emptyTable1, emptyTable2)));

        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(테이블_요청_만들기(2L), 테이블_요청_만들기(3L)));

        given(orderTableRepository.findAllById(request.getRequestOrderTableIds()))
                .willReturn(Arrays.asList(emptyTable1, emptyTable2));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request));
    }

    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
        long requestTableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 3, false);

        given(orderTableDao.findAllByTableGroupId(requestTableGroupId))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(
                Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(false);

        //when
        tableGroupService.ungroup(requestTableGroupId);

        //then
        assertThat(orderTable1.getTableGroupId()).isNull();
        assertThat(orderTable2.getTableGroupId()).isNull();
    }

    @DisplayName("주문 상태가 조리, 식사인 경우가 있으면 단체 지정 해제 할 수 없다.")
    @Test
    void ungroup_order_status_cooking_meal() {
        //given
        long requestTableGroupId = 1L;
        OrderTable orderTable1 = new OrderTable(1L, 1L, 2, false);
        OrderTable orderTable2 = new OrderTable(2L, 1L, 3, false);

        given(orderTableDao.findAllByTableGroupId(requestTableGroupId))
                .willReturn(Arrays.asList(orderTable1, orderTable2));
        given(orderDao.existsByOrderTableIdInAndOrderStatusIn(Arrays.asList(orderTable1.getId(), orderTable2.getId())
                , Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))
                .willReturn(true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(requestTableGroupId));
    }

}

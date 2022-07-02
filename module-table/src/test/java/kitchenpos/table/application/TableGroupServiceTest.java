package kitchenpos.table.application;

import static kitchenpos.helper.OrderFixtureHelper.주문_만들기;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_요청_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.helper.OrderLineItemBuilder;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("단체 지정 관련 Service 기능 테스트")
@DataJpaTest
@Import(TableGroupService.class)
class TableGroupServiceTest {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupService tableGroupService;

    @DisplayName("단체 지정을 등록 한다.")
    @Test
    void create() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(1L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(2L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2));

        //when
        TableGroupResponse result = tableGroupService.create(request, LocalDateTime.now());

        //then
        List<OrderTableResponse> orderTables = result.getOrderTables();
        assertThat(orderTables.get(0).getEmpty()).isFalse();
        assertThat(orderTables.get(0).getTableGroupId()).isNotNull();
        assertThat(orderTables.get(1).getEmpty()).isFalse();
        assertThat(orderTables.get(1).getTableGroupId()).isNotNull();
    }

    @DisplayName("테이블이 2개 미만이면 단체 지정을 등록 할 수 없다.")
    @Test
    void create_less_then_two() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        TableGroupRequest request_single = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1));
        TableGroupRequest request_empty = 테이블_그룹_요청_만들기(Collections.emptyList());

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_single, LocalDateTime.now()));
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request_empty, LocalDateTime.now()));
    }


    @DisplayName("등록 되어 있지 않은 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_not_registered_table() {
        //given
        OrderTableRequest not_registered_table = 테이블_요청_만들기(99999L);
        OrderTableRequest emptyTable = 테이블_요청_만들기(3L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(not_registered_table, emptyTable));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }


    @DisplayName("주문 테이블이 있는 경우 단체 지정을 등록 할 수 없다.")
    @Test
    void create_in_order_table() {
        //given
        OrderTable orderTable = orderTableRepository.save(테이블_만들기(3, false));
        OrderTableRequest orderTable1 = 테이블_요청_만들기(orderTable.getId());
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        TableGroupRequest request = 테이블_그룹_요청_만들기(Arrays.asList(orderTable1, emptyTable1));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request, LocalDateTime.now()));
    }


    @DisplayName("이미 단체 지정된 테이블이 있는 경우 단체 지정 등록 할 수 없다.")
    @Test
    void create_already() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(3L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(4L);
        OrderTableRequest emptyTable3 = 테이블_요청_만들기(5L);
        tableGroupService.create(테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2)), LocalDateTime.now());
        TableGroupRequest request2 = 테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable3));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.create(request2, LocalDateTime.now()));
    }

    @DisplayName("단체 지정을 해제 한다.")
    @Test
    void ungroup() {
        //given
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(5L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(6L);
        TableGroupResponse request = tableGroupService
                .create(테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2)), LocalDateTime.now());

        //when
        tableGroupService.ungroup(request.getId());

        //then
        assertThat(orderTableRepository.findTableGroupId(5L)).isNull();
        assertThat(orderTableRepository.findTableGroupId(6L)).isNull();
    }

    @DisplayName("주문 상태가 조리, 식사인 경우가 있으면 단체 지정 해제 할 수 없다.")
    @Test
    void ungroup_order_status_cooking_meal() {
        //given
        long menuId = 1;
        OrderTableRequest emptyTable1 = 테이블_요청_만들기(7L);
        OrderTableRequest emptyTable2 = 테이블_요청_만들기(8L);
        TableGroupResponse request = tableGroupService
                .create(테이블_그룹_요청_만들기(Arrays.asList(emptyTable1, emptyTable2)), LocalDateTime.now());
        OrderTable orderTable = orderTableRepository.findById(emptyTable1.getId())
                .orElseThrow(IllegalArgumentException::new);
        OrderLineItem orderLineItem = OrderLineItemBuilder.builder().menuId(menuId).menuName("테스트 메뉴").price(1000)
                .quantity(1).build();

        orderRepository.save(주문_만들기(OrderStatus.MEAL, orderLineItem, orderTable.getId()));

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupService.ungroup(request.getId()));

    }

}

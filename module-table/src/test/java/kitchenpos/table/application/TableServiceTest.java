package kitchenpos.table.application;

import static kitchenpos.helper.OrderFixtureHelper.주문_만들기;
import static kitchenpos.helper.TableFixtures.빈_테이블_요청;
import static kitchenpos.helper.TableFixtures.주문_테이블;
import static kitchenpos.helper.TableFixtures.주문_테이블_요청;
import static kitchenpos.helper.TableFixtures.테이블_만들기;
import static kitchenpos.helper.TableFixtures.테이블_요청_만들기;
import static kitchenpos.helper.TableGroupFixtures.테이블_그룹_만들기;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.util.Arrays;
import kitchenpos.helper.OrderLineItemBuilder;
import kitchenpos.order.consts.OrderStatus;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.repository.OrderTableRepository;
import kitchenpos.table.domain.repository.TableGroupRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DisplayName("테이블 관련 Service 기능 테스트")
@DataJpaTest
@Import({TableService.class})
class TableServiceTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TableService tableService;

    @DisplayName("테이블을 생성한다.")
    @Test
    void create() {
        //given
        OrderTableRequest request = 빈_테이블_요청;

        //when
        OrderTableResponse result = tableService.create(request);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getTableGroupId()).isEqualTo(request.getTableGroupId());
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("빈 테이블 여부를 업데이트 한다.")
    @Test
    void changeEmpty() {
        //given
        long requestTableId = 1L;
        OrderTableRequest request = 주문_테이블_요청;

        //when
        OrderTableResponse result = tableService.changeEmpty(requestTableId, request);

        //then
        assertThat(result.getEmpty()).isEqualTo(request.getEmpty());
    }

    @DisplayName("단체 지정이 되어있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_table_group() {
        //given
        OrderTable orderTable1 = 테이블_만들기(0, true);
        OrderTable orderTable2 = 테이블_만들기(0, true);
        TableGroup tableGroup = 테이블_그룹_만들기(null, Arrays.asList(orderTable1, orderTable2));
        tableGroupRepository.save(tableGroup);

        OrderTableRequest request = 테이블_요청_만들기(0, false);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable2.getId(), request))
                .withMessageContaining("그룹이 지정 되어있는 경우 빈테이블 여부 업데이트 할수 없습니다.");
    }

    @DisplayName("주문 상태가 조리, 식사인 경우가 있는 경우 빈 테이블 여부 업데이트 할 수 없다.")
    @Test
    void changeEmpty_order_status_cooking_or_meal() {
        //given
        OrderLineItem orderLineItem = OrderLineItemBuilder.builder()
                .menuId(1L).menuName("테스트 메뉴").price(10000).quantity(1).build();
        OrderTable orderTable1 = orderTableRepository.save(테이블_만들기(3, false));

        Order meal = 주문_만들기(OrderStatus.MEAL, orderLineItem, orderTable1.getId());
        Order completion1 = 주문_만들기(OrderStatus.COMPLETION, orderLineItem, orderTable1.getId());
        orderRepository.save(meal);
        orderRepository.save(completion1);

        OrderTable orderTable2 = orderTableRepository.save(테이블_만들기(3, false));
        Order cooking = 주문_만들기(OrderStatus.COOKING, orderLineItem, orderTable2.getId());
        Order completion2 = 주문_만들기(OrderStatus.COMPLETION, orderLineItem, orderTable2.getId());
        orderRepository.save(cooking);
        orderRepository.save(completion2);

        OrderTable orderTable3 = orderTableRepository.save(테이블_만들기(3, false));
        Order completion3 = 주문_만들기(OrderStatus.COMPLETION, orderLineItem, orderTable3.getId());
        Order completion4 = 주문_만들기(OrderStatus.COMPLETION, orderLineItem, orderTable3.getId());
        orderRepository.save(completion3);
        orderRepository.save(completion4);

        OrderTableRequest request = 테이블_요청_만들기(true);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable1.getId(), request))
                .withMessageContaining("식사 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다.");
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable2.getId(), request))
                .withMessageContaining("조리 상태인 주문이 있어 빈 테이블 여부 업데이트 할 수 없습니다.");
        assertThatNoException()
                .isThrownBy(() -> tableService.changeEmpty(orderTable3.getId(), request));
    }
    
    @DisplayName("방문 손님 수를 업데이트 한다.")
    @Test
    void changeNumberOfGuests() {
        //given
        OrderTable orderTable = 테이블_만들기(3, false);
        OrderTable save = orderTableRepository.save(orderTable);
        long requestTableId = save.getId();
        OrderTableRequest request = 테이블_요청_만들기(3);

        //when
        OrderTableResponse result = tableService.changeNumberOfGuests(requestTableId, request);

        //then
        assertThat(result.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수가 0명 미만인 경우 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_less_than_zero() {
        //given
        long requestTableId = 주문_테이블_등록됨(주문_테이블);
        OrderTableRequest request = 테이블_요청_만들기(-1);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request))
                .withMessageContaining("방문 손님 수가 0명 미만일 수 없습니다.");
    }

    @DisplayName("빈 테이블인 경우 방문 손님 수를 업데이트 할 수 없다.")
    @Test
    void changeNumberOfGuests_empty_table() {
        //given
        OrderTable orderTable = 테이블_만들기(3, true);
        long requestTableId = 주문_테이블_등록됨(orderTable);
        OrderTableRequest request = 테이블_요청_만들기(5);

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(requestTableId, request))
                .withMessageContaining("빈 테이블은 방문 손님 수를 변경할 수 없습니다.");
    }

    private long 주문_테이블_등록됨(OrderTable orderTable) {
        OrderTable save = orderTableRepository.save(orderTable);
        return save.getId();
    }

}

package api.order.application;

import domain.order.*;
import domain.order.exception.*;
import api.order.dto.OrderTableRequest;
import api.order.dto.OrderTableResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("주문 테이블 관리")
class TableServiceTest extends DataBaseCleanSupport {

    @Autowired
    private OrderTableService tableService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderTable 주문한_테이블;
    private Order 주문됨;

    @BeforeEach
    void setUp() {
        주문한_테이블 = orderTableRepository.save(OrderTable.of(4, false));
        주문됨 = 주문한_테이블.ordered(OrderLineItems.of(Lists.list()));
    }

    @DisplayName("주문 테이블을 추가한다.")
    @Test
    void create() {
        //when
        OrderTableResponse actual = tableService.create(OrderTableRequest.of(false));

        //then
        assertThat(actual.getId()).isNotNull();
    }

    @DisplayName("주문 테이블을 모두 조회한다.")
    @Test
    void list() {
        //when
        List<OrderTableResponse> actual = tableService.findOrderTableResponses();

        //then
        assertThat(actual.size()).isNotZero();
    }

    @DisplayName("특정 주문 테이블의 상태를 변경한다.")
    @Test
    void changeEmpty() {
        //given
        주문됨.changeOrderStatus(OrderStatus.COMPLETION);

        //when
        OrderTableResponse actual = tableService.changeEmpty(주문한_테이블.getId(), OrderTableRequest.of(false));

        //then
        assertThat(actual.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 주문 상태가 계산 완료가 아니면 테이블 상태를 변경할 수 없다.")
    @Test
    void changeEmptyExceptionIfTableOrderStatusIsNotCompletion() {
        //when
        assertThatThrownBy(() -> tableService.changeEmpty(주문한_테이블.getId(), OrderTableRequest.of(false)))
                .isInstanceOf(CannotChangeEmptyException.class); //then
    }

    @DisplayName("특정 테이블의 인원 수를 예약한다.")
    @Test
    void changeNumberOfGuests() {
        //when
        OrderTableResponse actual = tableService.changeNumberOfGuests(주문한_테이블.getId(), OrderTableRequest.of(2));

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getNumberOfGuests()).isEqualTo(2);
    }

    @DisplayName("주문 불가능 상태일 경우 인원수를 변경할 수 없다.")
    @Test
    void changeNumberOfGuestsIfTableEmptyIsTrue() {
        //given
        Long 주문불가능_테이블_id = orderTableRepository.save(OrderTable.of(4, true)).getId();
        OrderTableRequest orderTableRequest = OrderTableRequest.of(10);

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문불가능_테이블_id, orderTableRequest))
                .isInstanceOf(CannotChangeGuestEmptyTableException.class); //then
    }

    @DisplayName("주문 테이블의 인원수는 최소 0명 이상이어야 한다.")
    @Test
    void createTableExceptionIfNumberOfGuestsIsNull() {
        //given
        Long 주문한_테이블_id = 주문한_테이블.getId();
        OrderTableRequest orderTableRequest = OrderTableRequest.of(-10);

        //when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문한_테이블_id, orderTableRequest))
                .isInstanceOf(InvalidNumberOfGuestsException.class); //then
    }
}

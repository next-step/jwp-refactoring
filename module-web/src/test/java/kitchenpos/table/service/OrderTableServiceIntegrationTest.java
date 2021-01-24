package kitchenpos.table.service;

import kitchenpos.IntegrationTest;
import kitchenpos.repository.OrderLineItemRepository;
import kitchenpos.repository.OrderRepository;
import kitchenpos.order.service.OrderService;
import kitchenpos.order.util.OrderRequestBuilder;
import kitchenpos.repository.OrderTableRepository;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.util.OrderTableBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class OrderTableServiceIntegrationTest extends IntegrationTest {

    @Autowired
    private OrderTableService tableService;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private OrderLineItemRepository orderLineItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    private OrderTableResponse orderTableResponse;

    @BeforeEach
    void setUp() {
        orderTableResponse = tableService.create(new OrderTableBuilder()
                .withNumberOfGuests(4)
                .withEmpty(true).requestBuild());
    }

    @AfterEach
    void tearDown() {
        orderLineItemRepository.deleteAllInBatch();
        orderRepository.deleteAllInBatch();
        orderTableRepository.deleteAllInBatch();
    }

    @DisplayName("테이블을 등록할 수 있다.")
    @Test
    void createTable() {
        // when then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(4);
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("테이블에 상태값을 변경 가능하다.")
    @Test
    void changeStatus() {
        OrderTableResponse orderTableResponse = tableService.changeEmpty(
                this.orderTableResponse.getId(),false);

        // when then
        assertThat(orderTableResponse.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 주문 상태가 요리중, 식사중일때 빈 테이블로 바꿀수 없다.")
    @Test
    void expectedExceptionNotCompleteOrderWhenChangeTableEmpty() {
        orderService.create(new OrderRequestBuilder()
                .withOrderTableId(this.orderTableResponse.getId())
                .addOrderLineItem(1L, 1)
                .addOrderLineItem(2L, 1)
                .build());

        assertThatThrownBy(() -> tableService.changeEmpty(
                this.orderTableResponse.getId(), false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("주문이 완료되지 않아 빈 테이블로 바꿀수 없습니다.");

    }

    @DisplayName("테이블에 인원을 변경 가능하다.")
    @Test
    void changeNumberOfGuests() {
        // when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(this.orderTableResponse.getId(),
                new OrderTableBuilder()
                        .withNumberOfGuests(5)
                        .requestBuild());

        // then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("테이블에 인원은 0명 이상이어야 한다.")
    @Test
    void tableNumberOfGuestsGraterThanZero() {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(orderTableResponse.getId(),
                new OrderTableBuilder()
                        .withNumberOfGuests(-1)
                        .requestBuild()
        ))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
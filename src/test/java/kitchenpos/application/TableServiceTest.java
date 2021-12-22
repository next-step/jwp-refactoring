package kitchenpos.application;

import static kitchenpos.application.fixture.OrderTableFixture.빈_테이블;
import static kitchenpos.application.fixture.OrderTableFixture.한명_주문테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderTableRepository;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.dto.order.OrderTableRequest;
import kitchenpos.dto.order.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 관리 기능")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("`주문 테이블`을 등록할 수 있다.")
    void create() {
        // given
        OrderTableRequest orderTableRequest = OrderTableRequest.of(0, true);
        OrderTable orderTable = orderTableRequest.toOrderTable();
        given(orderTableRepository.save(any())).willReturn(orderTable);

        // when
        OrderTableResponse 등록된_주문테이블 = tableService.create(orderTableRequest);

        // then
        주문테이블_등록됨(등록된_주문테이블);
        주문테이블_초기값_검증(등록된_주문테이블);
    }

    @Test
    @DisplayName("`주문 테이블`목록을 조회할 수 있다.")
    void list() {
        // given
        OrderTable 요청_주문테이블 = 한명_주문테이블();
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(요청_주문테이블));

        // when
        List<OrderTableResponse> 주문목록 = tableService.list();

        // then
        주문목록_조회_검증(주문목록);
    }

    @Test
    @DisplayName("`주문 테이블` 빈테이블로 변경한다.")
    void changeEmpty() {
        // given
        OrderTableRequest 요청_주문테이블 = OrderTableRequest.of(0, true);
        OrderTable 주문테이블 = 한명_주문테이블();
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문테이블));

        // when
        OrderTableResponse 빈테이블_변경_결과 = tableService.changeEmpty(1L, 요청_주문테이블);

        // then
        빈테이블_변경_검증(빈테이블_변경_결과);
    }

    @Test
    @DisplayName("`주문테이블` 방문 손님 수 변경한다.")
    void changeNumberOfGuests() {
        // given
        int 방문손님수 = 3;
        OrderTableRequest 요청_주문테이블 = OrderTableRequest.of(방문손님수, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(빈_테이블()));

        // when
        OrderTableResponse 빈테이블_변경_결과 = tableService.changeNumberOfGuests(1L, 요청_주문테이블);

        방문손님수_변경_검증(방문손님수, 빈테이블_변경_결과);
    }

    private void 주문테이블_등록됨(OrderTableResponse orderTableResponse) {
        assertThat(orderTableResponse).isNotNull();
    }

    private void 주문테이블_초기값_검증(OrderTableResponse orderTableResponse) {
        assertAll(
            () -> assertThat(orderTableResponse.isEmpty()).isTrue(),
            () -> assertThat(orderTableResponse.getTableGroupId()).isNull()
        );
    }

    private void 주문목록_조회_검증(List<OrderTableResponse> orderTableResponses) {
        assertThat(orderTableResponses).isNotEmpty();
    }

    private void 빈테이블_변경_검증(OrderTableResponse orderTableResponse) {
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    private void 방문손님수_변경_검증(int expect, OrderTableResponse orderTableResponse) {
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(expect);
    }

}

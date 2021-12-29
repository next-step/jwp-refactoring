package kitchenpos.application;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.ordertable.OrderTableRequest;
import kitchenpos.dto.ordertable.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("테이블 관련 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    private OrderTable 주문_테이블;
    private OrderTableRequest 주문_테이블_요청;
    private OrderTableRequest 주문_빈_테이블_요청;
    private OrderTableRequest 주문_손님변경_테이블_요청;

    @BeforeEach
    void setUp() {
        주문_테이블 = OrderTable.of(1L, 2, false);
        주문_테이블_요청 = OrderTableRequest.of(2, false);
        주문_빈_테이블_요청 = OrderTableRequest.of(0, true);
        주문_손님변경_테이블_요청 = OrderTableRequest.of(5, false);
    }

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        given(orderTableRepository.save(any())).willReturn(주문_테이블);

        final OrderTableResponse actual = tableService.create(주문_테이블_요청);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(1),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(주문_테이블));

        final List<OrderTableResponse> actual = tableService.list();

        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0)).isNotNull(),
                () -> assertThat(actual.get(0).getId()).isEqualTo(1),
                () -> assertThat(actual.get(0).getNumberOfGuests()).isEqualTo(2),
                () -> assertThat(actual.get(0).isEmpty()).isFalse()
        );
    }

    @DisplayName("주문 테이블을 비운다.")
    @Test
    void changeEmpty() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableAndOrderStatusIn(any(), any())).willReturn(false);

        final OrderTableResponse actual = tableService.changeEmpty(1L, 주문_빈_테이블_요청);

        assertAll(
                () ->  assertThat(actual).isNotNull(),
                () ->  assertThat(actual.getId()).isEqualTo(1),
                () ->  assertThat(actual.getNumberOfGuests()).isEqualTo(2),
                () ->  assertThat(actual.isEmpty()).isTrue()
        );
    }

    @DisplayName("주문 테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문_테이블));

        final OrderTableResponse actual = tableService.changeNumberOfGuests(1L, 주문_손님변경_테이블_요청);

        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(1),
                () -> assertThat(actual.getNumberOfGuests()).isEqualTo(주문_손님변경_테이블_요청.getNumberOfGuests()),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }
}

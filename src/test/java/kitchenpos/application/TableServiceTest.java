package kitchenpos.application;

import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.ordertable.OrderTableResponse;
import kitchenpos.fixture.TestOrderTableFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

    @DisplayName("주문 테이블을 등록한다.")
    @Test
    void create() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_생성됨(1L, 10, false);

        given(orderTableRepository.save(any())).willReturn(주문테이블);

        final OrderTableResponse actual = tableService.create(TestOrderTableFactory.주문_테이블_요청(10, false));

        TestOrderTableFactory.주문테이블_생성_확인됨(actual, 주문테이블);
    }

    @DisplayName("주문 테이블 목록을 조회한다.")
    @Test
    void list() {
        final List<OrderTable> 주문테이블_목록 = TestOrderTableFactory.주문_테이블_목록_조회됨(10);

        given(orderTableRepository.findAll()).willReturn(주문테이블_목록);

        final List<OrderTableResponse> actual = tableService.list();

        TestOrderTableFactory.주문_테이블_목록_확인됨(actual,주문테이블_목록);
    }

    @DisplayName("주문 테이블을 비운다.")
    @Test
    void changeEmpty() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any())).willReturn(false);

        final OrderTableResponse actual = tableService.changeEmpty(1L, TestOrderTableFactory.주문_빈테이블_요청());

        TestOrderTableFactory.주문_빈테이블_확인됨(actual, 주문테이블);
    }

    @DisplayName("주문 테이블의 손님수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false);

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));

        final OrderTableResponse actual = tableService.changeNumberOfGuests(1L, TestOrderTableFactory.주문_손님_변경_테이블_요청(5));

        TestOrderTableFactory.주문_손님변경테이블_확인됨(actual, 5);
    }
}

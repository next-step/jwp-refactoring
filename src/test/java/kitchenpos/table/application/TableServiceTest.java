package kitchenpos.table.application;

import static kitchenpos.table.domain.OrderTableTest.주문_테이블_생성;
import static kitchenpos.table.dto.OrderTableRequestTest.주문_테이블_생성_요청_객체_생성;
import static kitchenpos.table.dto.OrderTableResponseTest.주문_테이블_응답_객체들_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private ExistsOrderPort existsOrderPort;

    @InjectMocks
    private TableService tableService;


    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;
    private OrderTable 주문_테이블_3;

    @BeforeEach
    public void setUp() {
        주문_테이블_1 = 주문_테이블_생성(1L, null, 0, true);
        주문_테이블_2 = 주문_테이블_생성(2L, null, 0, false);
        주문_테이블_3 = 주문_테이블_생성(3L, null, 2, false);

    }


    @Test
    @DisplayName("주문 테이블 생성")
    void create() {
        // given
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(주문_테이블_1);
        OrderTableRequest 주문_테이블_생성_요청_객체 = 주문_테이블_생성_요청_객체_생성(주문_테이블_1.getNumberOfGuestsValue(), 주문_테이블_1.isEmpty());

        // when
        OrderTableResponse 주문_테이블_생성_응답_객체 = tableService.create(주문_테이블_생성_요청_객체);

        // then
        assertThat(주문_테이블_생성_응답_객체.getId()).isEqualTo(주문_테이블_1.getId());
    }

    @Test
    @DisplayName("주문 테이블 목록 조회")
    void list() {
        // given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // when
        List<OrderTableResponse> 주문_테이블_생성_응답_객체들 = tableService.list();

        // then
        assertThat(주문_테이블_생성_응답_객체들).hasSize(2);
        assertThat(주문_테이블_생성_응답_객체들).containsAll(주문_테이블_응답_객체들_생성(주문_테이블_1, 주문_테이블_2));
    }

    @Test
    @DisplayName("주문 테이블이 비었는지 여부 변경")
    void changeEmpty() {
        // given
        OrderTable 저장된_주문_테이블 = 주문_테이블_생성(1L, null, 0, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(저장된_주문_테이블));
        when(existsOrderPort.existsOrderStatusCookingOrMeal(anyLong())).thenReturn(false);

        OrderTableRequest 주문_테이블_생성_요청_객체 = 주문_테이블_생성_요청_객체_생성(0, false);

        // when
        OrderTableResponse 주문_테이블_생성_응답_객체 = tableService.changeEmpty(저장된_주문_테이블.getId(), 주문_테이블_생성_요청_객체);

        // then
        assertThat(주문_테이블_생성_응답_객체.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("주문 테이블에 방문한 손님 수를 변경")
    void changeNumberOfGuests() {
        // given
        OrderTable 저장된_주문_테이블 = 주문_테이블_생성(1L, null, 0, false);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(저장된_주문_테이블));
        OrderTableRequest 주문_테이블_생성_요청_객체 = 주문_테이블_생성_요청_객체_생성(2, false);

        // when
        OrderTableResponse 주문_테이블_생성_응답_객체 = tableService.changeNumberOfGuests(저장된_주문_테이블.getId(), 주문_테이블_생성_요청_객체);

        // then
        assertThat(주문_테이블_생성_응답_객체.getNumberOfGuests()).isEqualTo(주문_테이블_생성_요청_객체.getNumberOfGuests());
    }
}
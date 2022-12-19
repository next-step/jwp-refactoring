package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderTableService orderTableService;

    private Long 주문_테이블_1_id;
    private OrderTable 주문_테이블;
    private OrderTableRequest 주문_테이블_요청정보;

    @BeforeEach
    void setUp() {
        주문_테이블_1_id = 1L;
        주문_테이블 = new OrderTable(주문_테이블_1_id, null, 0, true);
        주문_테이블_요청정보 = OrderTableRequest.from(주문_테이블);
    }

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void 주문_테이블_생성() {
        when(orderTableRepository.save(주문_테이블_요청정보.toOrderTable())).thenReturn(주문_테이블_요청정보.toOrderTable());

        OrderTableResponse 생성된_테이블 = orderTableService.create(주문_테이블_요청정보);

        assertAll(
                () -> assertThat(생성된_테이블.getNumberOfGuests()).isEqualTo(주문_테이블_요청정보.getNumberOfGuests()),
                () -> assertThat(생성된_테이블.isEmpty()).isEqualTo(주문_테이블_요청정보.isEmpty())
        );
    }

    @DisplayName("주문 테이블을 조회할 수 있다")
    @Test
    void 주문_테이블_조회() {
        OrderTableRequest 주문_테이블_2 = new OrderTableRequest(null, 0, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블_요청정보.toOrderTable(), 주문_테이블_2.toOrderTable());
        when(orderTableRepository.findAll()).thenReturn(주문_테이블_목록);

        List<OrderTableResponse> 조회된_주문_테이블_목록 = orderTableService.list();

        assertAll(
                () -> assertThat(조회된_주문_테이블_목록.size()).isEqualTo(주문_테이블_목록.size()),
                () -> assertThat(조회된_주문_테이블_목록.get(0).getNumberOfGuests()).isEqualTo(주문_테이블_목록.get(0).getNumberOfGuests()),
                () -> assertThat(조회된_주문_테이블_목록.get(1).getNumberOfGuests()).isEqualTo(주문_테이블_목록.get(1).getNumberOfGuests())
        );
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 수정할 수 있다")
    @Test
    void 빈_테이블_여부_수정() {
        OrderTableRequest 수정할_주문_테이블 = new OrderTableRequest(null, 0, false);
        when(orderTableRepository.findById(주문_테이블_1_id)).thenReturn(Optional.of(주문_테이블));
        List<Order> orders = Collections.singletonList(new Order(주문_테이블_1_id, OrderStatus.COMPLETION.name()));
        when(orderRepository.findByOrderTableId(주문_테이블_1_id)).thenReturn(orders);
        when(orderTableRepository.save(주문_테이블)).thenReturn(주문_테이블);

        OrderTableResponse 수정된_주문_테이블 = orderTableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블);

        //빈 테이블 -> 빈 테이블 아님
        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(주문_테이블_1_id),
                () -> assertThat(수정된_주문_테이블.isEmpty()).isEqualTo(수정할_주문_테이블.isEmpty())
        );

        OrderTableRequest 수정할_주문_테이블2 = new OrderTableRequest(null, 0, true);
        OrderTableResponse 수정된_주문_테이블2 = orderTableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블2);

        //빈 테이블 아님 -> 빈 테이블
        assertAll(
                () -> assertThat(수정된_주문_테이블2.getId()).isEqualTo(주문_테이블_1_id),
                () -> assertThat(수정된_주문_테이블2.isEmpty()).isEqualTo(수정할_주문_테이블2.isEmpty())
        );
    }

    @DisplayName("생성되지 않은 주문 테이블의 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 생성되지_않은_주문_테이블_빈_테이블_여부_수정_예외처리() {
        Long 수정할_주문_테이블_id = 3L;
        OrderTableRequest 수정할_주문_테이블 = new OrderTableRequest(null, 0, false);
        when(orderTableRepository.findById(수정할_주문_테이블_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableService.changeEmpty(수정할_주문_테이블_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("단체 지정되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 단체_지정_주문_테이블_빈_테이블_여부_수정_예외처리() {
        TableGroup 단체 = new TableGroup(1L, null);
        Long 단체_지정된_주문_테이블_id = 2L;
        OrderTable 단체_지정된_주문_테이블 = new OrderTable(단체_지정된_주문_테이블_id, 단체.getId(), 0, true);
        OrderTableRequest 수정할_주문_테이블 = new OrderTableRequest(null, 0, false);
        when(orderTableRepository.findById(단체_지정된_주문_테이블_id)).thenReturn(Optional.of(단체_지정된_주문_테이블));

        assertThatThrownBy(() -> orderTableService.changeEmpty(단체_지정된_주문_테이블_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료되지 않은 주문이 등록되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 계산_완료_안된_주문_등록된_빈_테이블_여부_수정_예외처리() {
        OrderTableRequest 수정할_주문_테이블 = new OrderTableRequest(null, 0, false);
        when(orderTableRepository.findById(주문_테이블_1_id)).thenReturn(Optional.of(주문_테이블_요청정보.toOrderTable()));
        List<Order> orders = Collections.singletonList(new Order(주문_테이블_1_id, OrderStatus.MEAL.name()));
        when(orderRepository.findByOrderTableId(주문_테이블_1_id)).thenReturn(orders);

        assertThatThrownBy(() -> orderTableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수 수정")
    @Test
    void 주문_테이블_방문한_손님_수_수정() {
        Long 주문_테이블_2_id = 2L;
        OrderTable 주문_테이블_2 = new OrderTable(주문_테이블_2_id, null, 0, false);
        OrderTableRequest 수정할_테이블 = new OrderTableRequest(null, 5, false);
        when(orderTableRepository.findById(주문_테이블_2_id)).thenReturn(Optional.of(주문_테이블_2));
        when(orderTableRepository.save(주문_테이블_2)).thenReturn(주문_테이블_2);

        OrderTableResponse 수정된_주문_테이블 = orderTableService.changeNumberOfGuests(주문_테이블_2_id, 수정할_테이블);

        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(주문_테이블_2_id),
                () -> assertThat(수정된_주문_테이블.getNumberOfGuests()).isEqualTo(수정할_테이블.getNumberOfGuests())
        );
    }

    @DisplayName("0보다 작은 수로 주문 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 방문한_손님_수를_0보다_작은_수로_수정_예외처리() {
        Long 수정할_주문_테이블_id = 2L;
        OrderTableRequest 수정할_주문_테이블 = new OrderTableRequest(null, -1, false);

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(수정할_주문_테이블_id, 수정할_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성되지 않은 주문 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 생성안된_주문_테이블_방문한_손님_수_수정_예외처리() {
        Long 수정할_주문_테이블_id = 2L;
        OrderTableRequest 수정할_테이블 = new OrderTableRequest(null, 5, false);
        when(orderTableRepository.findById(수정할_주문_테이블_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(수정할_주문_테이블_id, 수정할_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 빈_테이블_방문한_손님_수_수정_예외처리() {
        Long 주문_테이블_2_id = 2L;
        OrderTable 주문_테이블_2 = new OrderTable(주문_테이블_2_id, null, 0, true);
        OrderTableRequest 수정할_테이블 = new OrderTableRequest(null, 5, false);
        when(orderTableRepository.findById(주문_테이블_2_id)).thenReturn(Optional.of(주문_테이블_2));

        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(주문_테이블_2_id, 수정할_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
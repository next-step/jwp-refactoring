package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
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
    OrderDao orderDao;
    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    private Long 주문_테이블_1_id;
    private OrderTable 주문_테이블_1;

    @BeforeEach
    void setUp() {
        주문_테이블_1_id = 1L;
        주문_테이블_1 = new OrderTable(주문_테이블_1_id, null, 0, true);
    }

    @DisplayName("주문 테이블을 생성한다")
    @Test
    void 주문_테이블_생성() {
        when(orderTableDao.save(주문_테이블_1)).thenReturn(주문_테이블_1);

        OrderTable 생성된_테이블 = tableService.create(주문_테이블_1);

        assertAll(
                () -> assertThat(생성된_테이블.getId()).isNotNull(),
                () -> assertThat(생성된_테이블).isEqualTo(주문_테이블_1)
        );
    }

    @DisplayName("주문 테이블을 조회할 수 있다")
    @Test
    void 주문_테이블_조회() {
        OrderTable 주문_테이블_2 = new OrderTable(2L, null, 0, true);
        List<OrderTable> 주문_테이블_목록 = Arrays.asList(주문_테이블_1, 주문_테이블_2);
        when(orderTableDao.findAll()).thenReturn(주문_테이블_목록);

        List<OrderTable> 조회된_주문_테이블_목록 = tableService.list();

        assertAll(
                () -> assertThat(조회된_주문_테이블_목록.size()).isEqualTo(주문_테이블_목록.size()),
                () -> assertThat(조회된_주문_테이블_목록).containsAll(주문_테이블_목록)
        );
    }

    @DisplayName("주문 테이블의 빈 테이블 여부를 수정할 수 있다")
    @Test
    void 빈_테이블_여부_수정() {
        OrderTable 수정할_주문_테이블 = new OrderTable(주문_테이블_1_id, null, 0, false);
        when(orderTableDao.findById(주문_테이블_1_id)).thenReturn(Optional.of(주문_테이블_1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                주문_테이블_1_id, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(false);
        when(orderTableDao.save(주문_테이블_1)).thenReturn(수정할_주문_테이블);

        OrderTable 수정된_주문_테이블 = tableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블);

        //빈 테이블 -> 빈 테이블 아님
        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(주문_테이블_1_id),
                () -> assertThat(수정된_주문_테이블.isEmpty()).isEqualTo(수정할_주문_테이블.isEmpty())
        );

        수정할_주문_테이블.setEmpty(true);
        OrderTable 수정된_주문_테이블2 = tableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블);

        //빈 테이블 아님 -> 빈 테이블
        assertAll(
                () -> assertThat(수정된_주문_테이블2.getId()).isEqualTo(주문_테이블_1_id),
                () -> assertThat(수정된_주문_테이블2.isEmpty()).isEqualTo(수정할_주문_테이블.isEmpty())
        );
    }

    @DisplayName("생성되지 않은 주문 테이블의 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 생성되지_않은_주문_테이블_빈_테이블_여부_수정_예외처리() {
        Long 수정할_주문_테이블_id = 3L;
        OrderTable 수정할_주문_테이블 = new OrderTable(수정할_주문_테이블_id, null, 0, false);
        when(orderTableDao.findById(수정할_주문_테이블_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeEmpty(수정할_주문_테이블_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("단체 지정되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 단체_지정_주문_테이블_빈_테이블_여부_수정_예외처리() {
        Long 단체_지정된_주문_테이블_id = 2L;
        OrderTable 단체_지정된_주문_테이블 = new OrderTable(단체_지정된_주문_테이블_id, 1L, 0, true);
        OrderTable 수정할_주문_테이블 = new OrderTable(단체_지정된_주문_테이블_id, null, 0, false);
        when(orderTableDao.findById(단체_지정된_주문_테이블_id)).thenReturn(Optional.of(단체_지정된_주문_테이블));

        assertThatThrownBy(() -> tableService.changeEmpty(단체_지정된_주문_테이블_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("계산 완료되지 않은 주문이 등록되어 있는 경우 빈 테이블 여부 수정 요청 시 예외처리")
    @Test
    void 계산_완료_안된_주문_등록된_빈_테이블_여부_수정_예외처리() {
        OrderTable 수정할_주문_테이블 = new OrderTable(주문_테이블_1_id, null, 0, false);
        when(orderTableDao.findById(주문_테이블_1_id)).thenReturn(Optional.of(주문_테이블_1));
        when(orderDao.existsByOrderTableIdAndOrderStatusIn(
                주문_테이블_1_id, Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).thenReturn(true);

        assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블_1_id, 수정할_주문_테이블)).isInstanceOf(
                IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 방문한 손님 수 수정")
    @Test
    void 주문_테이블_방문한_손님_수_수정() {
        Long 주문_테이블_2_id = 2L;
        OrderTable 주문_테이블_2 = new OrderTable(주문_테이블_2_id, null, 0, false);
        OrderTable 수정할_테이블 = new OrderTable(주문_테이블_2_id, null, 5, false);
        when(orderTableDao.findById(주문_테이블_2_id)).thenReturn(Optional.of(주문_테이블_2));
        when(orderTableDao.save(주문_테이블_2)).thenReturn(주문_테이블_2);

        OrderTable 수정된_주문_테이블 = tableService.changeNumberOfGuests(주문_테이블_2_id, 수정할_테이블);

        assertAll(
                () -> assertThat(수정된_주문_테이블.getId()).isEqualTo(주문_테이블_2_id),
                () -> assertThat(수정된_주문_테이블.getNumberOfGuests()).isEqualTo(수정할_테이블.getNumberOfGuests())
        );
    }

    @DisplayName("0보다 작은 수로 주문 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 방문한_손님_수를_0보다_작은_수로_수정_예외처리() {
        Long 수정할_주문_테이블_id = 2L;
        OrderTable 수정할_주문_테이블 = new OrderTable(수정할_주문_테이블_id, null, -1, false);

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(수정할_주문_테이블_id, 수정할_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생성되지 않은 주문 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 생성안된_주문_테이블_방문한_손님_수_수정_예외처리() {
        Long 수정할_주문_테이블_id = 2L;
        OrderTable 수정할_테이블 = new OrderTable(수정할_주문_테이블_id, null, 5, false);
        when(orderTableDao.findById(수정할_주문_테이블_id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(수정할_주문_테이블_id, 수정할_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블의 방문한 손님 수 수정 요청 시 예외처리")
    @Test
    void 빈_테이블_방문한_손님_수_수정_예외처리() {
        Long 주문_테이블_2_id = 2L;
        OrderTable 주문_테이블_2 = new OrderTable(주문_테이블_2_id, null, 0, true);
        OrderTable 수정할_테이블 = new OrderTable(주문_테이블_2_id, null, 5, false);
        when(orderTableDao.findById(주문_테이블_2_id)).thenReturn(Optional.of(주문_테이블_2));

        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문_테이블_2_id, 수정할_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
package kitchenpos.table.application;

import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderTableDao;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;

    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    private OrderTable 생성할_주문테이블;
    private OrderTable 저장된_주문테이블;

    @BeforeEach
    void setUp() {
        생성할_주문테이블 = new OrderTable(4, false);
        저장된_주문테이블 = new OrderTable(1L, null, 4, false);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        given(orderTableDao.save(생성할_주문테이블))
                .willReturn(저장된_주문테이블);

        // when
        OrderTable 생성된_주문테이블 = tableService.create(생성할_주문테이블);

        // then
        주문테이블_생성_성공(생성된_주문테이블, 생성할_주문테이블);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void listOrderTable() {
        // given
        List<OrderTable> 조회할_주문테이블_목록 = Arrays.asList(저장된_주문테이블);
        given(orderTableDao.findAll())
                .willReturn(조회할_주문테이블_목록);

        // when
        List<OrderTable> 조회된_주문테이블_목록 = tableService.list();
        주문테이블_목록_조회_성공(조회된_주문테이블_목록, 조회할_주문테이블_목록);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable 빈_주문테이블 = 주문테이블_생성(1L, null, 4, true);

        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(false);
        given(orderTableDao.save(any()))
                .willReturn(저장된_주문테이블);

        // when
        OrderTable 변경된_주문테이블 = 주문테이블_비우기(1L, 빈_주문테이블);

        // then
        주문테이블_비우기_성공(변경된_주문테이블);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문_테이블ID = 1000L;

        // when & then
        주문테이블_비우기_실패(존재하지_않는_주문_테이블ID, 저장된_주문테이블);
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenHasGroup() {
        // given
        OrderTable 빈_주문테이블 = 주문테이블_생성(1L, null, 4, true);
        OrderTable 단체_지정된_주문테이블 = 주문테이블_생성(1L, 1L, 4, false);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(단체_지정된_주문테이블));

        // when & then
        주문테이블_비우기_실패(1L, 빈_주문테이블);
    }

    @DisplayName("'조리' 혹은 '식사' 상태이면 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenCookingOrMeal() {
        // given
        OrderTable 빈_주문테이블 = 주문테이블_생성(1L, null, 4, true);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList()))
                .willReturn(true);

        // when & then
        주문테이블_비우기_실패(1L, 빈_주문테이블);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 손님_수_변경된_주문테이블 = 주문테이블_생성(1L, null, 6, false);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderTableDao.save(any()))
                .willReturn(손님_수_변경된_주문테이블);

        // when
        OrderTable 손님_수_변경_결과_주문테이블 = 주문테이블_손님_수_변경(1L, 손님_수_변경된_주문테이블);

        // then
        주문테이블_손님_수_변경_성공(손님_수_변경_결과_주문테이블, 손님_수_변경된_주문테이블);
    }

    @DisplayName("손님 수가 0보다 작으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenLowerThanZero() {
        // given
        OrderTable 손님_수_음수_주문테이블 = 주문테이블_생성(1L, null, -4, false);

        // when & then
        주문테이블_손님_수_변경_실패(1L, 손님_수_음수_주문테이블);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문테이블ID = 1000L;
        OrderTable 손님_수_변경된_주문테이블 = 주문테이블_생성(1L, null, 6, false);

        // when & then
        주문테이블_손님_수_변경_실패(존재하지_않는_주문테이블ID, 손님_수_변경된_주문테이블);
    }

    @DisplayName("주문 테이블이 비었으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenIsEmpty() {
        // given
        OrderTable 빈_주문테이블 = 주문테이블_생성(1L, null, 6, true);
        given(orderTableDao.findById(anyLong()))
                .willReturn(Optional.of(빈_주문테이블));

        // when & then
        주문테이블_손님_수_변경_실패(1L, 빈_주문테이블);
    }

    private OrderTable 주문테이블_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    private OrderTable 주문테이블_비우기(Long id, OrderTable orderTable) {
        return tableService.changeEmpty(id, orderTable);
    }

    private OrderTable 주문테이블_손님_수_변경(Long id, OrderTable orderTable) {
        return tableService.changeNumberOfGuests(id, orderTable);
    }


    private void 주문테이블_생성_성공(OrderTable 생성된_주문테이블, OrderTable 생성할_주문테이블) {
        assertAll(
                () -> assertThat(생성된_주문테이블.getId())
                        .isNotNull(),
                () -> assertThat(생성된_주문테이블.getTableGroupId())
                        .isNull(),
                () -> assertThat(생성된_주문테이블.getNumberOfGuests())
                        .isEqualTo(생성할_주문테이블.getNumberOfGuests())
        );
    }

    private void 주문테이블_비우기_실패(Long 주문테이블ID, OrderTable 주문테이블) {
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블ID, 주문테이블))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }

    private void 주문테이블_목록_조회_성공(List<OrderTable> 조회된_주문테이블_목록, List<OrderTable> 조회할_주문테이블_목록) {
        assertThat(조회된_주문테이블_목록)
                .hasSameElementsAs(조회할_주문테이블_목록);
    }

    private void 주문테이블_비우기_성공(OrderTable 빈_주문테이블) {
        assertThat(빈_주문테이블.isEmpty())
                .isTrue();
    }

    private void 주문테이블_손님_수_변경_성공(OrderTable 변경_전_주문테이블, OrderTable 변경_후_주문테이블) {
        assertThat(변경_전_주문테이블.getNumberOfGuests())
                .isEqualTo(변경_후_주문테이블.getNumberOfGuests());
    }

    private void 주문테이블_손님_수_변경_실패(Long 주문테이블ID, OrderTable 주문테이블) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블ID, 주문테이블))
                .isExactlyInstanceOf(IllegalArgumentException.class);
    }
}

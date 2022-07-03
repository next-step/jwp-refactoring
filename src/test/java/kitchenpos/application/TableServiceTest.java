package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 테이블 서비스")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    @InjectMocks
    private TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있고 생성된 주문 테이블은 단체 지정이 되지 않은 상태이다.")
    @Test
    void 주문_테이블_생성() {
        // given
        OrderTable 주문_테이블 = new OrderTable(5, true);
        given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable(1L, 5, true));

        // when
        OrderTable savedOrderTable = tableService.create(주문_테이블);

        // then
        assertAll(() -> assertThat(savedOrderTable).isNotNull(), () -> assertThat(savedOrderTable.getId()).isNotNull(),
                () -> assertThat(savedOrderTable.getTableGroupId()).isNull());
    }

    @DisplayName("전체 주문 테이블을 조회할 수 있다.")
    @Test
    void 전체_주문_테이블_조회() {
        // given
        OrderTable 주문_테이블 = new OrderTable(5, true);
        OrderTable 주문_테이블2 = new OrderTable(3, true);

        given(orderTableDao.findAll()).willReturn(Arrays.asList(주문_테이블, 주문_테이블2));

        // when
        List<OrderTable> orderTables = tableService.list();

        // then
        assertThat(orderTables).hasSize(2);
    }

    @DisplayName("주문 테이블 수정")
    @Nested
    class 주문_테이블_수정 {
        @DisplayName("단체 지정되지 않은 주문 테이블 중 계산완료된 주문 테이블은 빈 주문 테이블 여부를 변경할 수 있다.")
        @Test
        void 단체_지정되지_않고_계산완료인_주문_테이블의_빈_테이블_여부_변경_가능() {
            // given
            OrderTable 주문_테이블 = new OrderTable(1L, 5, false);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(),
                    eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(Boolean.FALSE);
            given(orderTableDao.save(any(OrderTable.class))).willReturn(new OrderTable(1L, 5, true));

            // when
            tableService.changeEmpty(주문_테이블.getId(), 주문_테이블);

            // then
            assertThat(주문_테이블.isEmpty()).isFalse();
        }

        @DisplayName("생성되지 않은 주문 테이블의 빈 테이블 값을 변경할 수 없다.")
        @Test
        void 존재하지_않는_주문_테이블_빈_테이블_여부_변경() {
            // given
            Long 존재하지_않은_주문_테이블_아이디 = 9999L;
            OrderTable 생성되지_않은_주문_테이블 = new OrderTable(존재하지_않은_주문_테이블_아이디, 5, false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> tableService.changeEmpty(존재하지_않은_주문_테이블_아이디, 생성되지_않은_주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("단체 지정된 주문 테이블의 빈 테이블 값을 변경할 수 없다.")
        @Test
        void 단체_지정된_주문_테이블_빈_테이블_여부_변경() {
            // given
            OrderTable 주문_테이블 = new OrderTable(1L, 1L, 5, false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));

            // when / then
            assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("단체가 아니며 계산완료가 아닌 주문 테이블의 빈 테이블 값을 변경할 수 없다.")
        @Test
        void 주문_상태가_계산_완료가_아닌_경우() {
            // given
            OrderTable 주문_테이블 = new OrderTable(1L, 5, false);

            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(주문_테이블));
            given(orderDao.existsByOrderTableIdAndOrderStatusIn(anyLong(),
                    eq(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name())))).willReturn(Boolean.TRUE);

            // when / then
            assertThatThrownBy(() -> tableService.changeEmpty(주문_테이블.getId(), 주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
        @Test
        void 주문_테이블_손님_수_변경() {
            // given
            OrderTable 주문_테이블 = new OrderTable(1L, 3, false);

            OrderTable 조회된_주문_테이블 = new OrderTable(1L, 5, false);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(조회된_주문_테이블));

            // when
            tableService.changeNumberOfGuests(주문_테이블.getId(), 주문_테이블);

            // then
            assertThat(조회된_주문_테이블.getNumberOfGuests()).isEqualTo(주문_테이블.getNumberOfGuests());
        }

        @DisplayName("주문 테이블의 손님 수를 0보자 작은 수으로 변경할 수 없다.")
        @Test
        void 주문_테이블의_손님_수를_0보다_작은_수로_변경() {
            // given
            OrderTable 손님_수가_음수인_주문_테이블 = new OrderTable(1L, -1);

            // when / then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 손님_수가_음수인_주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("생성되지 않은 주문 테이블의 손님 수를 변경할 수 없다.")
        @Test
        void 생성되지_않은_주문_테이블_손님_수_변경() {
            // given
            Long 존재하지_않는_주문_테이블_아이디 = 99999L;
            OrderTable 생성되지_않은_주문_테이블 = new OrderTable(존재하지_않는_주문_테이블_아이디, 10, false);

            given(orderTableDao.findById(eq(존재하지_않는_주문_테이블_아이디))).willReturn(Optional.empty());

            // when / then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(존재하지_않는_주문_테이블_아이디, 생성되지_않은_주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);
        }

        @DisplayName("빈 주문 테이블의 손님 수를 변경할 수 없다.")
        @Test
        void 빈_주문_테이블_손님_수_변경() {
            // given
            OrderTable 빈_주문_테이블 = new OrderTable(1L, 3, true);
            given(orderTableDao.findById(anyLong())).willReturn(Optional.of(new OrderTable(1L, 5, true)));

            // when / then
            assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 빈_주문_테이블)).isInstanceOf(
                    IllegalArgumentException.class);

        }
    }
}
package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderTable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 관련 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블 생성")
    @Test
    void create() {
        // given
        OrderTable request = 주문_테이블_데이터_생성(null, null, 2, false);
        OrderTable 예상값 = 주문_테이블_데이터_생성(1L, null, 2, false);
        given(orderTableDao.save(request)).willReturn(예상값);

        // when
        OrderTable 주문_테이블_생성_결과 = tableService.create(request);

        // then
        주문_테이블_데이터_비교(주문_테이블_생성_결과, 예상값);
    }

    @DisplayName("주문 테이블 목록 조회")
    @Test
    void list() {
        // given
        List<OrderTable> 예상값 = Arrays.asList(
                주문_테이블_데이터_생성(1L, null, 2, false),
                주문_테이블_데이터_생성(2L, null, 3, false)
        );
        given(orderTableDao.findAll()).willReturn(예상값);

        // when
        List<OrderTable> 주문_테이블_목록_조회_결과 = tableService.list();

        // then
        assertAll(
                () -> 주문_테이블_데이터_비교(주문_테이블_목록_조회_결과.get(0), 예상값.get(0)),
                () -> 주문_테이블_데이터_비교(주문_테이블_목록_조회_결과.get(1), 예상값.get(1))
        );
    }

    @DisplayName("빈 테이블로 상태 변경")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableDao.save(orderTable)).willReturn(orderTable);

        // when
        OrderTable 주문상태_변경_결과 = 주문_상태_변경(1L, orderTable);

        // then
        assertThat(주문상태_변경_결과.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블로 상태 변경 - 그룹으로 지정되어 있는 테이블일 경우")
    @Test
    void changeEmpty_exception1() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L,1L, 2, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 상태 변경 - 주문 상태가 '조리' 또는 '식사' 인 테이블일 경우")
    @Test
    void changeEmpty_exception2() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문 손님 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 변경전_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 2, false);
        OrderTable 변경후_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 4, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(변경전_주문_테이블));
        given(orderTableDao.save(any())).willReturn(변경후_주문_테이블);

        // when
        OrderTable 방문_손님_수_변경_결과 = 방문_손님_수_변경(1L, 변경후_주문_테이블);

        // then
        assertThat(방문_손님_수_변경_결과.getNumberOfGuests()).isEqualTo(변경후_주문_테이블.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수 변경 - 변경 하려는 주문 테이블의 방문 손님 수가 0보다 작을 경우")
    @Test
    void changeNumberOfGuests_exception1() {
        // given
        OrderTable 변경할_주문_테이블 = 주문_테이블_데이터_생성(1L, null, -1, false);

        // when && then
        assertThatThrownBy(() -> 방문_손님_수_변경(1L, 변경할_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문 손님 수 변경 - 주문 테이블이 빈 테이블일 경우")
    @Test
    void changeNumberOfGuests_exception2() {
        // given
        OrderTable 변경전_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 2, true);
        OrderTable 변경후_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 4, false);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(변경전_주문_테이블));

        // when && then
        assertThatThrownBy(() -> 방문_손님_수_변경(1L, 변경후_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTable 주문_테이블_데이터_생성(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    private OrderTable 주문_상태_변경(long orderTableId, OrderTable orderTable) {
        return tableService.changeEmpty(orderTableId, orderTable);
    }

    private OrderTable 방문_손님_수_변경(long orderTableId, OrderTable orderTable) {
        return tableService.changeNumberOfGuests(orderTableId, orderTable);
    }

    private void 주문_테이블_데이터_비교(OrderTable result, OrderTable expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getTableGroupId()).isEqualTo(expectation.getTableGroupId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(expectation.getNumberOfGuests()),
                () -> assertThat(result.isEmpty()).isEqualTo(expectation.isEmpty())
        );
    }
}

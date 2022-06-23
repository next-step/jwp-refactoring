package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.domain.tableGroup.TableGroup;
import kitchenpos.dto.orderTable.OrderTableRequest;
import kitchenpos.dto.orderTable.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableGroupServiceTest.테이블_그룹_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 테이블 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    OrderDao orderDao;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    TableService tableService;

    @DisplayName("주문 테이블을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderTableRequest request = 주문_테이블_요청_데이터_생성(2, false);
        OrderTable 예상값 = 주문_테이블_데이터_생성(1L, null, 2, false);
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(예상값);

        // when
        OrderTableResponse 주문_테이블_생성_결과 = tableService.create(request);

        // then
        주문_테이블_데이터_비교(주문_테이블_생성_결과, OrderTableResponse.of(예상값));
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        List<OrderTable> 예상값 = Arrays.asList(
                주문_테이블_데이터_생성(1L, null, 2, false),
                주문_테이블_데이터_생성(2L, null, 3, false)
        );
        given(orderTableRepository.findAll()).willReturn(예상값);

        // when
        List<OrderTableResponse> 주문_테이블_목록_조회_결과 = tableService.list();

        // then
        assertAll(
                () -> 주문_테이블_데이터_비교(주문_테이블_목록_조회_결과.get(0), OrderTableResponse.of(예상값.get(0))),
                () -> 주문_테이블_데이터_비교(주문_테이블_목록_조회_결과.get(1), OrderTableResponse.of(예상값.get(1)))
        );
    }

    @DisplayName("빈 테이블로 변경할 수 있다")
    @Test
    void changeEmpty() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTableResponse 주문상태_변경_결과 = 주문_상태_변경(1L, orderTable);

        // then
        assertThat(주문상태_변경_결과.isEmpty()).isTrue();
    }

    @DisplayName("빈 테이블로 변경할 수 있다 - 그룹으로 지정되지 않은 테이블이어야 한다")
    @Test
    void changeEmpty_exception1() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L,테이블_그룹_데이터_생성(1L, null), 2, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("빈 테이블로 변경할 수 있다 - 주문 상태가 '조리' 또는 '식사' 상태가 아니어야 한다")
    @Test
    void changeEmpty_exception2() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, orderTable))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문 손님 수를 변경할 수 있다")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 변경전_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 2, false);
        OrderTable 변경후_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 4, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(변경전_주문_테이블));
        given(orderTableRepository.save(any())).willReturn(변경후_주문_테이블);

        // when
        OrderTableResponse 방문_손님_수_변경_결과 = 방문_손님_수_변경(1L, 변경후_주문_테이블);

        // then
        assertThat(방문_손님_수_변경_결과.getNumberOfGuests()).isEqualTo(변경후_주문_테이블.getNumberOfGuests());
    }

    @DisplayName("방문 손님 수를 변경할 수 있다 - 방문 손님의 수는 0명 이상이어야 한다")
    @Test
    void changeNumberOfGuests_exception1() {
        // given
        OrderTable 변경할_주문_테이블 = 주문_테이블_데이터_생성(1L, null, -1, false);

        // when && then
        assertThatThrownBy(() -> 방문_손님_수_변경(1L, 변경할_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("방문 손님 수를 변경할 수 있다 - 주문 테이블이 빈 테이블이 아니어야 한다")
    @Test
    void changeNumberOfGuests_exception2() {
        // given
        OrderTable 변경전_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 2, true);
        OrderTable 변경후_주문_테이블 = 주문_테이블_데이터_생성(1L, null, 4, false);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(변경전_주문_테이블));

        // when && then
        assertThatThrownBy(() -> 방문_손님_수_변경(1L, 변경후_주문_테이블))
                .isInstanceOf(IllegalArgumentException.class);
    }

    public static OrderTableRequest 주문_테이블_요청_데이터_생성(int numberOfGuests, boolean empty) {
        return new OrderTableRequest(numberOfGuests, empty);
    }

    public static OrderTable 주문_테이블_데이터_생성(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroup, numberOfGuests, empty);
    }

    private OrderTableResponse 주문_상태_변경(long orderTableId, OrderTable orderTable) {
        return tableService.changeEmpty(orderTableId, orderTable);
    }

    private OrderTableResponse 방문_손님_수_변경(long orderTableId, OrderTable orderTable) {
        return tableService.changeNumberOfGuests(orderTableId, orderTable);
    }

    private void 주문_테이블_데이터_비교(OrderTableResponse result, OrderTableResponse expectation) {
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(expectation.getId()),
                () -> assertThat(result.getNumberOfGuests()).isEqualTo(expectation.getNumberOfGuests()),
                () -> assertThat(result.isEmpty()).isEqualTo(expectation.isEmpty())
        );
    }
}

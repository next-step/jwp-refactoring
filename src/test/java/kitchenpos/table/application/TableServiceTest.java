package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
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
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTable 생성할_주문테이블;
    private OrderTable 저장된_주문테이블;

    @BeforeEach
    void setUp() {
        생성할_주문테이블 = new OrderTable(4, false);
        저장된_주문테이블 = new OrderTable(1L, 4, false);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void createOrderTable() {
        // given
        OrderTableRequest 생성할_주문테이블_요청 = new OrderTableRequest(생성할_주문테이블.getNumberOfGuests(), false);
        given(orderTableRepository.save(생성할_주문테이블))
                .willReturn(저장된_주문테이블);

        // when
        OrderTableResponse 생성된_주문테이블_응답 = tableService.create(생성할_주문테이블_요청);

        // then
        주문테이블_생성_성공(생성된_주문테이블_응답, 생성할_주문테이블);
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void listOrderTable() {
        // given
        List<OrderTable> 조회할_주문테이블_목록 = Arrays.asList(저장된_주문테이블);

        given(orderTableRepository.findAll())
                .willReturn(조회할_주문테이블_목록);

        // when
        List<OrderTableResponse> 조회된_주문테이블_목록_응답 = tableService.list();
        주문테이블_목록_조회_성공(조회된_주문테이블_목록_응답, 조회할_주문테이블_목록);
    }

    @DisplayName("주문 테이블을 빈 테이블로 변경할 수 있다.")
    @Test
    void changeEmpty() {
        // given
        OrderTableRequest 빈_테이블_요청 = new OrderTableRequest(true);

        given(orderTableRepository.findById(any()))
                .willReturn(Optional.ofNullable(저장된_주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), any()))
                .willReturn(false);

        // when
        OrderTableResponse 빈_테이블_응답 = 주문테이블_비우기(1L, 빈_테이블_요청);

        // then
        주문테이블_비우기_성공(빈_테이블_응답);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문_테이블ID = 1000L;
        OrderTableRequest 빈_테이블_요청 = new OrderTableRequest(true);

        given(orderTableRepository.findById(eq(존재하지_않는_주문_테이블ID)))
                .willReturn(Optional.empty());

        // when & then
        주문테이블_비우기_실패_주문테이블_없음(존재하지_않는_주문_테이블ID, 빈_테이블_요청);
    }

    @DisplayName("단체 지정된 주문 테이블은 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenHasGroup() {
        // given
        OrderTableRequest 빈_테이블_요청 = new OrderTableRequest(true);
        OrderTable 단체_지정할_테이블1 = new OrderTable(1L, null, 4, true);
        OrderTable 단체_지정할_테이블2 = new OrderTable(2L, null, 6, true);
        TableGroup 단체_지정 = new TableGroup(1L, Arrays.asList(단체_지정할_테이블1, 단체_지정할_테이블2));
        저장된_주문테이블 = new OrderTable(1L, 단체_지정, 4, false);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(false);

        // when & then
        주문테이블_비우기_실패_단체_지정됨(1L, 빈_테이블_요청);
    }

    @DisplayName("'조리' 혹은 '식사' 상태이면 빈 테이블로 변경이 실패한다.")
    @Test
    void changeEmptyFailsWhenCookingOrMeal() {
        // given
        OrderTableRequest 빈_테이블_요청 = new OrderTableRequest(true);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList()))
                .willReturn(true);

        // when & then
        주문테이블_비우기_실패_완료_상태_아님(1L, 빈_테이블_요청);
    }

    @DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTableRequest 손님_수_변경_요청 = new OrderTableRequest(6);
        OrderTable 손님_수_변경된_주문테이블 = 주문테이블_생성(1L, 6, false);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));

        // when
        OrderTableResponse 손님_수_변경_결과_주문테이블 = 주문테이블_손님_수_변경(1L, 손님_수_변경_요청);

        // then
        주문테이블_손님_수_변경_성공(손님_수_변경_결과_주문테이블, 손님_수_변경된_주문테이블);
    }

    @DisplayName("손님 수가 0보다 작으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenLowerThanZero() {
        // given
        OrderTableRequest 손님_수_변경_요청 = new OrderTableRequest(-4);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));

        // when & then
        주문테이블_손님_수_변경_실패_손님_수_오류(1L, 손님_수_변경_요청);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenNoOrderTable() {
        // given
        Long 존재하지_않는_주문테이블ID = 1000L;
        OrderTableRequest 손님_수_변경_요청 = new OrderTableRequest(6);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.empty());

        // when & then
        주문테이블_손님_수_변경_실패_주문테이블_없음(존재하지_않는_주문테이블ID, 손님_수_변경_요청);
    }

    @DisplayName("주문 테이블이 비었으면 손님 수 변경이 실패한다.")
    @Test
    void changeNumberOfGuestsFailsWhenIsEmpty() {
        // given
        OrderTableRequest 손님_수_변경_요청 = new OrderTableRequest(6);
        저장된_주문테이블 = 주문테이블_생성(1L, 6, true);

        given(orderTableRepository.findById(anyLong()))
                .willReturn(Optional.of(저장된_주문테이블));

        // when & then
        주문테이블_손님_수_변경_실패_빈_주문테이블(1L, 손님_수_변경_요청);
    }


    private OrderTable 주문테이블_생성(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    private OrderTableResponse 주문테이블_비우기(Long id, OrderTableRequest 주문테이블_요청) {
        return tableService.changeEmpty(id, 주문테이블_요청);
    }

    private OrderTableResponse 주문테이블_손님_수_변경(Long id, OrderTableRequest 주문테이블_요청) {
        return tableService.changeNumberOfGuests(id, 주문테이블_요청);
    }

    private void 주문테이블_생성_성공(OrderTableResponse 생성된_주문테이블_응답, OrderTable 생성할_주문테이블) {
        assertAll(
                () -> assertThat(생성된_주문테이블_응답.getId())
                        .isNotNull(),
                () -> assertThat(생성된_주문테이블_응답.getTableGroupId())
                        .isNull(),
                () -> assertThat(생성된_주문테이블_응답.getNumberOfGuests())
                        .isEqualTo(생성할_주문테이블.getNumberOfGuests())
        );
    }

    private void 주문테이블_비우기_실패_주문테이블_없음(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    private void 주문테이블_비우기_실패_완료_상태_아님(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("주문 상태가 완료일때만 빈 테이블 여부 변경 가능합니다.");
    }

    private void 주문테이블_비우기_실패_단체_지정됨(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeEmpty(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("단체 지정이 된 테이블은 빈 테이블 여부를 변경할 수 없습니다.");
    }

    private void 주문테이블_목록_조회_성공(List<OrderTableResponse> 조회된_주문테이블_목록_응답, List<OrderTable> 조회할_주문테이블_목록) {
        assertThat(조회된_주문테이블_목록_응답)
                .hasSize(조회할_주문테이블_목록.size());
    }

    private void 주문테이블_비우기_성공(OrderTableResponse 빈_테이블_응답) {
        assertThat(빈_테이블_응답.isEmpty())
                .isTrue();
    }

    private void 주문테이블_손님_수_변경_성공(OrderTableResponse 변경_전_주문테이블, OrderTable 변경_후_주문테이블) {
        assertThat(변경_전_주문테이블.getNumberOfGuests())
                .isEqualTo(변경_후_주문테이블.getNumberOfGuests());
    }

    private void 주문테이블_손님_수_변경_실패_손님_수_오류(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(IllegalArgumentException.class)
                .hasMessage("손님 수는 최소 0명 이상이어야 합니다.");
    }

    private void 주문테이블_손님_수_변경_실패_주문테이블_없음(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(EntityNotFoundException.class)
                .hasMessage("주문 테이블이 존재하지 않습니다.");
    }

    private void 주문테이블_손님_수_변경_실패_빈_주문테이블(Long 주문테이블ID, OrderTableRequest 주문테이블_요청) {
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(주문테이블ID, 주문테이블_요청))
                .isExactlyInstanceOf(IllegalStateException.class)
                .hasMessage("빈 테이블의 손님 수는 변경할 수 없습니다.");
    }
}

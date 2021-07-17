package kitchenpos.table.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.OrderTables;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.exception.NotChangeNumberOfGuestThatEmptyTable;
import kitchenpos.table.exception.NotChangeToEmptyThatCookingOrMealTable;
import kitchenpos.table.exception.NotChangeToEmptyThatGroupTable;
import kitchenpos.table.exception.NotFoundOrderTableException;
import kitchenpos.table.handler.OrderTableMapper;
import kitchenpos.table.handler.OrderTableValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@DisplayName("테이블 기능 관련 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableServiceTest {
    private OrderTableRequest firstOrderTableRequest;
    private OrderTableRequest secondOrderTableRequest;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableMapper orderTableMapper;

    private OrderTableValidator orderTableValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTableService orderTableService;

    @BeforeEach
    public void setUp() {
        orderTableValidator = new OrderTableValidator(orderRepository);
        orderTableService = new OrderTableService(orderTableMapper, orderTableValidator, orderTableRepository);

        // given
        firstOrderTableRequest = 주문_테이블_생성(10);
        secondOrderTableRequest = 주문_테이블_생성(10);
    }

    @Test
    @DisplayName("테이블 등록 테스트")
    void 테이블_등록() {
        // when
        // 주문 테이블 등록 요청
        OrderTable orderTable = new OrderTable(10);
        when(orderTableMapper.mapFormToOrderTable(firstOrderTableRequest)).thenReturn(orderTable);
        when(orderTableRepository.save(orderTable)).thenReturn(orderTable);
        OrderTableResponse expected = orderTableService.create(firstOrderTableRequest);

        // then
        // 주문 테이블 저장
        assertThat(expected.getId()).isEqualTo(orderTable.getId());
        assertThat(expected.getTableGroupId()).isNull();
    }

    @Test
    @DisplayName("주문 테이블 리스트 조회")
    void 테이블_리스트_조회() {
        // given
        // 주문 테이블 저장되어 있음
        OrderTable firstOrderTable = new OrderTable(10);
        OrderTable secondOrderTable = new OrderTable(10);

        // when
        // 주문 테이블 리스트 조회함
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(firstOrderTable, secondOrderTable));
        List<OrderTableResponse> expected = orderTableService.list();

        // then
        // 리스트 조회됨
        assertThat(expected.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("저장되어 있지 않은 주문의 테이블을 빈 테이블로 변경 시 예외 처리")
    void isBlackOrderTable_exception() {
        // when
        // 주문 테이블 리스트 조회
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeEmpty(1L))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("단체 테이블에 속한 테이블 변경 불가")
    void isOrderTableOfGroupTable_exception() {
        // give
        // 해당 주문 테이블은 단체 테이블임
        OrderTable firstOrderTable = new OrderTable(10);
        TableGroup tableGroup = new TableGroup(new OrderTables(Arrays.asList(firstOrderTable)));
        firstOrderTable.changeTableGroup(tableGroup);

        // when
        // 주문 테이블 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeEmpty(1L))
                .isInstanceOf(NotChangeToEmptyThatGroupTable.class);
    }

    @Test
    @DisplayName("요리중이거나 식사중인 테이블 변경 불가")
    void isCookingOrMealingOrderTable_exception() {
        // give
        // 해당 주문 테이블 생성되어 있음
        OrderTable firstOrderTable = new OrderTable(1L, 10);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));
        order.progressCook();

        // when
        // 주문테이블과 주문 등록되어 있음
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));
        when(orderRepository.findByOrderTableId(1L)).thenReturn(Optional.of(order));

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeEmpty(1L))
                .isInstanceOf(NotChangeToEmptyThatCookingOrMealTable.class);
    }

    @Test
    @DisplayName("주문 테이블 정상 변경")
    void 주문_테이블_정상_변경() {
        // give
        // 주문 테이블 등록되어 있음
        OrderTable firstOrderTable = new OrderTable(1L, 10);

        // and
        // 주문 메뉴 및 테이블 생성되어 있음
        OrderLineItem firstOrderLineItem = new OrderLineItem(1L, 1L);
        OrderLineItem secondOrderLineItem = new OrderLineItem(2L, 1L);
        Order order = new Order(1L, new OrderLineItems(Arrays.asList(firstOrderLineItem, secondOrderLineItem)));

        // and
        // 계산 완료 테이블
        order.changeOrderStatusComplete();
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(firstOrderTable));
        when(orderRepository.findByOrderTableId(1L)).thenReturn(Optional.of(order));

        //then 변경됨
        OrderTableResponse expected = orderTableService.changeEmpty(1L);
        assertThat(expected.getEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블 손님 명수 0 이하 일때 오류")
    void isZeroGuestNumber_exception() {
        // given
        // 손님 명수 오류 등록
        firstOrderTableRequest = 주문_테이블_생성(-10);

        // and
        // 주문 테이블 등록되어 있음.
        OrderTable orderTable = new OrderTable(1L, 10);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("존재하지 않는 테이블 예외처리")
    void isBlankOrderTable_exception() {
        // when
        // 등록되지 않은 테이블
        when(orderTableRepository.findById(1L)).thenReturn(Optional.empty());

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(NotFoundOrderTableException.class);
    }

    @Test
    @DisplayName("빈 테이블 예외처리")
    void isEmptyOrderTable_exception() {
        // when
        // 빈 테이블임
        OrderTable orderTable = new OrderTable(1L, 10);
        orderTable.changeToEmpty();
        when(orderTableRepository.findById(1L)).thenReturn(Optional.of(orderTable));

        // then
        // 예외 발생
        assertThatThrownBy(() -> orderTableService.changeNumberOfGuests(1L, firstOrderTableRequest))
                .isInstanceOf(NotChangeNumberOfGuestThatEmptyTable.class);
    }

    @Test
    @DisplayName("주문 테이블 인원수 정상 변경")
    void 주문_테이블_인원수_정상_변경() {
        // when
        // 비지 않은 테이블 생성
        OrderTable orderTable = new OrderTable(1L, 5);
        when(orderTableRepository.findById(1L)).thenReturn(Optional.ofNullable(orderTable));

        // then
        // 정상 변경
        OrderTableResponse expected = orderTableService.changeNumberOfGuests(1L, firstOrderTableRequest);
        assertThat(expected.getNumberOfGuests()).isEqualTo(10);
    }

    private OrderTableRequest 주문_테이블_생성(int numberOfGuests) {
        return new OrderTableRequest(numberOfGuests);
    }
}

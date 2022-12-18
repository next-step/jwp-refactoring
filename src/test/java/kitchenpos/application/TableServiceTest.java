package kitchenpos.application;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableChangeRequest;
import kitchenpos.table.dto.OrderTableCreateRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.message.NumberOfGuestsMessage;
import kitchenpos.table.message.OrderTableMessage;
import kitchenpos.tablegroup.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private OrderTableCreateRequest request;

    @BeforeEach
    void setUp() {
        request = new OrderTableCreateRequest(0, true);
    }

    @Test
    @DisplayName("주문 테이블 등록시 성공하고 주문 테이블 정보를 반환한다")
    void createOrderTableThenReturnOrderTableInfoResponseTest() {
        // given
        given(orderTableRepository.save(any())).willReturn(request.toOrderTable());

        // when
        OrderTableResponse response = tableService.createOrderTable(request);

        // then
        assertAll(
                () -> assertThat(response.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
                () -> assertThat(response.isEmpty()).isEqualTo(request.isEmpty())
        );
    }

    @Test
    @DisplayName("주문 테이블 목록 조회시 등록된 주문 테이블 목록을 반환한다")
    void findAllOrderTablesThenReturnOrderTableResponsesTest() {
        // given
        List<OrderTable> expectedOrderTables = Arrays.asList(
                OrderTable.of(1, true),
                OrderTable.of(2, true)
        );
        given(orderTableRepository.findAll()).willReturn(expectedOrderTables);

        // when
        List<OrderTableResponse> orderTableResponses = tableService.findAllOrderTables();

        // then
        List<Integer> numberOfGuests = orderTableResponses.stream()
                .map(OrderTableResponse::getNumberOfGuests)
                .collect(Collectors.toList());
        List<Integer> expectedNumberOfGuests = expectedOrderTables.stream()
                .map(OrderTable::getNumberOfGuests)
                .map(NumberOfGuests::value)
                .collect(Collectors.toList());
        assertAll(
                () -> assertThat(orderTableResponses).hasSize(expectedOrderTables.size()),
                () -> assertThat(numberOfGuests).containsAll(expectedNumberOfGuests)
        );
    }

    @ParameterizedTest(name = "주문 테이블의 이용 여부 변경시 변경에 성공한다 - 기존 이용 여부 : [{0}], 변경 된 이용 여부 : [{1}]")
    @MethodSource("주문_테이블의_이용_여부_변경_테스트_파라미터")
    void changeOrderTableStateTest(boolean currentEmpty, boolean expectedEmpty) {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(0, !currentEmpty);
        OrderTable expectedOrderTable = OrderTable.of(0, currentEmpty);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expectedOrderTable));

        // when
        OrderTableResponse orderTableResponse = tableService.changeEmpty(1L, changeRequest);

        // then
        assertThat(orderTableResponse.isEmpty()).isEqualTo(expectedEmpty);
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블이 미등록된경우 변경에 실패한다")
    void changeOrderTableStateThrownByNotFoundTableTest() {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(0, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeRequest))
                .isInstanceOf(EntityNotFoundException.class);

        // then
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블 그룹에 포함되어있는경우 변경에 실패한다")
    void changeOrderTableEmptyThrownByEnrolledTableGroupTest() {
        // given
        OrderTable orderTable = OrderTable.of(0, true);
        List<OrderTable> orderTables = Arrays.asList(
                orderTable,
                OrderTable.of(2, true)
        );
        TableGroup tableGroup = new TableGroup(orderTables);
        tableGroup.group();
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(0, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());

        // then
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 이용 여부 변경시 테이블의 상태가 조리 또는 식사중인경우 변경에 실패한다")
    void changeOrderTableEmptyThrownByTableStateTest() {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(0, false);
        OrderTable expectedOrderTable = OrderTable.of(0, true);
        expectedOrderTable.addOrder(new Order(OrderStatus.COOKING));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expectedOrderTable));

        // when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE.message());

        // then
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 변경에 성공한다")
    void changeNumberOfGuestsTest() {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(1, false);
        OrderTable expectedOrderTable = OrderTable.of(0, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expectedOrderTable));

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(1L, changeRequest);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(1);
        then(orderTableRepository).should(times(1)).findById(any());
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 변경가능한 최소 인원보다 적게 주어진다면 변경에 실패한다")
    void changeNumberOfGuestsThrownByLessThanMinGuestsTest() {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(-1, false);
        OrderTable expectedOrderTable = OrderTable.of(0, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expectedOrderTable));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(NumberOfGuestsMessage.CREATE_ERROR_GUESTS_MUST_BE_MORE_THAN_ZERO.message());
    }

    @Test
    @DisplayName("주문 테이블의 손님수 변경시 해당 테이블이 빈테이블이라면 변경에 실패한다")
    void changeNumberOfGuestsThrownByEmptyTableTest() {
        // given
        OrderTableChangeRequest changeRequest = new OrderTableChangeRequest(1, false);
        OrderTable expectedOrderTable = OrderTable.of(0, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(expectedOrderTable));

        // when
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, changeRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableMessage.CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE.message());

        // then
        then(orderTableRepository).should(times(1)).findById(any());
    }

    private static Stream<Arguments> 주문_테이블의_이용_여부_변경_테스트_파라미터() {
        return Stream.of(
                Arguments.of(true, false),
                Arguments.of(false, true)
        );
    }
}

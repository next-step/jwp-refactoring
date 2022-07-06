package kitchenpos.application;

import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성_요청_객체;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_목록_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성_요청_객체;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import kitchenpos.application.table.TableService;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.TableGroup;
import kitchenpos.dto.table.CreateOrderTableRequest;
import kitchenpos.dto.table.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Table")
class TableServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    @ParameterizedTest(name = "case[{index}] : {0} => {1}")
    @MethodSource
    @DisplayName("주문 테이블을 생성한다.")
    public void createOrderTable(final CreateOrderTableRequest 주문_테이블_생성_객체, final String testDescription) {
        // Given
        given(orderTableRepository.save(any(OrderTable.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        OrderTableResponse actualOrderTable = tableService.create(주문_테이블_생성_객체);

        // Then
        verify(orderTableRepository).save(any(OrderTable.class));
        assertThat(actualOrderTable.isEmpty())
            .as(testDescription)
            .isEqualTo(주문_테이블_생성_객체.isEmpty());
    }

    private static Stream<Arguments> createOrderTable() {
        return Stream.of(
            Arguments.of(비어있는_주문_테이블_생성_요청_객체(), "비어 있는 주문 테이블 생성"),
            Arguments.of(비어있지_않은_주문_테이블_생성_요청_객체(), "비어 있지 않은 주문 테이블 생성")
        );
    }

    @Test
    @DisplayName("주문 테이블 목록을 조회 한다.")
    public void getAllOrderTables() {
        // Given
        final int generateOrderTableCount = 5;
        List<OrderTable> givenOrderTables = 비어있지_않은_주문_테이블_목록_생성(generateOrderTableCount);
        given(orderTableRepository.findAll()).willReturn(givenOrderTables);

        // When
        List<OrderTableResponse> actualOrderTables = tableService.list();

        // Then
        assertThat(actualOrderTables).hasSize(generateOrderTableCount);
    }

    @Test
    @DisplayName("테이블의 사용 가능 여부를 변경한다.")
    public void changeEmpty() {
        // Given
        OrderTable givenOrderTable = 비어있지_않은_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(givenOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(false);

        // When
        OrderTable updateEmptyRequest = 비어있지_않은_주문_테이블_생성();
        updateEmptyRequest.changeEmpty(true);
        OrderTableResponse actualOrderTable = tableService.changeEmpty(anyLong(), updateEmptyRequest);

        // Then
        verify(orderTableRepository).findById(anyLong());
        verify(orderRepository).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
        assertThat(actualOrderTable.isEmpty()).isEqualTo(updateEmptyRequest.isEmpty());
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 상태 변경 시 예외 발생 검증")
    public void throwException_WhenTargetOrderTableIsNotExist() {
        // Given
        OrderTable givenOrderTable = 비어있지_않은_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(anyLong(), givenOrderTable));

        verify(orderTableRepository).findById(anyLong());
    }

    @Test
    @DisplayName("단체 지정된 주문 테이블의 사용 가능 상태 변경 시 예외 발생 검증")
    public void throwException_WhenTargetOrderTableIsBindingToTableGroup() {
        // Given
        OrderTable givenOrderTable = 비어있는_주문_테이블_생성();
        TableGroup tableGroup = TableGroup.of(
            Collections.singletonList(givenOrderTable.getId()),
            Collections.singletonList(givenOrderTable)
        );
        givenOrderTable.allocateTableGroup(tableGroup);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(givenOrderTable));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(anyLong(), givenOrderTable));

        verify(orderTableRepository).findById(anyLong());
    }

    @Test
    @DisplayName("조리중이거나 식사중인 주문테이블의 사용 가능 상태 변경 시 예외 발생 검증")
    public void throwException_WhenTargetOrderTableIsMealOrCooking() {
        // Given
        OrderTable givenOrderTable = 비어있지_않은_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(givenOrderTable));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList())).willReturn(true);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeEmpty(anyLong(), givenOrderTable));

        verify(orderTableRepository).findById(anyLong());
        verify(orderRepository).existsByOrderTableIdAndOrderStatusIn(anyLong(), anyList());
    }

    @Test
    @DisplayName("주문 테이블의 객수를 변경한다.")
    public void changeNumberOfGuests() {
        // Given
        OrderTable givenOrderTable = 비어있지_않은_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(givenOrderTable));

        final int newNumberOfGuests = 4;
        OrderTable updateNumberOfGuestsRequest = 비어있지_않은_주문_테이블_생성();
        updateNumberOfGuestsRequest.changeNumberOfGuests(4);

        // When
        OrderTableResponse actualOrderTable = tableService.changeNumberOfGuests(anyLong(), updateNumberOfGuestsRequest);

        // Then
        verify(orderTableRepository).findById(anyLong());
        assertThat(actualOrderTable.getNumberOfGuests()).isEqualTo(newNumberOfGuests);
    }

    @Test
    @DisplayName("존재하지 않는 주문 테이블의 객수 변경 시, 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        OrderTable orderTable = 비어있지_않은_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable));

        verify(orderTableRepository).findById(anyLong());
    }

    @Test
    @DisplayName("비어있는 주문 테이블의 객수 변경 시, 예외 발생 검증")
    public void throwException_WhenOrderTableIsEmpty() {
        // Given
        OrderTable orderTable = 비어있는_주문_테이블_생성();
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(orderTable));

        // When
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> tableService.changeNumberOfGuests(anyLong(), orderTable));

        // Then
        verify(orderTableRepository).findById(anyLong());
    }
}

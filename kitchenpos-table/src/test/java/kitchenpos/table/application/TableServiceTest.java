package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 관련 비즈니스 기능 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private ExistsOrderPort existsOrderPort;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private TableGroup 단체테이블;
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @BeforeEach
    void setUp() {
        단체테이블 = new TableGroup();
        주문테이블1 = new OrderTable(1L, 2, false);
        주문테이블2 = new OrderTable(2L, 3, false);
        주문테이블2 = new OrderTable(3L, 3, false);
    }

    @DisplayName("주문테이블 생성 테스트")
    @Test
    void createTableTest() {
        // given
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(주문테이블1);

        // when
        OrderTableResponse response = tableService.create(
                new OrderTableRequest(주문테이블1.getNumberOfGuests(), 주문테이블1.isEmpty()));

        // then
        assertAll(
                () -> assertThat(주문테이블1.getId()).isEqualTo(response.getId()),
                () -> assertThat(주문테이블1.getNumberOfGuests()).isEqualTo(response.getNumberOfGuests())
        );
    }

    @DisplayName("주문테이블 목록 조회 테스트")
    @Test
    void findAllTablesTest() {
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(주문테이블1, 주문테이블2));

        List<OrderTableResponse> responses = tableService.list();

        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.stream().map(OrderTableResponse::getId).collect(toList()))
                        .containsExactly(주문테이블1.getId(), 주문테이블2.getId())
        );
    }

    @DisplayName("주문 테이블 상태 변경 테스트")
    @Test
    void updateOrderTableEmpty() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(주문테이블1);

        OrderTableRequest request = new OrderTableRequest(0, true);
        OrderTableResponse response = tableService.changeEmpty(주문테이블1.getId(), request);

        assertThat(response.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 등록되지 않은 주문 테이블일 경우")
    @Test
    void updateOrderTableEmpty2() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(
                () -> tableService.changeEmpty(주문테이블1.getId(), new OrderTableRequest(0, true))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 단체지정되있는 테이블일 경우")
    @Test
    void updateOrderTableEmpty3() {
        ReflectionTestUtils.setField(주문테이블1, "empty", true);
        ReflectionTestUtils.setField(주문테이블2, "empty", true);
        ReflectionTestUtils.setField(단체테이블, "id", 1L);

        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        Assertions.assertThatThrownBy(() ->
                tableService.changeEmpty(주문테이블1.getId(), new OrderTableRequest(0, true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 상태 변경 테스트 - 주문 테이블의 상태가 조리또는 식사 중이면 비어있는 상태일 경우")
    @Test
    void updateOrderTableEmpty4() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        Assertions.assertThatThrownBy(() ->
                tableService.changeEmpty(주문테이블1.getId(), new OrderTableRequest(0, true))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트")
    @Test
    void updateOrderTableNumberOfGuest() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(주문테이블1);

        OrderTableResponse response = tableService.changeNumberOfGuests(주문테이블1.getId(),
                new OrderTableRequest(5, false));

        assertThat(response.getNumberOfGuests()).isEqualTo(5);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 0명보다 작은 경우")
    @ParameterizedTest
    @ValueSource(ints = { -1, -3, -7 })
    void updateOrderTableNumberOfGuest2(int numberOfGuest) {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        // when / then
        assertThatIllegalArgumentException().isThrownBy(() -> tableService.changeNumberOfGuests(주문테이블1.getId(),
                new OrderTableRequest(numberOfGuest, false)));
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 등록되지 않은 주문 테이블의 경우")
    @Test
    void updateOrderTableNumberOfGuest3() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(주문테이블1.getId(), new OrderTableRequest(5, false))
        ).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("주문 테이블 손님수 변경 테스트 - 빈 주문 테이블의 경우")
    @Test
    void updateOrderTableNumberOfGuest4() {
        when(orderTableRepository.findById(주문테이블1.getId())).thenReturn(Optional.of(주문테이블1));

        Assertions.assertThatThrownBy(() ->
                tableService.changeNumberOfGuests(주문테이블1.getId(), new OrderTableRequest(5, false))
        ).isInstanceOf(IllegalArgumentException.class);
    }
}

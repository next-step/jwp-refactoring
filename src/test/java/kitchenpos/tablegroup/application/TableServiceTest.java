package kitchenpos.tablegroup.application;

import kitchenpos.exception.EntityNotFoundException;
import kitchenpos.tablegroup.domain.*;
import kitchenpos.tablegroup.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.OrderTableResponse;
import kitchenpos.tablegroup.exception.OrderTableExceptionCode;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@DisplayName("TableService 테스트")
@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private TableValidator tableValidator;

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
        주문테이블1 = new OrderTable(2, false);
        주문테이블2 = new OrderTable(3, false);

        ReflectionTestUtils.setField(단체테이블, "id", 1L);
        ReflectionTestUtils.setField(주문테이블1, "id", 1L);
        ReflectionTestUtils.setField(주문테이블2, "id", 2L);
    }

    @ParameterizedTest
    @ValueSource(ints = { -1, -5, -10, -15 })
    void 방문한_손님_수가_음수이면_주문_테이블을_생성할_수_없음(int numberOfGuests) {
        assertThatThrownBy(() -> {
            tableService.create(new OrderTableRequest(numberOfGuests, 주문테이블1.isEmpty()));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @Test
    void 주문_테이블_생성() {
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블1);

        OrderTableResponse response = tableService.create(
                new OrderTableRequest(주문테이블1.getNumberOfGuests(), 주문테이블1.isEmpty()));

        assertThat(response).satisfies(res -> {
            assertEquals(주문테이블1.getId(), res.getId());
            assertEquals(주문테이블1.getNumberOfGuests(), res.getNumberOfGuests());
        });
    }

    @Test
    void 주문_테이블_목록_조회() {
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(주문테이블1, 주문테이블2));

        List<OrderTableResponse> responses = tableService.list();

        assertAll(
                () -> assertThat(responses).hasSize(2),
                () -> assertThat(responses.stream().map(OrderTableResponse::getId).collect(toList()))
                        .containsExactly(주문테이블1.getId(), 주문테이블2.getId())
        );
    }

    @Test
    void 주문_테이블이_등록되어_있지_않으면_빈_주문_테이블로_변경할_수_없음() {
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeEmpty(주문테이블1.getId(), new TableEmpty(true));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 주문_테이블을_빈_테이블로_변경() {
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));
        willDoNothing().given(tableValidator).validateToChangeEmpty(any(OrderTable.class));
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블1);

        OrderTableResponse response = tableService.changeEmpty(주문테이블1.getId(), new TableEmpty(true));

        assertTrue(response.isEmpty());
    }

    @Test
    void 주문_테이블이_등록되어_있지_않으면_방문한_손님_수를_변경할_수_없음() {
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), new TableGuests(5));
        }).isInstanceOf(EntityNotFoundException.class)
                .hasMessage(OrderTableExceptionCode.NOT_FOUND_BY_ID.getMessage());
    }

    @Test
    void 주문_테이블이_빈_테이블이면_방문한_손님_수를_변경할_수_없음() {
        주문테이블1.changeEmpty(true);
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));

        assertThatThrownBy(() -> {
            tableService.changeNumberOfGuests(주문테이블1.getId(), new TableGuests(5));
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(OrderTableExceptionCode.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
    }

    @Test
    void 주문_테이블의_방문한_손님_수를_변경() {
        given(orderTableRepository.findById(주문테이블1.getId())).willReturn(Optional.of(주문테이블1));
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(주문테이블1);

        OrderTableResponse response = tableService.changeNumberOfGuests(주문테이블1.getId(), new TableGuests(5));

        assertEquals(5, response.getNumberOfGuests());
    }
}
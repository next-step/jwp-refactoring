package kitchenpos.table.application;

import kitchenpos.exception.ErrorMessage;
import kitchenpos.table.domain.*;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@DisplayName("주문 테이블 비즈니스 테스트")
@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private TableService tableService;

    private TableGroup 우아한형제들_단체그룹;
    private OrderTable 첫번째_주문_테이블;
    private OrderTable 두번째_주문_테이블;

    @BeforeEach
    void setUp() {
        우아한형제들_단체그룹 = new TableGroup();
        첫번째_주문_테이블 = new OrderTable(2, false);
        두번째_주문_테이블 = new OrderTable(3, false);

        ReflectionTestUtils.setField(우아한형제들_단체그룹, "id", 1L);
        ReflectionTestUtils.setField(첫번째_주문_테이블, "id", 1L);
        ReflectionTestUtils.setField(두번째_주문_테이블, "id", 2L);
    }

    @DisplayName("주문 테이블을 생성할 수 있다.")
    @Test
    void 주문_테이블을_생성할_수_있다() {
        // given
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(첫번째_주문_테이블);

        // when
        OrderTableResponse orderTableResponse = tableService.create(new OrderTableRequest(첫번째_주문_테이블.getNumberOfGuests(), 첫번째_주문_테이블.isEmpty()));

        // then
        assertThat(orderTableResponse).satisfies(response -> {
            assertEquals(response.getId(), 첫번째_주문_테이블.getId());
            assertEquals(response.getNumberOfGuests(), 첫번째_주문_테이블.getNumberOfGuests());
        });
    }

    @DisplayName("주문 테이블 목록을 조회할 수 있다.")
    @Test
    void 주문_테이블_목록을_조회할_수_있다() {
        // given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        // when
        List<OrderTableResponse> responses = tableService.list();

        // then
        assertAll(() -> {
            assertThat(responses).hasSize(2);
            assertThat(responses.stream().map(OrderTableResponse::getId).collect(toList()))
                    .containsExactly(첫번째_주문_테이블.getId(), 두번째_주문_테이블.getId());
        });
    }

    @DisplayName("주문 테이블은 반드시 등록되어 있어야 한다.")
    @Test
    void 주문_테이블은_반드시_등록되어_있어야_한다() {
        // given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), new OrderEmpty(true)))
                .withMessage(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 테이블의 방문한 손님 수가 0명 이상이어야 한다.")
    @Test
    void 주문_테이블의_방문한_손님_수가_0명_이상이어야_한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.create(new OrderTableRequest(-1, 첫번째_주문_테이블.isEmpty())))
                .withMessage(ErrorMessage.ORDER_TABLE_INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @DisplayName("주문 테이블은 등록되어 있어야 한다.")
    @Test
    void 주문_테이블은_등록되어_있어야_한다() {
        // given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), new OrderGuests(2)))
                .withMessage(ErrorMessage.ORDER_TABLE_NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 테이블이 빈 테이블이면 손님 수를 변경할 수 없다.")
    @Test
    void 주문_테이블이_빈_테이블이면_손님_수를_변경할_수_없다() {
        // given
        첫번째_주문_테이블.changeEmpty(true);
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), new OrderGuests(2)))
                .withMessage(ErrorMessage.ORDER_TABLE_NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
    }


    @DisplayName("주문 테이블에 방문한 손님 수를 변경할 수 있다.")
    @Test
    void 주문_테이블에_방문한_손님_수를_변경할_수_있다() {
        // given
        when(orderTableRepository.findById(첫번째_주문_테이블.getId())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderTableRepository.save(첫번째_주문_테이블)).thenReturn(첫번째_주문_테이블);

        // when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), new OrderGuests(2));

        // then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(2);
    }
}

package kitchenpos.order.application;

import kitchenpos.exception.OrderErrorMessage;
import kitchenpos.exception.OrderTableErrorMessage;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;
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
    private OrderRepository orderRepository;

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
                .withMessage(OrderTableErrorMessage.NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 테이블은 단체 지정이 되어 있지 않아야 한다.")
    @Test
    void 주문_테이블은_단체_지정이_되어_있지_않아야_한다() {
        // given
        ReflectionTestUtils.setField(첫번째_주문_테이블, "empty", new OrderEmpty(true));
        ReflectionTestUtils.setField(두번째_주문_테이블, "empty", new OrderEmpty(true));
        우아한형제들_단체그룹.group(Arrays.asList(첫번째_주문_테이블, 두번째_주문_테이블));

        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), new OrderEmpty(true)))
                .withMessage(OrderTableErrorMessage.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("주문 테이블의 주문 상태는 조리 중이거나 식사 중이면 안된다.")
    @Test
    void 주문_테이블의_주문_상태는_조리_중이거나_식사_중이면_안된다() {
        // given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(Arrays.asList(new Order(첫번째_주문_테이블, OrderStatus.COOKING)));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeEmpty(첫번째_주문_테이블.getId(), new OrderEmpty(true)))
                .withMessage(OrderErrorMessage.CANNOT_BE_CHANGED.getMessage());
    }

    @DisplayName("주문 테이블이 비어있는지 여부를 변경할 수 있다.")
    @Test
    void 주문_테이블이_비어있는지_여부를_변경할_수_있다() {
        // given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.of(첫번째_주문_테이블));
        when(orderRepository.findAllByOrderTableId(any())).thenReturn(Arrays.asList(new Order(첫번째_주문_테이블, OrderStatus.COMPLETION)));
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(첫번째_주문_테이블);

        // when
        OrderTableResponse orderTableResponse = tableService.changeEmpty(첫번째_주문_테이블.getId(), new OrderEmpty(true));

        // then
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @DisplayName("주문 테이블의 방문한 손님 수가 0명 이상이어야 한다.")
    @Test
    void 주문_테이블의_방문한_손님_수가_0명_이상이어야_한다() {
        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.create(new OrderTableRequest(-1, 첫번째_주문_테이블.isEmpty())))
                .withMessage(OrderTableErrorMessage.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @DisplayName("주문 테이블은 등록되어 있어야 한다.")
    @Test
    void 주문_테이블은_등록되어_있어야_한다() {
        // given
        when(orderTableRepository.findById(anyLong())).thenReturn(Optional.empty());

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), new OrderGuests(2)))
                .withMessage(OrderTableErrorMessage.NOT_FOUND_BY_ID.getMessage());
    }

    @DisplayName("주문 테이블이 빈 테이블이면 손님 수를 변경할 수 없다.")
    @Test
    void 주문_테이블이_빈_테이블이면_손님_수를_변경할_수_없다() {
        // given
        첫번째_주문_테이블.changeEmpty(true, Collections.emptyList());
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(첫번째_주문_테이블));

        // when, then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableService.changeNumberOfGuests(첫번째_주문_테이블.getId(), new OrderGuests(2)))
                .withMessage(OrderTableErrorMessage.NUMBER_OF_GUESTS_CANNOT_BE_CHANGED.getMessage());
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

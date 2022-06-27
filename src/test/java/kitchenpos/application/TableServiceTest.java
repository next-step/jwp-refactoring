package kitchenpos.application;

import static kitchenpos.fixture.DomainFactory.createOrderTable;
import static kitchenpos.fixture.OrderTableFactory.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.OrderTableRepository;
import kitchenpos.dto.OrderTableRequest;
import kitchenpos.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;
    private OrderTable orderTable;
    private OrderTableRequest orderTableRequest;

    @BeforeEach
    void setUp() {
        orderTable = createOrderTable(1L, null, 5, false);
        orderTableRequest = createOrderTableRequest(5, false);
    }

    @Test
    void 테이블_생성() {
        // given
        given(orderTableRepository.save(any(OrderTable.class))).willReturn(orderTable);

        // when
        OrderTableResponse result = tableService.create(orderTableRequest);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 테이블_목록_조회() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTable));

        // when
        List<OrderTableResponse> result = tableService.list();

        // then
        assertThat(toIdList(result)).containsExactlyElementsOf(Arrays.asList(orderTable.getId()));
    }

    @Test
    void 빈_상태_변경_테이블_없음_예외() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경_단체_지정_테이블_예외() {
        // given
        orderTable.groupByTableGroupId(1L);
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.ofNullable(orderTable));

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경_주문_상태_계산완료_아닌경우_예외() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(true);

        // when, then
        assertThatThrownBy(
                () -> tableService.changeEmpty(orderTable.getId(), orderTableRequest)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 빈_상태_변경() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.ofNullable(orderTable));
        given(orderDao.existsByOrderTableIdAndOrderStatusIn(orderTable.getId(),
                Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()))).willReturn(false);
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTableResponse result = tableService.changeEmpty(orderTable.getId(), orderTableRequest);

        // then
        assertThat(result.isEmpty()).isEqualTo(orderTable.isEmpty());
    }

    @Test
    void 손님_수_변경_0_미만_예외() {
        // given
        OrderTableRequest 손님_수_음수 = createOrderTableRequest(-1, false);

        // when, then
        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), 손님_수_음수))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경_존재하지_않는_테이블_예외() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willThrow(IllegalArgumentException.class);

        // when, then
        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경_빈_테이블_예외() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.ofNullable(orderTable));
        orderTable.changeEmpty(true);

        // when, then
        assertThatThrownBy(
                () -> assertThat(tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest))
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 손님_수_변경() {
        // given
        given(orderTableRepository.findById(orderTable.getId())).willReturn(Optional.ofNullable(orderTable));
        given(orderTableRepository.save(orderTable)).willReturn(orderTable);

        // when
        OrderTableResponse result = tableService.changeNumberOfGuests(orderTable.getId(), orderTableRequest);

        assertThat(result.getNumberOfGuests()).isEqualTo(orderTable.getNumberOfGuests().getValue());
    }

    private List<Long> toIdList(List<OrderTableResponse> OrderTableResponses) {
        return OrderTableResponses.stream()
                .map(OrderTableResponse::getId)
                .collect(Collectors.toList());
    }
}

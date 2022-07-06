package kitchenpos.table.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import kitchenpos.application.table.TableService;
import kitchenpos.common.order.domain.OrderStatus;
import kitchenpos.common.order.repository.OrderRepository;
import kitchenpos.common.table.domain.NumberOfGuests;
import kitchenpos.common.table.domain.OrderTable;
import kitchenpos.common.table.dto.OrderTableRequest;
import kitchenpos.common.table.dto.OrderTableResponse;
import kitchenpos.common.table.repository.OrderTableRepository;
import kitchenpos.fixture.UnitTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableService tableService;

    private UnitTestFixture 식당_포스;

    @BeforeEach
    void setUp() {
        식당_포스 = new UnitTestFixture();
    }

    @Test
    void 주문_테이블을_등록할_수_있어야_한다() {
        // given
        final OrderTableRequest given = new OrderTableRequest(new NumberOfGuests(2), false);
        final OrderTable expected = new OrderTable(1L, null, new NumberOfGuests(2), false);
        when(orderTableRepository.save(any(OrderTable.class))).thenReturn(expected);

        // when
        final OrderTableResponse actual = tableService.create(given);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
    }

    @Test
    void 주문_테이블_목록을_조회할_수_있어야_한다() {
        // given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(식당_포스.테이블, 식당_포스.빈_테이블1));

        // when
        final List<OrderTableResponse> actual = tableService.list();

        // then
        assertThat(actual.stream().map(OrderTableResponse::getId).collect(Collectors.toList()))
                .containsExactly(식당_포스.테이블.getId(), 식당_포스.빈_테이블1.getId());
    }

    @Test
    void 주문_테이블이_비었는지_여부를_변경할_수_있어야_한다() {
        // given
        when(orderTableRepository.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(식당_포스.테이블.getId(), OrderStatus.notCompletes()))
                .thenReturn(false);

        // when
        tableService.changeEmpty(식당_포스.테이블.getId(), new OrderTableRequest(true));

        // then
        assertThat(식당_포스.테이블.isEmpty()).isTrue();
    }

    @Test
    void 비었는지_여부_변경_시_주문_테이블이_단체_등록_테이블이면_에러가_발생해야_한다() {
        // given
        when(orderTableRepository.findById(식당_포스.단체_지정_테이블1.getId())).thenReturn(Optional.of(식당_포스.단체_지정_테이블1));

        // when and then
        assertThatThrownBy(() -> tableService.changeEmpty(식당_포스.단체_지정_테이블1.getId(), new OrderTableRequest(true)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 비었는지_여부_변경_시_주문_테이블의_주문이_결제완료_상태가_아니면_에러가_발생해야_한다() {
        // given
        when(orderTableRepository.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));
        when(orderRepository.existsByOrderTableIdAndOrderStatusIn(식당_포스.테이블.getId(), OrderStatus.notCompletes()))
                .thenReturn(true);

        // when and then
        assertThatThrownBy(() -> tableService.changeEmpty(식당_포스.테이블.getId(), new OrderTableRequest(true)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 주문_테이블의_손님_수를_변경할_수_있어야_한다() {
        // given
        when(orderTableRepository.findById(식당_포스.테이블.getId())).thenReturn(Optional.of(식당_포스.테이블));

        // when
        tableService.changeNumberOfGuests(식당_포스.테이블.getId(), new OrderTableRequest(new NumberOfGuests(1)));

        // then
        assertThat(식당_포스.테이블.getNumberOfGuests().value()).isEqualTo(1);
    }

    @Test
    void 손님_수_변경_시_주문_테이블의_빈_테이블이면_에러가_발생해야_한다() {
        // given
        when(orderTableRepository.findById(식당_포스.빈_테이블1.getId())).thenReturn(Optional.of(식당_포스.빈_테이블1));

        // when and then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(
                식당_포스.빈_테이블1.getId(), new OrderTableRequest(new NumberOfGuests(1))))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void 아이디로_주문_테이블을_조회할_수_있어야_한다() {
        // given
        final OrderTable given = new OrderTable(1L, null, new NumberOfGuests(4), false);
        when(orderTableRepository.findById(given.getId())).thenReturn(Optional.of(given));

        // when and then
        assertThat(tableService.getById(given.getId())).isEqualTo(given);
    }

    @Test
    void 테이블_그룹_아이디로_주문_테이블들을_조회할_수_있어야_한다() {
        // given
        when(orderTableRepository.findAllByTableGroupId(식당_포스.단체.getId()))
                .thenReturn(Arrays.asList(식당_포스.단체_지정_테이블1, 식당_포스.단체_지정_테이블2));

        // when and then
        assertThat(tableService.getAllByTableGroupId(식당_포스.단체.getId()))
                .containsExactly(식당_포스.단체_지정_테이블1, 식당_포스.단체_지정_테이블2);
    }
}

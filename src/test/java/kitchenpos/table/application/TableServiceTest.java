package kitchenpos.table.application;

import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.application.TableService;
import kitchenpos.table.repository.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
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

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable 주문_테이블;

    @BeforeEach
    void setUp() {
        주문_테이블 = new OrderTable(1L, null, 0, true);
    }

    @DisplayName("테이블 생성")
    @Test
    void create() {
        // given
        OrderTableRequest request = new OrderTableRequest(0, true);
        given(orderTableRepository.save(any())).willReturn(주문_테이블);

        // when
        OrderTableResponse response = tableService.create(request);

        // then
        assertThat(response).isNotNull();
    }

    @DisplayName("테이블 목록 조회")
    @Test
    void list() {
        // given
        given(orderTableRepository.findAll()).willReturn(Collections.singletonList(주문_테이블));

        // when
        List<OrderTableResponse> responses = tableService.list();

        // then
        assertThat(responses).hasSize(1);
    }

    @DisplayName("빈 테이블로 변경")
    @Test
    void changeEmpty() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(false);

        // when
        OrderTableResponse response = tableService.changeEmpty(1L, true);

        // then
        assertThat(response.isEmpty()).isTrue();
    }

    @Test
    void 존재하지않는_테이블의_상태를_변경할_경우() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 단체_지정된_주문_테이블의_상태를_변경할_경우() {
        // given
        주문_테이블 = new OrderTable(1L, new TableGroup(1L), 3, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 조리_또는_식사중인_상태의_테이블을_변경하는_경우() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(any(), anyList())).willReturn(true);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("인원 수 변경")
    @Test
    void changeNumberOfGuests() {
        // given
        주문_테이블 = new OrderTable(1L, null, 3, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));

        // when
        OrderTableResponse response = tableService.changeNumberOfGuests(1L, 0);

        // then
        assertThat(response.getNumberOfGuests()).isEqualTo(0);
    }

    @Test
    void 손님수를_0미만으로_변경할_경우() {
        // given
        주문_테이블 = new OrderTable(1L, null, 3, false);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, -1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 존재하지_않는_테이블의_손님_수를_변경할_경우() {
        // given
        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 0))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 비어있는_테이블의_손님_수를_변경할_경우() {
        // given
        주문_테이블 = new OrderTable(1L, null, 0, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(주문_테이블));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(1L, 0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

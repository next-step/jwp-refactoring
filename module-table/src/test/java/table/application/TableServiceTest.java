package table.application;

import table.domain.OrderTable;
import table.dto.OrderTableRequest;
import table.dto.OrderTableResponse;
import table.repository.OrderTableRepository;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private TableValidator tableValidator;

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

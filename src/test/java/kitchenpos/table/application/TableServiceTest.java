package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.ChangeEmptyRequest;
import kitchenpos.table.dto.ChangeNumberOfGuestsRequest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.fixture.TableFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private TableValidator tableValidator;

    private OrderTable 테이블_그룹에_속해있지_않은_테이블;
    private OrderTable 비어있지_않은_테이블;

    @BeforeEach
    public void setUp() {
        테이블_그룹에_속해있지_않은_테이블 = TableFixture.create(1L, null, 0, true);
        비어있지_않은_테이블 = TableFixture.create(2L, null, 0, false);
    }

    @DisplayName("테이블 생성 성공 테스트")
    @Test
    void create() {
        // given
        OrderTableRequest 요청_테이블 = OrderTableRequest.of(0, true);

        given(orderTableRepository.save(any(OrderTable.class))).willReturn(테이블_그룹에_속해있지_않은_테이블);

        // when
        OrderTableResponse 생성된_테이블 = tableService.create(요청_테이블);

        // then
        assertThat(생성된_테이블).isEqualTo(OrderTableResponse.of(테이블_그룹에_속해있지_않은_테이블));
    }

    @DisplayName("테이블 목록 조회 성공 테스트")
    @Test
    void list() {
        // given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(테이블_그룹에_속해있지_않은_테이블));

        // when
        List<OrderTableResponse> 조회된_테이블_목록 = tableService.list();

        // then
        assertThat(조회된_테이블_목록).containsExactly(OrderTableResponse.of(테이블_그룹에_속해있지_않은_테이블));
    }

    @DisplayName("기존 주문 테이블 수정 성공 테스트")
    @Test
    void changeEmpty() {
        // given
        ChangeEmptyRequest 요청_테이블 = ChangeEmptyRequest.of(false);

        given(orderTableRepository.findById(any(Long.class))).willReturn(Optional.of(테이블_그룹에_속해있지_않은_테이블));

        // when
        OrderTableResponse 수정된_테이블 = tableService.changeEmpty(테이블_그룹에_속해있지_않은_테이블.getId(), 요청_테이블);

        // then
        assertThat(수정된_테이블.isEmpty()).isFalse();
    }

    @DisplayName("주문 테이블 손님 수 수정 성공 테스트")
    @Test
    void changeNumberOfGuests() {
        // given
        ChangeNumberOfGuestsRequest 요청_테이블 = ChangeNumberOfGuestsRequest.of(4);

        given(orderTableRepository.findById(any(Long.class))).willReturn(Optional.of(비어있지_않은_테이블));

        // when
        OrderTableResponse 수정된_테이블 = tableService.changeNumberOfGuests(비어있지_않은_테이블.getId(), 요청_테이블);

        // then
        assertThat(수정된_테이블.getNumberOfGuests()).isEqualTo(요청_테이블.getNumberOfGuests());
    }
}

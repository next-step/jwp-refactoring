package kitchenpos.table;

import static kitchenpos.table.TableFixture.createOrderTableRequest;
import static kitchenpos.table.TableFixture.빈테이블;
import static kitchenpos.table.TableFixture.일번테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.dao.OrderTableRepository;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.application.TableService;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private TableService tableService;

    @Test
    @DisplayName("테이블 생성")
    void createTable() {
        //given
        when(orderTableRepository.save(any())).thenReturn(일번테이블);
        OrderTableRequest orderTableRequest = createOrderTableRequest(일번테이블);

        //when
        OrderTableResponse orderTableResponse = tableService.create(orderTableRequest);

        //then
        assertThat(orderTableResponse).isEqualTo(OrderTableResponse.from(일번테이블));
    }

    @Test
    @DisplayName("테이블 목록 조회")
    void getList() {
        //given
        when(orderTableRepository.findAll()).thenReturn(Arrays.asList(일번테이블, 빈테이블));

        //when
        List<OrderTableResponse> list = tableService.list();

        //then
        assertThat(list)
            .hasSize(2)
            .containsExactly(
                OrderTableResponse.from(일번테이블),
                OrderTableResponse.from(빈테이블)
            );
    }

    @Test
    @DisplayName("테이블 비우기")
    void emptyTable() {
        //given
        OrderTable 일번테이블 = new OrderTable(1L, 0, false, Collections.emptyList());
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(일번테이블));
        when(orderTableRepository.save(any())).then(returnsFirstArg());

        //when
        OrderTableResponse orderTableResponse = tableService.changeEmpty(1L, true);

        //then
        assertThat(orderTableResponse.isEmpty()).isTrue();
    }

    @Test
    void 변경할_테이블이_존재하지_않으면__변경_불가능() {
        //given
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(() -> tableService.changeEmpty(1L, true))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경하고자 하는 테이블 정보가 없습니다.");
    }

    @Test
    @DisplayName("테이블 손님 수 변경")
    void changeNumberOfGuest() {
        //given
        OrderTable 일번테이블 = new OrderTable(1L, 0, false, Collections.emptyList());
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(일번테이블));
        when(orderTableRepository.save(any())).then(returnsFirstArg());

        //when
        OrderTableResponse orderTableResponse = tableService.changeNumberOfGuests(1L, 5);

        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(5);
    }

    @Test
    void 방문한_손님수를_변경할_주문_테이블이_없으면_변경_불가능() {
        //given
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        //when
        assertThatThrownBy(
            () -> tableService.changeNumberOfGuests(1L, 0))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("변경하고자 하는 테이블 정보가 없습니다.");
    }

}

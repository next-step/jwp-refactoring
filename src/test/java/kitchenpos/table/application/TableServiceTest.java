package kitchenpos.table.application;

import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
class TableServiceTest {
    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderTableService orderTableService;

    private OrderTableRequest orderTableRequest;

    @BeforeEach
    public void setUp() {
        orderTableRequest = new OrderTableRequest(0, true);
    }

    @DisplayName("주문테이블을 등록할 수 있다.")
    @Test
    void createTest() {
        //given
        given(orderTableRepository.save(any())).willReturn(orderTableRequest.toEntity());

        //when
        OrderTableResponse orderTableResponse = orderTableService.createTemp(orderTableRequest);

        //then
        assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(orderTableRequest.getNumberOfGuests());
        assertThat(orderTableResponse.isEmpty()).isEqualTo(orderTableRequest.isEmpty());
    }

    @DisplayName("주문테이블을 조회할 수 있다.")
    @Test
    void listTest() {
        //given
        given(orderTableRepository.findAll()).willReturn(Arrays.asList(orderTableRequest.toEntity()));

        //when
        List<OrderTableResponse> orderTableResponses = orderTableService.listTemp();

        //then
        assertThat(orderTableResponses.size()).isGreaterThan(0);
    }


}
package order.application;

import static order.fixture.OrderFixture.주문_요청_데이터_생성;
import static order.fixture.OrderLineItemFixture.주문항목_요청_데이터_생성;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import order.dto.OrderLineItemRequestDto;
import order.dto.OrderRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import table.domain.OrderTable;
import table.repository.OrderTableRepository;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(orderTableRepository);
    }

    @DisplayName("주문테이블이 존재하지 않으면 생성할 수 없다.")
    @Test
    void create_fail_notExistsOrderTable() {
        //given
        List<OrderLineItemRequestDto> orderLineItemRequests = Arrays.asList(주문항목_요청_데이터_생성(1L, 1));
        OrderRequestDto request = 주문_요청_데이터_생성(orderLineItemRequests);

        given(orderTableRepository.findById(any())).willReturn(Optional.empty());

        //when //then
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> orderValidator.checkCreatable(request));
    }

    @DisplayName("주문테이블이 빈테이블이면 생성할 수 없다.")
    @Test
    void create_fail_emptyOrderTable() {
        //given
        List<OrderLineItemRequestDto> orderLineItemRequests = Arrays.asList(주문항목_요청_데이터_생성(1L, 1));
        OrderRequestDto request = 주문_요청_데이터_생성(orderLineItemRequests);

        OrderTable orderTable = new OrderTable(1L, null, 4, true);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(orderTable));

        //when //then
        assertThatIllegalArgumentException()
            .isThrownBy(() -> orderValidator.checkCreatable(request))
            .withMessage("orderTable must be notEmpty");
    }

}
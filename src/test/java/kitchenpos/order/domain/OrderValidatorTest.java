package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableFactory;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderValidator orderValidator;

    @DisplayName("주문테이블이 비어 있으면 주문을 생성할 수 없다.")
    @Test
    void validOrderTable() {
        //given
        OrderTable orderTable = OrderTableFactory.create(1L, null, 0, true);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 에러가 발생한다.")
    @Test
    void createOrderTable() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

}

package api.order.application;

import api.exception.menu.MenuNotFoundException;
import api.exception.order.OrderLineItemNotFoundException;
import api.exception.table.InvalidTableException;
import api.exception.table.OrderTableNotFoundException;
import api.menu.domain.MenuRepository;
import api.order.domain.Order;
import api.order.domain.OrderValidator;
import api.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static domain.order.OrderFixtures.주문;
import static domain.table.OrderTableFixtures.주문가능_다섯명테이블;
import static domain.table.OrderTableFixtures.주문불가_다섯명테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.order.application
 * fileName : OrderValidatorTest
 * author : haedoang
 * date : 2021/12/26
 * description :
 */
@DisplayName("주문 Validator 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;


    @Test
    @DisplayName("테이블정보가 등록되어 있지 않은 경우 등록할 수 없다.")
    public void createFailByUnknownTable() {
        // then
        assertThatThrownBy(() -> orderValidator.validate(주문())).isInstanceOf(OrderTableNotFoundException.class);
    }

    @Test
    @DisplayName("주문정보가 있지 않은 경우 등록할 수 없다.")
    public void createFailByNotExistOrderLineItem() throws Exception {
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문가능_다섯명테이블()));

        // then
        assertThatThrownBy(() -> orderValidator.validate(new Order(1L, Lists.emptyList()))).isInstanceOf(OrderLineItemNotFoundException.class);
    }

    @Test
    @DisplayName("주문정보가 등록되어 있지 않은 경우 등록할 수 없다.")
    public void createFailByUnknownOrderLineItem() {
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문가능_다섯명테이블()));
        given(menuRepository.findAllById(anyList())).willReturn(Lists.emptyList());
        // then
        assertThatThrownBy(() -> orderValidator.validate(주문())).isInstanceOf(MenuNotFoundException.class);
    }


    @Test
    @DisplayName("빈 테이블인 경우 등록할 수 없다.")
    public void createFailByEmptyTable() {
        //given
        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문불가_다섯명테이블()));
        given(menuRepository.findAllById(anyList())).willReturn(Lists.emptyList());

        // then
        assertThatThrownBy(() -> orderValidator.validate(주문())).isInstanceOf(InvalidTableException.class);
    }
}

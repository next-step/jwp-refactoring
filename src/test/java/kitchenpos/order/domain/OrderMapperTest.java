package kitchenpos.order.domain;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.service.OrderMapper;
import kitchenpos.order.domain.service.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class OrderMapperTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    private OrderValidator orderValidator;

    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    public void setUp() {
        orderValidator = new OrderValidator(menuRepository);
    }

    @DisplayName("주문 등록 예외 - 주문테이블이 없는 경우")
    @Test
    public void 주문테이블이없는경우_주문_등록_예외() throws Exception {
        //given
        OrderMapper orderMapper = new OrderMapper(orderTableRepository, orderValidator);

        //when
        //then
        assertThatThrownBy(() -> orderMapper.mapToFrom(new OrderRequest(-1L,
                Arrays.asList(new OrderLineItemRequest(1L, 1L)))))
                .hasMessage("주문테이블이 존재하지 않습니다.")
                .isInstanceOf(IllegalArgumentException.class);
    }
}

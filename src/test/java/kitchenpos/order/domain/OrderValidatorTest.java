package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableFactory;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
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
    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    OrderValidator orderValidator;

    OrderTable 빈테이블;
    OrderTable 한명테이블;

    @BeforeEach
    void setUp() {
        빈테이블 = OrderTableFactory.create(1L, null, 0, true);
        한명테이블 = OrderTableFactory.create(1L, null, 1, false);
    }

    @DisplayName("주문테이블이 비어 있으면 주문을 생성할 수 없다.")
    @Test
    void validOrderTable() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(빈테이블));
        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블이 존재하지 않으면 에러가 발생한다.")
    @Test
    void createOrderTable() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(빈테이블));

        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성 요청시 주문 항목이 없으면 에러가 발생한다.")
    @Test
    void createOrderLineItems() {
        //given
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(한명테이블));

        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L, Collections.emptyList()))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 등록되지 않은 메뉴가 있으면 에러가 발생한다.")
    @Test
    void createOrderItemCount() {
        //given
        OrderLineItem orderLineItem = new OrderLineItem(1L, null, 1L, 10L);
        given(menuRepository.findById(any())).willReturn(Optional.empty());
        given(orderTableRepository.findById(any())).willReturn(Optional.of(한명테이블));

        //when & then
        assertThatThrownBy(() -> orderValidator.validCreate(1L, Collections.singletonList(orderLineItem)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

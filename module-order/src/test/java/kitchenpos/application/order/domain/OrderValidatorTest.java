package kitchenpos.application.order.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private Menu 메뉴;
    private MenuGroup 메뉴그룹;
    private OrderTable 주문_테이블;

    private OrderRequest 주문_요청;

    @BeforeEach
    void set_up() {
        Product 상품_후라이드 = new Product("상품_후라이드", BigDecimal.valueOf(1000));
        Product 상품_양념치킨 = new Product("상품_양념치킨", BigDecimal.valueOf(1000));

        주문_테이블 = new OrderTable(4, false);

        메뉴그룹 = new MenuGroup("메뉴그룹");
        메뉴 = new Menu("메뉴", BigDecimal.valueOf(1000), 메뉴그룹);

        주문_요청 = new OrderRequest(1L, Arrays.asList(new OrderLineItemRequest(1L, 1)));
    }

    @DisplayName("주문 항목이 없으면 주문할 수 없다.")
    @Test
    void create_error_without_order_item() {
        // given
        when(orderTableRepository.findById(any())).thenThrow(IllegalArgumentException.class);
        주문_요청 = new OrderRequest(1L, new ArrayList<>());

        // when && then
        assertThatThrownBy(() -> orderValidator.validate(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 값이 비어있다면 주문 할 수 없다.")
    @Test
    void create_error_order_table_value_empty() {
        // given
        주문_요청 = new OrderRequest(null, Arrays.asList(new OrderLineItemRequest(1L, 1)));

        // when && then
        assertThatThrownBy(() -> orderValidator.validate(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 비어 있다면 주문할 수 없다.")
    @Test
    void create_error_order_table_empty() {
        // given
        OrderTable 비어있는_주문_테이블 = new OrderTable(5, true);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있는_주문_테이블));

        // when && then
        assertThatThrownBy(() -> orderValidator.validate(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("주문 항목의 메뉴의 개수가 일치하지 않으면 주문 할 수 없다.")
    @Test
    void create_error_duplicated_order_item() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(메뉴, 메뉴));

        // when && then
        assertThatThrownBy(() -> orderValidator.validate(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

}

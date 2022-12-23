package kitchenpos.order.application;

import static kitchenpos.menu.domain.MenuFixture.*;
import static kitchenpos.menu.domain.OrderMenuFixture.*;
import static kitchenpos.order.domain.OrderLineItemFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menu.domain.OrderMenuRepository;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.dto.OrderLineItemRequest;

@DisplayName("주문 항목 생성 테스트")
@ExtendWith(MockitoExtension.class)
class OrderLineItemGeneratorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderMenuRepository orderMenuRepository;

    @InjectMocks
    private OrderLineItemGenerator orderLineItemGenerator;

    @DisplayName("등록 되어 있지 않은 주문 항목 메뉴")
    @Test
    void generate_not_exists() {
        // given
        List<OrderLineItemRequest> orderLineItems = Arrays.asList(
            orderLineItemRequest(1L, 1L),
            orderLineItemRequest(2L, 2L)
        );
        given(menuRepository.findAllById(anyList())).willReturn(Collections.singletonList(
            savedMenu(1L, "메뉴", BigDecimal.valueOf(13000))
        ));
        given(orderMenuRepository.saveAll(anyList())).willReturn(Collections.singletonList(
            savedOrderMenu(1L, 1L, "메뉴", BigDecimal.valueOf(13000))
        ));

        // when, then
        assertThatThrownBy(() -> orderLineItemGenerator.generate(orderLineItems))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 메뉴 생성")
    @Test
    void generate() {
        // given
        Long menuId = 1L;
        String menuName = "메뉴";
        BigDecimal menuPrice = BigDecimal.valueOf(13000);
        long quantity = 2L;
        List<OrderLineItemRequest> orderLineItems = Collections.singletonList(orderLineItemRequest(menuId, quantity));
        given(menuRepository.findAllById(anyList())).willReturn(Collections.singletonList(
            savedMenu(menuId, menuName, menuPrice)
        ));

        given(orderMenuRepository.saveAll(anyList())).willReturn(Collections.singletonList(
            savedOrderMenu(1L, menuId, menuName, menuPrice)
        ));

        // when
        List<OrderLineItem> actual = orderLineItemGenerator.generate(orderLineItems);

        // then
        assertAll(
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual.get(0).getOrderMenuId()).isEqualTo(menuId),
            () -> assertThat(actual.get(0).getQuantity()).isEqualTo(quantity)
        );
    }
}

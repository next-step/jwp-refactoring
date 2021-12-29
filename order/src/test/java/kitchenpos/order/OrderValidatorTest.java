package kitchenpos.order;

import kitchenpos.order.application.OrderMenuService;
import kitchenpos.order.application.OrderMenuServiceImpl;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.MenuAdapter;
import kitchenpos.order.domain.Order;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class OrderValidatorTest {

    @MockBean
    private OrderMenuServiceImpl orderMenuService;

    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 가격이 틀릴경우 ")
    @Test
    void validateMenuPrice() {

        //given
        OrderValidator orderValidator = new OrderValidator(orderMenuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "후라이드세트", new BigDecimal("20000"), 1L);

        //메뉴 만들기
        MenuAdapter menu = new MenuAdapter(1L, "후라이드세트", new BigDecimal("18000"));
        Map<Long, MenuAdapter> products = new HashMap<>();
        products.put(1L, menu);

        when(orderMenuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 이름이 틀릴경우 ")
    @Test
    void validateMenuName() {

        //given
        OrderValidator orderValidator = new OrderValidator(orderMenuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "양념치킨세트", new BigDecimal("18000"), 1L);

        //메뉴 만들기
        MenuAdapter menu = new MenuAdapter(1L, "후라이드세트", new BigDecimal("18000"));
        Map<Long, MenuAdapter> products = new HashMap<>();
        products.put(1L, menu);

        when(orderMenuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 메뉴와 주문항목이 같을경우 ")
    @Test
    void validateCorrectMenu() {

        //given
        OrderValidator orderValidator = new OrderValidator(orderMenuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "후라이드세트", new BigDecimal("18000"), 1L);

        //메뉴 만들기
        MenuAdapter menu = new MenuAdapter(1L, "후라이드세트", new BigDecimal("18000"));
        Map<Long, MenuAdapter> products = new HashMap<>();
        products.put(1L, menu);

        when(orderMenuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        orderValidator.validate(order);
    }

}

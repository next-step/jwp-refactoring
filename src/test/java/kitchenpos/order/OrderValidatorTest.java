package kitchenpos.order;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.application.OrderValidator;
import kitchenpos.order.domain.Order;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.product.domain.Product;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
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
    private MenuService menuService;


    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 가격이 틀릴경우 ")
    @Test
    void validateMenuPrice() {

        //given
        OrderValidator orderValidator = new OrderValidator(menuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "후라이드세트", new BigDecimal("20000"), 1L);

        //메뉴 만들기
        Menu menu = Menu.prepared("후라이드세트", new BigDecimal("18000"));
        ReflectionTestUtils.setField(menu, "id", 1L);
        Map<Product, Long> products = new HashMap<>();
        Product 후라이드 = Product.create("후라이드", new BigDecimal("16000"));
        Product 콜라 = Product.create("콜라", new BigDecimal("200"));
        products.put(후라이드, 1L);
        products.put(콜라, 1L);
        menu.addProducts(products);

        when(menuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 이름이 틀릴경우 ")
    @Test
    void validateMenuName() {

        //given
        OrderValidator orderValidator = new OrderValidator(menuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "양념치킨세트", new BigDecimal("18000"), 1L);

        //메뉴 만들기
        Menu menu = Menu.prepared("후라이드세트", new BigDecimal("18000"));
        ReflectionTestUtils.setField(menu, "id", 1L);
        Map<Product, Long> products = new HashMap<>();
        Product 후라이드 = Product.create("후라이드", new BigDecimal("16000"));
        Product 콜라 = Product.create("콜라", new BigDecimal("200"));
        products.put(후라이드, 1L);
        products.put(콜라, 1L);
        menu.addProducts(products);

        when(menuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        //then
        Assertions.assertThatThrownBy(() -> orderValidator.validate(order)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문항목과 메뉴 이름과 가격 검증 할 시 메뉴와 주문항목이 같을경우 ")
    @Test
    void validateCorrectMenu() {

        //given
        OrderValidator orderValidator = new OrderValidator(menuService);

        //주문항목 만들기
        final int numberOfGuests = 10;
        final boolean isEmpty = false;

        OrderTable orderTable = OrderTable.setting(numberOfGuests, isEmpty);
        Order order = orderTable.placeOrder();
        order.addItem(1L, "후라이드세트", new BigDecimal("18000"), 1L);

        //메뉴 만들기
        Menu menu = Menu.prepared("후라이드세트", new BigDecimal("18000"));
        ReflectionTestUtils.setField(menu, "id", 1L);
        Map<Product, Long> products = new HashMap<>();
        Product 후라이드 = Product.create("후라이드", new BigDecimal("16000"));
        Product 콜라 = Product.create("콜라", new BigDecimal("200"));
        products.put(후라이드, 1L);
        products.put(콜라, 1L);
        menu.addProducts(products);

        when(menuService.findAllByIds(anyList())).thenReturn(Arrays.asList(menu));

        //when
        orderValidator.validate(order);
    }

}

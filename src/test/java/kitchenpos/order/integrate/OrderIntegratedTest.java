package kitchenpos.order.integrate;

import kitchenpos.order.application.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OrderIntegratedTest {
    @Autowired
    private OrderService orderService;

}

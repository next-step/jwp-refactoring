package kitchenpos.ui;

import kitchenpos.application.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.*;

class OrderRestControllerTest extends ControllerTest {

    @Autowired
    private OrderService orderService;

}
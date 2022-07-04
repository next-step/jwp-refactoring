package kitchenpos.order.repository;

import kitchenpos.common.domain.OrderStatus;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @CsvSource(value = {"COOKING,true", "MEAL,false"})
    @ParameterizedTest
    void existsByOrderTableIdAndOrderStatusIn(String enumString, boolean expected) {
        //given
        OrderStatus orderStatus = OrderStatus.valueOf(enumString);
        //when
        boolean exists = orderRepository.existsByOrderTableIdAndOrderStatusIn(1L, Arrays.asList(orderStatus));
        //then
        assertEquals(expected, exists);
    }
}
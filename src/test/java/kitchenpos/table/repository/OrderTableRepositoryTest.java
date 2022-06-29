package kitchenpos.table.repository;

import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class OrderTableRepositoryTest {

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    void findByIdIn() {
        //given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        //when
        List<OrderTable> list = orderTableRepository.findByIdIn(ids);
        //then
        assertEquals(3, list.size());
    }
}
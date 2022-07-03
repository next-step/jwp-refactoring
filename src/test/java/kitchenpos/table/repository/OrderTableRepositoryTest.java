package kitchenpos.table.repository;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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

    @Test
    void findByTableGroupId() {
        //given
        Long tableGroupId = 1L;
        //when
        List<OrderTable> list = orderTableRepository.findByTableGroupId(tableGroupId);
        //then
        assertAll(
            () -> assertEquals(2, list.size()),
            () -> assertEquals(9, list.get(0).getId()),
            () -> assertEquals(10, list.get(1).getId())
        );
    }
}
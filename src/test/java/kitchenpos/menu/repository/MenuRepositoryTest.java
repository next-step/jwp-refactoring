package kitchenpos.menu.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MenuRepositoryTest {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void countByIdIn() {
        //given

        //when
        int count = menuRepository.countByIdIn(Arrays.asList(1L, 2L));
        //then
        assertEquals(2, count);
    }
}
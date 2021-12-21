package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("테이블 JPA 테스트")
@DataJpaTest
@Sql(scripts = "classpath:scripts/data.sql")
class OrderTableRepositoryTest {

    @Autowired
    OrderTableRepository repository;

    @Test
    @DisplayName("여러 id로 테이블 목록 조회")
    void findAllByIdIn() {
        List<OrderTable> orderTables = repository.findAllByIdIn(Arrays.asList(1L, 2L, 3L));
        assertThat(orderTables.size()).isEqualTo(3);
    }

}
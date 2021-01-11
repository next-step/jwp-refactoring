package kitchenpos.domain.tablegroup;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TableGroupRepositoryTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private EntityManager entityManager;

    @DisplayName("엔티티 프로퍼티 이름을 통한 쿼리 학습 테스트")
    @Test
    void learningAboutEntityPropertyTest() {
        Long orderTableId = 1L;

        boolean result = tableGroupRepository.existsByOrderTablesOrderTableId(orderTableId);

        assertThat(result).isFalse();

        TableGroup tableGroup = new TableGroup(
                LocalDateTime.now(),
                Arrays.asList(new OrderTableInTableGroup(1L), new OrderTableInTableGroup(2L))
        );
        tableGroupRepository.save(tableGroup);
        entityManager.flush();

        boolean result2 = tableGroupRepository.existsByOrderTablesOrderTableId(orderTableId);

        assertThat(result2).isTrue();
    }
}
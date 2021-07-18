package kitchenpos.tablegroup.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class TableGroupRepositoryTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        // when
        TableGroup tableGroup = tableGroupRepository.save(new TableGroup());

        // then
        assertThat(tableGroup.getId()).isNotNull();
    }
}

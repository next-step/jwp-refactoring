package api.tablegroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.domain
 * fileName : TableGroupRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
@DisplayName("그룹테이블 리파지토리 테스트")
class TableGroupRepositoryTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("그룹 테이블을 생성한다.")
    public void create() {
        // given
        TableGroup tableGroup = new TableGroup();

        // when
        TableGroup actual = tableGroupRepository.save(tableGroup);

        // then
        assertThat(actual.getId()).isNotNull();
    }
}

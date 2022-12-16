package kitchenpos.order.domain;

import kitchenpos.JpaEntityTest;
import kitchenpos.order.repository.TableGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹(단체테이블) 도메인 테스트")
public class TableGroupTest extends JpaEntityTest {

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @DisplayName("단체테이블 객체 생성")
    @Test
    void create() {
        // given
        TableGroup tableGroup = new TableGroup();

        // when
        TableGroup savedTableGroup = tableGroupRepository.save(tableGroup);

        // then
        assertThat(savedTableGroup.getCreatedDate()).isNotNull();
    }
}

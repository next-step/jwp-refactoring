package kitchenpos.tablegroup.domain;

import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.IllegalOrderTableException;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static kitchenpos.table.fixtures.OrderTableFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

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

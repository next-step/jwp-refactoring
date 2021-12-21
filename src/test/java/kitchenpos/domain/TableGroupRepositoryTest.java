package kitchenpos.domain;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static kitchenpos.fixtures.OrderTableFixtures.사용가능_다섯명테이블;
import static kitchenpos.fixtures.OrderTableFixtures.사용불가_다섯명테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : kitchenpos.domain
 * fileName : TableGroupRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
class TableGroupRepositoryTest {
    private OrderTable 사용가능테이블1;
    private OrderTable 사용가능테이블2;
    private OrderTable 사용불가테이블1;


    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;


    @BeforeEach
    void setUp() {
        사용가능테이블1 = orderTableRepository.save(사용가능_다섯명테이블().toEntity());
        사용가능테이블2 = orderTableRepository.save(사용가능_다섯명테이블().toEntity());
        사용불가테이블1 = orderTableRepository.save(사용불가_다섯명테이블().toEntity());
    }

    @Test
    @DisplayName("그룹 테이블을 생성한다.")
    public void create() {
        // given
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(사용가능테이블1, 사용가능테이블2));

        // when
        TableGroup actual = tableGroupRepository.save(tableGroup);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
        );
    }
}

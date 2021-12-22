package kitchenpos.table.domain;

import kitchenpos.table.exception.IllegalOrderTableException;
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
    private OrderTable 주문가능_다섯명테이블;
    private OrderTable 주문가능_두명테이블;
    private OrderTable 주문불가_다섯명테이블;
    private OrderTable 주문불가_두명테이블;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @BeforeEach
    void setUp() {
        // not working
        주문가능_다섯명테이블 = orderTableRepository.save(주문가능_다섯명테이블());
        주문가능_두명테이블 = orderTableRepository.save(주문가능_두명테이블());

        // working
        주문불가_다섯명테이블 = orderTableRepository.save(주문불가_다섯명테이블());
        주문불가_두명테이블 = orderTableRepository.save(주문불가_두명테이블());
    }

    @Test
    @DisplayName("그룹 테이블을 생성한다.")
    public void create() {
        // given
        TableGroup tableGroup = new TableGroup(Lists.newArrayList(주문불가_다섯명테이블, 주문불가_두명테이블));

        // when
        TableGroup actual = tableGroupRepository.save(tableGroup);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderTables()).hasSize(2)
        );
    }

    @Test
    @DisplayName("테이블이 사용가능인 경우 그룹 테이블로 사용할 수 없다.")
    public void createFail() {
        //then
        assertThatThrownBy(() -> new TableGroup(Lists.newArrayList(주문가능_다섯명테이블, 주문가능_두명테이블)))
                .isInstanceOf(IllegalOrderTableException.class);
    }
}

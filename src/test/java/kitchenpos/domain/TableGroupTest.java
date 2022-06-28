package kitchenpos.domain;

import kitchenpos.dao.OrderTableRepository;
import kitchenpos.dao.TableGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("테이블 그룹 테스트")
public class TableGroupTest {
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TestEntityManager tem;

    OrderTable 주문_테이블_일번;
    OrderTable 주문_테이블_이번;

    @BeforeEach
    void beforeEach() {
        주문_테이블_일번 = orderTableRepository.save(new OrderTable(0, true));
        주문_테이블_이번 = orderTableRepository.save(new OrderTable(0, true));
    }

    @DisplayName("2개 이상의 개별 주문 테이블을 하나의 단체 지정 테이블로 생성한다.")
    @Test
    void group() {
        // when
        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));

        // show sql
        tem.flush();
        tem.clear();

        // then
        assertThat(테이블_그룹.getId()).isNotNull();
    }

    @DisplayName("단체 지정 테이블을 개별 주문 테이블로 변경한다.")
    @Test
    void ungroup() {
        // given
        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));

        // show sql
        tem.flush();
        tem.clear();

        // when
        tableGroupRepository.findById(테이블_그룹.getId())
                            .get()
                            .ungroup();

        // then
        orderTableRepository.findAll()
                            .forEach(orderTable -> assertThat(orderTable.getTableGroupId()).isNull());
    }
}

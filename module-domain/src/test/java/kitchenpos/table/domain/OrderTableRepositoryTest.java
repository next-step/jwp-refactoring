package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.domain.TableGroupRepository;

@DataJpaTest
class OrderTableRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Test
    @DisplayName("기본 저장 확인")
    void save() {
        OrderTable orderTable = orderTableRepository.save(new OrderTable(5, false));

        assertThat(orderTable.getId()).isNotNull();
    }

    @TestFactory
    @DisplayName("테이블이 비어있지 않은 주문 테이블 대상 조회")
    List<DynamicTest> findById_and_emptyIsFalse() {
        // given
        OrderTable orderTableIsNotEmpty = orderTableRepository.save(new OrderTable(5, false));
        OrderTable orderTableIsEmpty = orderTableRepository.save(new OrderTable(0, true));
        return Arrays.asList(
                dynamicTest("비어있지 않은 대상 조회 결과 있음", () -> {
                    // when
                    Optional<OrderTable> findOrderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderTableIsNotEmpty.getId());

                    // then
                    assertThat(findOrderTable).isPresent();
                }),
                dynamicTest("비어있지 않은 대상 조회 결과 없음.", () -> {
                    // when
                    Optional<OrderTable> findOrderTable = orderTableRepository.findByIdAndEmptyIsFalse(orderTableIsEmpty.getId());

                    // then
                    assertThat(findOrderTable).isEmpty();
                })
        );
    }

    @Test
    @DisplayName("ID 기준 테이블 조회 건수 확인.")
    void count_by_id() {
        // given
        OrderTable orderTable1 = orderTableRepository.save(new OrderTable(5, false));
        OrderTable orderTable2 = orderTableRepository.save(new OrderTable(0, true));

        // when
        Long count = orderTableRepository.countByIdIn(Arrays.asList(orderTable1.getId(), orderTable2.getId()));

        // then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("ID 목록으로 주문테이블 목록 조회")
    void find_by_ids() {
        // when
        List<OrderTable> orderTables = orderTableRepository.findByIdIn(Arrays.asList(1L, 2L, 100L));

        // then
        assertThat(orderTables).size().isEqualTo(2);
    }

    @Test
    @DisplayName("단체지정 ID 기준으로 테이블 목록 조회")
    void findByTableGroupId() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroupRepository.save(tableGroup);
        OrderTable orderTable1 = new OrderTable(3, true);
        OrderTable orderTable2 = new OrderTable(3, true);
        orderTable1.groupBy(tableGroup.getId());
        orderTable2.groupBy(tableGroup.getId());
        orderTableRepository.saveAll(Arrays.asList(orderTable1, orderTable2));

        // when
        List<OrderTable> orderTables = orderTableRepository.findByTableGroupId(tableGroup.getId());

        // then
        assertThat(orderTables).size().isEqualTo(2);
    }
}

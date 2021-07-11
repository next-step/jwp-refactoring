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

@DataJpaTest
class OrderTableRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

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
}

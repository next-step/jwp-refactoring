package kitchenpos.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static kitchenpos.fixtures.OrderTableFixtures.사용가능_다섯명테이블;
import static kitchenpos.fixtures.OrderTableFixtures.사용불가_다섯명테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderTableRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
class OrderTableRepositoryTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("사용불가 테이블을 생성할 수 있다.")
    public void createEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(사용불가_다섯명테이블().toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("사용가능 주문 테이블을 생성할 수 있다.")
    public void createNotEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(사용가능_다섯명테이블().toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    public void list() {
        // when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("그룹화된 테이블 정보를 조회할 수 있다.")
    public void listWithGroupTable() {
        // when
        List<OrderTable> orderTables = orderTableRepository.findAllJoinFetch();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }
}

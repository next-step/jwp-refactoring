package kitchenpos.domain;

import kitchenpos.dto.OrderTableSaveRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static kitchenpos.fixtures.OrderTableFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderTableRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
class OrderTableRepositoryTest {
    private OrderTableSaveRequest 빈_다섯명테이블 = 사용불가_다섯명테이블();
    private OrderTableSaveRequest 사용중_다섯명테이블 = 사용가능_다섯명테이블();

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("빈 주문 테이블을 생성할 수 있다.")
    public void createEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(빈_다섯명테이블.toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("사용중 주문 테이블을 생성할 수 있다.")
    public void createNotEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(사용중_다섯명테이블.toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    public void list() throws Exception{
        // when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("그룹화된 테이블 정보를 조회할 수 있다.")
    public void listWithGroupTable() throws Exception{
        // when
        List<OrderTable> orderTables = orderTableRepository.findAllJoinFetch();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }


     //TODO 테이블 변경 테스트 할것
}

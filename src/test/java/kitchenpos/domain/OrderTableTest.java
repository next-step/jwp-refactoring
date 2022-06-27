package kitchenpos.domain;

import kitchenpos.dao.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("주문 테이블 테스트")
class OrderTableTest {
    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TestEntityManager tem;

    @DisplayName("개별 주문 테이블을 생성한다")
    @Test
    void save() {
        // when
        OrderTable 주문_테이블 = orderTableRepository.save(new OrderTable(0, true));

        // then
        assertThat(주문_테이블.getId()).isNotNull();
    }

    @DisplayName("테이블의 주문 등록 가능 상태를 변경한다.")
    @Test
    void changeEmpty() {
        // given
        OrderTable 빈_테이블 = tem.persistAndFlush(new OrderTable(0, true));

        // when
        빈_테이블.changeEmpty(false);
        // show sql
        tem.flush();
        tem.clear();
        OrderTable 주문_등록_가능_테이블 = tem.find(OrderTable.class, 빈_테이블.getId());

        // then
        assertThat(주문_등록_가능_테이블.isEmpty()).isFalse();
    }

    @DisplayName("테이블의 방문한 손님 수를 변경한다.")
    @Test
    void changeNumberOfGuests() {
        // given
        OrderTable 주문_테이블 = tem.persistAndFlush(new OrderTable(0, true));
        주문_테이블.changeEmpty(false);
        주문_테이블.changeNumberOfGuests(8);

        // show sql
        tem.flush();
        tem.clear();

        // when
        OrderTable orderTable = tem.find(OrderTable.class, 주문_테이블.getId());

        // then
        assertThat(orderTable.getNumberOfGuests()).isEqualTo(8);
    }
}

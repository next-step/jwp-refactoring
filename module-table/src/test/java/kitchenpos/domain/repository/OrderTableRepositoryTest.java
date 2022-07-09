//package kitchenpos.domain.repository;
//
//import kitchenpos.domain.OrderTable;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//public class OrderTableRepositoryTest {
//    @Autowired
//    OrderTableRepository orderTableRepository;
//
//    @Test
//    @DisplayName("주문 테이블 생성")
//    void create() {
//        // given
//        final OrderTable orderTable = OrderTable.of(0, true);
//        // when
//        final OrderTable save = orderTableRepository.save(orderTable);
//        // then
//        assertThat(save).isInstanceOf(OrderTable.class);
//    }
//}

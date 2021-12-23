package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.domain.TableGroupDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OrderDaoTest {

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private TableGroupDao tableGroupDao;

    @Test
    void id값으로_주문상태확인() {
        // when
        OrderStatus orderStatus = orderDao.findByOrderTableId(9L);

        // then
        assertThat(orderStatus).isEqualTo(OrderStatus.COOKING);
    }

    @Test
    void 테이블_그룹에_해당하는_테이블이_정상_조회되는지_확인() {

        // given
        TableGroup tableGroup = tableGroupDao.findById(1L).get();

        // when
        List<OrderStatus> orderStatuses = orderDao.findOrderStatusByOrderTableIn(
            tableGroup.getOrderTableIds());

        // then
        assertThat(orderStatuses).contains(OrderStatus.COOKING);
    }
}
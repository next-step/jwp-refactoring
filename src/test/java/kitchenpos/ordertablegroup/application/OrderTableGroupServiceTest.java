package kitchenpos.ordertablegroup.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.ordertable.application.OrderTableService;
import kitchenpos.ordertablegroup.dto.OrderTableGroupRequest;
import kitchenpos.ordertablegroup.dto.OrderTableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class OrderTableGroupServiceTest {

    @Autowired
    private OrderTableService orderTableService;

    @Autowired
    private OrderTableGroupService orderTableGroupService;

    @Autowired
    private OrderDao orderDao;

    @DisplayName("테이블 그룹을 등록한다.")
    @Test
    void create() {
        // given
        OrderTableGroupRequest tableGroup = new OrderTableGroupRequest(Arrays.asList(1L, 2L));

        // when
        OrderTableGroupResponse savedTableGroup = orderTableGroupService.create(tableGroup);

        // then
        assertThat(savedTableGroup.getId()).isNotNull();
        assertThat(savedTableGroup.getOrderTables().size()).isEqualTo(2);
    }

}

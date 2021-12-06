package kitchenpos.application;

import kitchenpos.dao.*;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.fixture.OrderTableFixture.주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 단체 지정을 저장할 수 있다
 * - 단체 지정의 주문 테이블의 개수가 올바르지 않으면 단체 지정을 저장할 수 없다
 *     - 주문 테이블 목록이 비어 있을 수 없다
 *     - 주문 테이블 목록의 크기는 2 이상이어야 한다
 * - 단체 지정의 저장된 주문 테이블이 올바르지 않으면 단체 지정을 저장할 수 없다
 *     - 주문 테이블 목록과 저장된 저장된 주문 테이블 목록의 크기가 같아야 한다
 * - 단체 지정의 주문 테이블이 올바르지 않으면 단체 지정을 저장할 수 없다
 *     - 주문 테이블 목록이 비어 있을 수 없다
 *     - 주문 테이블의 테이블 그룹 아이디는 존재해야 한다
 * - 단체 지정을 삭제할 수 있다
 * - 단체 지정의 주문 테이블 아이디와 주문 혹은 식사 주문 상태인 주문이 존재하면 단체 지정을 삭제할 수 없다
 */
class TableGroupServiceTest {

    private static final OrderTable 빈_주문_테이블 = 주문_테이블(0, true);

    private OrderDao orderDao;
    private OrderTableDao orderTableDao;
    private TableGroupDao tableGroupDao;
    private TableGroupService tableGroupService;

    @BeforeEach
    void setUp() {
        orderDao = new InMemoryOrderDao();
        orderTableDao = new InMemoryOrderTableDao();
        tableGroupDao = new InMemoryTableGroupDao();
        tableGroupService = new TableGroupService(orderDao, orderTableDao, tableGroupDao);
    }

    @Test
    void create_단체_지정을_저장할_수_있다() {
        OrderTable 저장된_주문_테이블1 = orderTableDao.save(빈_주문_테이블);
        OrderTable 저장된_주문_테이블2 = orderTableDao.save(빈_주문_테이블);
        TableGroup tableGroup = 단체_지정(Arrays.asList(저장된_주문_테이블1, 저장된_주문_테이블2));

        TableGroup 저장된_단체_지정 = tableGroupService.create(tableGroup);

        assertAll(
                () -> assertThat(저장된_단체_지정.getOrderTables().size()).isEqualTo(2),
                () -> assertThat(저장된_단체_지정.getCreatedDate()).isNotNull()
        );
    }

    @Test
    void create_단체_지정의_주문_테이블의_개수가_올바르지_않으면_단체_지정을_저장할_수_없다() {
        OrderTable 저장된_주문_테이블1 = orderTableDao.save(빈_주문_테이블);
        TableGroup tableGroup = 단체_지정(Arrays.asList(저장된_주문_테이블1));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    void create_단체_지정의_저장된_주문_테이블이_올바르지_않으면_단체_지정을_저장할_수_없다() {
        OrderTable 저장된_주문_테이블 = orderTableDao.save(빈_주문_테이블);
        TableGroup tableGroup = 단체_지정(Arrays.asList(저장된_주문_테이블, 빈_주문_테이블));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    @Test
    void create_단체_지정의_주문_테이블이_올바르지_않으면_단체_지정을_저장할_수_없다() {
        OrderTable 빈_주문_테이블 = orderTableDao.save(주문_테이블(0, 1L, true));
        OrderTable 채워진_주문_테이블 = orderTableDao.save(주문_테이블(2, null, false));
        TableGroup tableGroup = 단체_지정(Arrays.asList(빈_주문_테이블, 채워진_주문_테이블));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> tableGroupService.create(tableGroup));
    }

    private TableGroup 단체_지정(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);
        return tableGroup;
    }
}
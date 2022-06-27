package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.fixture.MenuFixtureFactory;
import kitchenpos.fixture.MenuGroupFixtureFactory;
import kitchenpos.fixture.MenuProductFixtureFactory;
import kitchenpos.fixture.OrderFixtureFactory;
import kitchenpos.fixture.OrderLineItemFixtureFactory;
import kitchenpos.fixture.OrderTableFixtureFactory;
import kitchenpos.fixture.ProductFixtureFactory;
import kitchenpos.fixture.TableGroupFixtureFactory;
import kitchenpos.application.table.OrderTableGroupingEventHandler;
import kitchenpos.application.table.OrderTableUngroupEventHandler;
import kitchenpos.application.tablegroup.TableGroupService;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.domain.tablegroup.event.TableGroupingEvent;
import kitchenpos.domain.tablegroup.event.TableUngroupEvent;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.CreateTableGroupException;
import kitchenpos.exception.DontUnGroupException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

@DataJpaTest(includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, classes = Component.class))
class TableGroupServiceTest {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private TableGroupRepository tableGroupRepository;

    @Autowired
    private TableGroupService tableGroupService;

    @Autowired
    private OrderTableGroupingEventHandler groupingEventHandler;

    @Autowired
    private OrderTableUngroupEventHandler ungroupEventHandler;

    private MenuGroup 초밥_메뉴그룹;
    private Product 우아한_초밥_1;
    private Product 우아한_초밥_2;
    private MenuProduct A_우아한_초밥_1;
    private MenuProduct A_우아한_초밥_2;
    private Menu A;
    private OrderTable A_주문_테이블;
    private Order A_주문;
    private OrderLineItem A_주문항목;


    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;
    private OrderTable 주문_테이블_10명;

    @BeforeEach
    void setUp() {
        초밥_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "초밥_메뉴그룹");
        우아한_초밥_1 = ProductFixtureFactory.create(1L, "우아한_초밥_1", BigDecimal.valueOf(10_000));
        우아한_초밥_2 = ProductFixtureFactory.create(2L, "우아한_초밥_2", BigDecimal.valueOf(20_000));
        A = MenuFixtureFactory.create("A", BigDecimal.valueOf(30_000), 초밥_메뉴그룹.getId());

        A_우아한_초밥_1 = MenuProductFixtureFactory.create(1L, A, 우아한_초밥_1.getId(), 1);
        A_우아한_초밥_2 = MenuProductFixtureFactory.create(2L, A, 우아한_초밥_2.getId(), 2);

        A_우아한_초밥_1.mappedByMenu(A);
        A_우아한_초밥_2.mappedByMenu(A);

        초밥_메뉴그룹 = menuGroupRepository.save(초밥_메뉴그룹);
        우아한_초밥_1 = productRepository.save(우아한_초밥_1);
        우아한_초밥_2 = productRepository.save(우아한_초밥_2);
        A = menuRepository.save(A);

        A_주문_테이블 = OrderTableFixtureFactory.createWithGuest(false, 2);
        A_주문항목 = OrderLineItemFixtureFactory.create(A.getId(), 1);

        A_주문_테이블 = orderTableRepository.save(A_주문_테이블);

        A_주문 = OrderFixtureFactory.create(A_주문_테이블.getId(), OrderStatus.COOKING, Lists.newArrayList(A_주문항목));
        A_주문 = orderRepository.save(A_주문);

        주문_1_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_2_테이블 = OrderTableFixtureFactory.createWithGuest(true, 2);
        주문_테이블_10명 = OrderTableFixtureFactory.createWithGuest(false, 10);
        단체_1 = TableGroupFixtureFactory.create(1L);

        주문_1_테이블 = orderTableRepository.save(주문_1_테이블);
        주문_2_테이블 = orderTableRepository.save(주문_2_테이블);
        주문_테이블_10명 = orderTableRepository.save(주문_테이블_10명);

        단체_1 = tableGroupRepository.save(단체_1);
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // given
        TableGroupRequest request = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));

        // when
        TableGroupResponse response = tableGroupService.create(request);

        // then
        TableGroup findTableGroup = tableGroupRepository.findById(response.getId()).get();
        assertThat(response).isEqualTo(TableGroupResponse.from(findTableGroup));
    }

    @DisplayName("주문 테이블이 비어있으면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create02() {
        // given
        List<Long> orderTableIds = Collections.emptyList();

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("주문 테이블이 1개이면 테이블을 단체로 지정할 수 없다.")
    @Test
    void create03() {
        // given
        List<Long> orderTableIds = Lists.newArrayList(주문_1_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체에 속하는 주문 테이블이 빈 테이블이 아니면 단체로 지정할 수 없다.")
    @Test
    void create05() {
        // given
        List<Long> orderTableIds = Lists.newArrayList(주문_테이블_10명.getId(), 주문_1_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체에 속하는 주문 테이블이 이미 테이블 그룹에 속해있으면 단체로 지정할 수 없다.")
    @Test
    void create06() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1.getId());
        List<Long> orderTableIds = Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId());

        // when & then
        assertThrows(CreateTableGroupException.class, () -> groupingEventHandler.handle(new TableGroupingEvent(단체_1, orderTableIds)));
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        주문_1_테이블.mappedByTableGroup(단체_1.getId());
        주문_2_테이블.mappedByTableGroup(단체_1.getId());

        // when
        ungroupEventHandler.handle(new TableUngroupEvent(단체_1));

        // then
        assertAll(
                () -> assertThat(주문_1_테이블.getTableGroupId()).isNull(),
                () -> assertThat(주문_2_테이블.getTableGroupId()).isNull()
        );
    }

    @DisplayName("주문 테이블의 주문 상태가 COOKING 이거나 MEAL 인 경우 단체를 해제할 수 없다.")
    @Test
    void change02() {
        // given
        A_주문_테이블.mappedByTableGroup(단체_1.getId());
        주문_테이블_10명.mappedByTableGroup(단체_1.getId());

        // when & then
        assertThrows(DontUnGroupException.class, () -> ungroupEventHandler.handle(new TableUngroupEvent(단체_1)));
    }
}
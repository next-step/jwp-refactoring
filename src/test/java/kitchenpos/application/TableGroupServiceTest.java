package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;

import kitchenpos.application.fixture.MenuFixtureFactory;
import kitchenpos.application.fixture.MenuGroupFixtureFactory;
import kitchenpos.application.fixture.MenuProductFixtureFactory;
import kitchenpos.application.fixture.OrderFixtureFactory;
import kitchenpos.application.fixture.OrderLineItemFixtureFactory;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.ProductFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
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
import kitchenpos.domain.product.Product;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.domain.table.event.OrderTableUngroupedEvent;
import kitchenpos.domain.tablegroup.TableGroup;
import kitchenpos.domain.tablegroup.TableGroupRepository;
import kitchenpos.dto.tablegroup.OrderTableIdRequest;
import kitchenpos.dto.tablegroup.TableGroupRequest;
import kitchenpos.dto.tablegroup.TableGroupResponse;
import kitchenpos.exception.NotCompletionOrderException;

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
    private ApplicationEventPublisher publisher;

    private MenuGroup 고기_메뉴그룹;
    private Product 돼지고기;
    private Product 공기밥;
    private Menu 불고기;
    private MenuProduct 불고기_돼지고기;
    private MenuProduct 불고기_공기밥;

    private OrderLineItem 불고기_주문항목;
    private Order 불고기_주문;

    private TableGroup 단체_테이블그룹;
    private OrderTable 주문1_단체테이블;
    private OrderTable 주문2_단체테이블;
    private OrderTable 손님_10명_개인테이블;
    private OrderTable 개인1_단체테이블;
    private OrderTable 개인2_단체테이블;

    @BeforeEach
    void setUp() {
        고기_메뉴그룹 = MenuGroupFixtureFactory.create(1L, "고기 메뉴그룹");
        돼지고기 = ProductFixtureFactory.create(1L, "돼지고기", 9_000);
        공기밥 = ProductFixtureFactory.create(2L, "공기밥", 1_000);
        불고기 = MenuFixtureFactory.create(1L, "불고기", 10_000, 고기_메뉴그룹);

        불고기_돼지고기 = MenuProductFixtureFactory.create(1L, 불고기, 돼지고기, 1L);
        불고기_공기밥 = MenuProductFixtureFactory.create(2L, 불고기, 공기밥, 1L);
        불고기.addMenuProducts(Arrays.asList(불고기_돼지고기, 불고기_공기밥));

        주문1_단체테이블 = OrderTableFixtureFactory.create(1L, true);
        주문2_단체테이블 = OrderTableFixtureFactory.create(2L, true);
        손님_10명_개인테이블 = OrderTableFixtureFactory.createWithGuests(3L, 10, false);
        개인1_단체테이블 = OrderTableFixtureFactory.create(4L, true);
        개인2_단체테이블 = OrderTableFixtureFactory.create(5L, true);
        단체_테이블그룹 = TableGroupFixtureFactory.create(1L);

        불고기_주문 = OrderFixtureFactory.create(1L, 주문1_단체테이블.getId(), OrderStatus.COOKING);
        불고기_주문항목 = OrderLineItemFixtureFactory.create(1L, 불고기_주문.getId(), 불고기.getId(), 1L);
        불고기_주문.addOrderLineItems(Arrays.asList(불고기_주문항목));

        고기_메뉴그룹 = menuGroupRepository.save(고기_메뉴그룹);
        돼지고기 = productRepository.save(돼지고기);
        공기밥 = productRepository.save(공기밥);
        불고기 = menuRepository.save(불고기);

        주문1_단체테이블 = orderTableRepository.save(주문1_단체테이블);
        주문2_단체테이블 = orderTableRepository.save(주문2_단체테이블);
        손님_10명_개인테이블 = orderTableRepository.save(손님_10명_개인테이블);
        개인1_단체테이블 = orderTableRepository.save(개인1_단체테이블);
        개인2_단체테이블 = orderTableRepository.save(개인2_단체테이블);
        단체_테이블그룹 = tableGroupRepository.save(단체_테이블그룹);

        불고기_주문 = orderRepository.save(불고기_주문);
    }

    @DisplayName("TableGroup 을 등록한다.")
    @Test
    void create1() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(개인1_단체테이블.getId()), OrderTableIdRequest.from(개인2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when
        TableGroupResponse tableGroupResponse = tableGroupService.create(tableGroupRequest);

        // then
        TableGroup findTableGroup = tableGroupRepository.findById(tableGroupResponse.getId()).get();

        assertThat(tableGroupResponse).isEqualTo(
            TableGroupResponse.from(findTableGroup, Arrays.asList(개인1_단체테이블, 개인2_단체테이블)));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 0개면 예외가 발생한다.")
    @Test
    void create2() {
        // given
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 1개면 예외가 발생한다.")
    @Test
    void create3() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests = Arrays.asList(OrderTableIdRequest.from(주문1_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 존재하지 않으면 예외가 발생한다.")
    @Test
    void create4() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(0L), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when & then
        assertThrows(EntityNotFoundException.class, () -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 빈 테이블이 아니면 예외가 발생한다.")
    @Test
    void create5() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(손님_10명_개인테이블.getId()), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 등록 시, OrderTable 이 이미 그룹에 속해있으면 예외가 발생한다.")
    @Test
    void create6() {
        // given
        List<OrderTableIdRequest> orderTableIdRequests =
            Arrays.asList(OrderTableIdRequest.from(주문1_단체테이블.getId()), OrderTableIdRequest.from(주문2_단체테이블.getId()));
        TableGroupRequest tableGroupRequest = TableGroupRequest.from(orderTableIdRequests);
        주문1_단체테이블.alignTableGroup(단체_테이블그룹.getId());

        // when & then
        assertThatIllegalArgumentException().isThrownBy(() -> tableGroupService.create(tableGroupRequest));
    }

    @DisplayName("TableGroup 을 해제한다.")
    @Test
    void ungroup1() {
        // given
        주문1_단체테이블.alignTableGroup(단체_테이블그룹.getId());
        주문2_단체테이블.alignTableGroup(단체_테이블그룹.getId());
        불고기_주문.changeOrderStatus(OrderStatus.COMPLETION);

        // when
        publisher.publishEvent(new OrderTableUngroupedEvent(단체_테이블그룹));

        // then
        OrderTable 그룹해제된_주문1_단체테이블 = orderTableRepository.findById(주문1_단체테이블.getId()).get();
        OrderTable 그룹해제된_주문2_단체테이블 = orderTableRepository.findById(주문2_단체테이블.getId()).get();

        assertFalse(그룹해제된_주문1_단체테이블.hasTableGroup());
        assertFalse(그룹해제된_주문2_단체테이블.hasTableGroup());
    }

    @DisplayName("TableGroup 해제 시, 주문상태가 요리중(COOKING)이거나 식사중(MEAL) 이면 예외가 발생한다.")
    @Test
    void ungroup2() {
        // given
        주문1_단체테이블.alignTableGroup(단체_테이블그룹.getId());
        주문2_단체테이블.alignTableGroup(단체_테이블그룹.getId());

        // when & then
        assertThrows(NotCompletionOrderException.class, () -> tableGroupService.ungroup(단체_테이블그룹.getId()));
    }
}
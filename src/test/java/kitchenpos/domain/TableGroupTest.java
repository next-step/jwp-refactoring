package kitchenpos.domain;

import kitchenpos.dao.*;
import kitchenpos.dto.OrderLineItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("테이블 그룹 테스트")
public class TableGroupTest {
    @Autowired
    private TestEntityManager tem;
    @Autowired
    private TableGroupRepository tableGroupRepository;
    @Autowired
    private OrderTableRepository orderTableRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ProductRepository productRepository;

    OrderTable 주문_테이블_일번;
    OrderTable 주문_테이블_이번;
    OrderTable 주문_테이블_삼번;

    Product 마늘치킨;
    Product 양념치킨;
    MenuGroup 점심메뉴;
    MenuProduct 점심특선_마늘치킨;
    MenuProduct 점심특선_양념치킨;
    Menu 점심특선;

    TableGroup 주문_테이블_일번_이번_묶음;

    @BeforeEach
    void beforeEach() {
        주문_테이블_일번 = orderTableRepository.save(new OrderTable(0, true));
        주문_테이블_이번 = orderTableRepository.save(new OrderTable(0, true));
        주문_테이블_삼번 = orderTableRepository.save(new OrderTable(4, false));

        마늘치킨 = productRepository.save(new Product("마늘치킨", 1000));
        양념치킨 = productRepository.save(new Product("양념치킨", 1000));
        점심메뉴 = menuGroupRepository.save(MenuGroup.builder()
                                                 .name("두마리메뉴")
                                                 .build());
        점심특선_마늘치킨 = new MenuProduct(마늘치킨, 1);
        점심특선_양념치킨 = new MenuProduct(양념치킨, 1);

        점심특선 = menuRepository.save(new Menu("점심특선", 2000, 점심메뉴.getId(), Arrays.asList(점심특선_마늘치킨, 점심특선_양념치킨)));
    }

    @DisplayName("2개 이상의 개별 주문 테이블을 하나의 단체 지정 테이블로 생성한다.")
    @Test
    void group() {
        // when
        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));

        // show sql
        tem.flush();
        tem.clear();

        // then
        assertThat(테이블_그룹.getId()).isNotNull();
    }

    @DisplayName("단체 지정 테이블을 개별 주문 테이블로 변경한다.")
    @Test
    void ungroup() {
        // given
        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));

        // show sql
        tem.flush();
        tem.clear();

        // when
        tableGroupRepository.findById(테이블_그룹.getId()).get().ungroup();

        // then
        orderTableRepository.findAll().forEach(orderTable -> assertThat(orderTable.getTableGroup()).isNull());
    }

    @DisplayName("빈 테이블이 아닌 테이블은 단체 지정할 수 없다.")
    @Test
    void group_throwException_givenGroupedTable() {
        // when
        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_삼번))));
    }

    @DisplayName("이미 그룹화된 테이블은 단체 지정할 수 없다.")
    @Test
    void group_throwException_givenEmptyTable() {
        // given
        TableGroup 테이블_그룹 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));

        // when
        // then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번))));
    }

    @Disabled
    @DisplayName("주문 상태가 조리 또는 식사인 경우 개별 주문 테이블로 변경할 수 없다.")
    @Test
    void ungroup_throwException_givenOrderStatusInCookingAndMeal() {
        // given
        테이블_그룹_생성됨();
        주문_생성됨();

        // show sql
        tem.flush();
        tem.clear();

        // when
        TableGroup 테이블_그룹 = tableGroupRepository.findById(주문_테이블_일번_이번_묶음.getId()).get();

        // then
        assertThatIllegalArgumentException().isThrownBy(테이블_그룹::ungroup);
    }

    private void 테이블_그룹_생성됨() {
        주문_테이블_일번_이번_묶음 = tableGroupRepository.save(new TableGroup(Arrays.asList(주문_테이블_일번, 주문_테이블_이번)));
    }

    private void 주문_생성됨() {
        List<OrderLineItemRequest> OrderLineItems = Arrays.asList(new OrderLineItemRequest(점심특선.getId(), 1));
        orderRepository.save(new Order(주문_테이블_일번, OrderStatus.COOKING, toOrderLineItems(Arrays.asList(점심특선), OrderLineItems)));
    }

    private List<OrderLineItem> toOrderLineItems(List<Menu> menus, List<OrderLineItemRequest> OrderLineItems) {
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        for (OrderLineItemRequest orderLineItem : OrderLineItems) {
            menus.stream()
                 .filter(menu -> menu.getId().equals(orderLineItem.getMenuId()))
                 .findFirst()
                 .ifPresent(menu -> orderLineItems.add(new OrderLineItem(menu, orderLineItem.getQuantity())));
        }
        return orderLineItems;
    }
}

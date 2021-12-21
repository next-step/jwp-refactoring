package kitchenpos.domain;

import kitchenpos.exception.OrderStatusUpdateException;
import kitchenpos.fixtures.OrderTableFixtures;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.반반메뉴;
import static kitchenpos.fixtures.OrderLineItemFixtures.주문정보_1개_수량_1개;
import static kitchenpos.fixtures.OrderTableFixtures.사용가능_다섯명테이블;
import static kitchenpos.fixtures.ProductFixtures.양념치킨;
import static kitchenpos.fixtures.ProductFixtures.후라이드;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
class OrderRepositoryTest {
    private Order 후라이드반양념반두개주세요;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    OrderTableRepository orderTableRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);
        Product 양념치킨 = productRepository.save(양념치킨().toEntity());
        Product 후라이드 = productRepository.save(후라이드().toEntity());
        MenuGroup 메뉴그룹 = menuGroupRepository.save(반반메뉴().toEntity());
        MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨, 1L);
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드, 1L);
        Menu 후라이드반양념반메뉴 = menuRepository.save(new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)));
        OrderTable 사용가능_다섯명테이블 = orderTableRepository.save(OrderTableFixtures.사용가능_다섯명테이블().toEntity());

        OrderLineItem 후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴, 2L);
        후라이드반양념반두개주세요 = new Order(사용가능_다섯명테이블, Lists.newArrayList(후라이드양념반두개));

    }

    @Test
    @DisplayName("주문을 조회할 수 있다.")
    public void list() throws Exception {
        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertThat(orders.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("주문을 조회할 떄 주문상품도 같이 조회한다")
    public void listWithOrderLineItems() throws Exception {
        // when
        List<Order> orders = orderRepository.findAllJoinFetch();

        // then
        assertThat(orders.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("주문을 등록할 수 있다.")
    public void create() throws Exception {
        // when
        Order actual = orderRepository.save(후라이드반양념반두개주세요);

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getOrderLineItems()).extracting(OrderLineItem::getId)
                        .isNotNull()
        );
    }
}

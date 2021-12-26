package kitchenpos.order.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import kitchenpos.table.fixtures.OrderTableFixtures;
import kitchenpos.menu.domain.*;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.menu.fixtures.MenuGroupFixtures.메뉴그룹;
import static kitchenpos.menu.fixtures.MenuProductFixtures.*;
import static kitchenpos.menu.fixtures.ProductFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
@DisplayName("주문 리파지토리 테스트")
class OrderRepositoryTest {
    private Order 후라이드반양념반두개주세요;
    private Menu 후라이드반양념반메뉴;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);
        Product 양념치킨 = productRepository.save(양념치킨());
        Product 후라이드 = productRepository.save(후라이드());
        MenuGroup 메뉴그룹 = menuGroupRepository.save(메뉴그룹("반반메뉴그룹"));
        MenuProduct 양념치킨메뉴상품 = 메뉴상품(양념치킨, 1L);
        MenuProduct 후라이드메뉴상품 = 메뉴상품(후라이드, 1L);
        후라이드반양념반메뉴 = menuRepository.save(
                new Menu(
                        "후라이드반양념반메뉴",
                        메뉴가격,
                        메뉴그룹.getId(),
                        Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)
                )
        );
        OrderTable 사용가능_다섯명테이블 = orderTableRepository.save(OrderTableFixtures.주문가능_다섯명테이블());
        OrderLineItem 후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴.getId(), 2L);
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
        assertThat(actual.getId()).isNotNull();
    }
}

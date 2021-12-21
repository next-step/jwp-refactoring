package kitchenpos.domain;

import kitchenpos.exception.OrderTableNotFoundException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;

import static kitchenpos.fixtures.MenuGroupFixtures.반반메뉴그룹요청;
import static kitchenpos.fixtures.OrderTableFixtures.주문가능_다섯명테이블요청;
import static kitchenpos.fixtures.OrderTableFixtures.주문불가_다섯명테이블요청;
import static kitchenpos.fixtures.ProductFixtures.양념치킨요청;
import static kitchenpos.fixtures.ProductFixtures.후라이드요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * packageName : kitchenpos.domain
 * fileName : OrderTableRepositoryTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DataJpaTest
@DisplayName("주문테이블 리파지토리 테스트")
class OrderTableRepositoryTest {
    private OrderTable savedTable;

    @Autowired
    private OrderTableRepository orderTableRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        BigDecimal 메뉴가격 = new BigDecimal(32000);
        Product 양념치킨 = productRepository.save(양념치킨요청().toEntity());
        Product 후라이드 = productRepository.save(후라이드요청().toEntity());
        MenuGroup 메뉴그룹 = menuGroupRepository.save(반반메뉴그룹요청().toEntity());
        MenuProduct 양념치킨메뉴상품 = new MenuProduct(양념치킨, 1L);
        MenuProduct 후라이드메뉴상품 = new MenuProduct(후라이드, 1L);
        Menu 후라이드반양념반메뉴 = menuRepository.save(new Menu("후라이드반양념반메뉴", 메뉴가격, 메뉴그룹, Lists.newArrayList(양념치킨메뉴상품, 후라이드메뉴상품)));
        savedTable = orderTableRepository.save(주문가능_다섯명테이블요청().toEntity());

        OrderLineItem 후라이드양념반두개 = new OrderLineItem(후라이드반양념반메뉴, 2L);
        orderRepository.save(new Order(savedTable, Lists.newArrayList(후라이드양념반두개)));
    }

    @Test
    @DisplayName("사용불가 테이블을 생성할 수 있다.")
    public void createEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(주문불가_다섯명테이블요청().toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isTrue()
        );
    }

    @Test
    @DisplayName("사용가능 주문 테이블을 생성할 수 있다.")
    public void createNotEmptyTable() {
        // when
        OrderTable actual = orderTableRepository.save(주문가능_다섯명테이블요청().toEntity());

        // then
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getTableGroup()).isNull(),
                () -> assertThat(actual.isEmpty()).isFalse()
        );
    }

    @Test
    @DisplayName("테이블 목록을 조회할 수 있다.")
    public void list() {
        // when
        List<OrderTable> orderTables = orderTableRepository.findAll();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("그룹화된 테이블 정보를 조회할 수 있다.")
    public void listWithGroupTable() {
        // when
        List<OrderTable> orderTables = orderTableRepository.findAllJoinFetch();

        // then
        assertThat(orderTables.size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("테이블에 있는 주문 정보를 조회할 수 있다.")
    public void listWithOrder() {
        // given
        final OrderTable orderTable = orderTableRepository.findOneWithOrderByIdJoinFetch(savedTable.getId())
                .orElseThrow(OrderTableNotFoundException::new);

        // then
        assertThat(orderTable.getOrder()).isNotNull();
    }
}

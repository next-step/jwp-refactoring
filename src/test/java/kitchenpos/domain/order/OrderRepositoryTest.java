package kitchenpos.domain.order;

import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repository:Order")
class OrderRepositoryTest extends ScenarioTestFixtureGenerator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    public OrderRepositoryTest(
        ProductRepository productRepository,
        MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository,
        OrderTableRepository orderTableRepository,
        OrderRepository orderRepository,
        EntityManager entityManager
    ) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
        this.entityManager = entityManager;
    }

    private Menu 커플_냉삼_메뉴, 고기_더블_더블_메뉴;
    private OrderTable 비어있지_않은_첫번째_주문_테이블, 비어있지_않은_두번째_주문_테이블;
    private Order 커플_냉삼_메뉴_주문, 고기_더블_더블_메뉴_주문;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        메뉴_생성();
        주문_테이블_생성();
        커플_냉삼_메뉴_주문 = orderRepository.save(주문_생성(비어있지_않은_첫번째_주문_테이블, 커플_냉삼_메뉴));
        고기_더블_더블_메뉴_주문 = orderRepository.save(주문_생성(비어있지_않은_두번째_주문_테이블, 커플_냉삼_메뉴));
        entityManager.clear();
    }

    private void 메뉴_생성() {
        productRepository.saveAll(Arrays.asList(물냉면, 비빔냉면, 삼겹살));
        menuGroupRepository.save(고기랑_같이);
        커플_냉삼_메뉴 = menuRepository.save(ScenarioTestFixtureGenerator.커플_냉삼_메뉴);

        productRepository.saveAll(Arrays.asList(항정살, 고추장_불고기));
        menuGroupRepository.save(고기만_듬뿍);
        고기_더블_더블_메뉴 = menuRepository.save(ScenarioTestFixtureGenerator.고기_더블_더블_메뉴);
    }

    private void 주문_테이블_생성() {
        비어있지_않은_첫번째_주문_테이블 = orderTableRepository.save(비어있지_않은_주문_테이블_생성());
        비어있지_않은_두번째_주문_테이블 = orderTableRepository.save(비어있지_않은_주문_테이블_생성());
    }

    /**
     * 주문 목록 조회 시, 대상 건수가 100건(Batch Fetch Size) 이하인 경우 발생하는 쿼리는 최대 3회
     * - N:1 관계인 주문의 주문 테이블은 @EntityGraph를 이용한 즉시 조회
     * - 1:N 관계인 주문의 주문 항목은 `FetchType.LAZY + batch_fetch_size`를 이용한 In절 조회
     * - 1:N 관계인 주문 항목의 메뉴는 `FetchType.LAZY + batch_fetch_size`를 이용한 In절 조회
     */
    @Test
    @DisplayName("주문 목록 조회")
    public void findAllOrder() {
        // When
        List<Order> actual = orderRepository.findAll();

        // Then
        List<OrderResponse> orderResponses = actual.stream()
            .map(OrderResponse::from)
            .collect(Collectors.toList());

        List<OrderResponse> filteredOrderResponse = orderResponses.stream()
            .filter(it -> it.getId().equals(커플_냉삼_메뉴_주문.getId()) || it.getId().equals(고기_더블_더블_메뉴_주문.getId()))
            .collect(Collectors.toList());

        assertThat(filteredOrderResponse).extracting(OrderResponse::getOrderTableId)
            .containsOnly(비어있지_않은_첫번째_주문_테이블.getId(), 비어있지_않은_두번째_주문_테이블.getId());
    }
}

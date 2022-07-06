package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import kitchenpos.domain.product.ProductRepository;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;

@DataJpaTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@DisplayName("Repository:Menu")
class MenuRepositoryTest extends ScenarioTestFixtureGenerator {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;
    private final EntityManager entityManager;

    public MenuRepositoryTest(
        ProductRepository productRepository,
        MenuGroupRepository menuGroupRepository,
        MenuRepository menuRepository,
        EntityManager entityManager
    ) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
        this.entityManager = entityManager;
    }

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        productRepository.saveAll(Arrays.asList(물냉면, 비빔냉면, 삼겹살));
        menuGroupRepository.save(고기랑_같이);
        menuRepository.save(커플_냉삼_메뉴);
        entityManager.clear();
    }

    /**
     * 주문 목록 조회 시, 대상 건수가 100건(Batch Fetch Size) 이하인 경우 발생하는 쿼리는 최대 3회
     * - N:1 관계인 메뉴의 메뉴 그룹은 @EntityGraph를 이용한 즉시 조회
     * - 1:N 관계인 메뉴의 메뉴 상품은 `FetchType.LAZY + batch_fetch_size`를 이용한 In절 조회
     * - 1:N 관계인 메뉴 상품의 상품은 `FetchType.LAZY + batch_fetch_size`를 이용한 In절 조회
     */
    @Test
    @DisplayName("메뉴 목록 조회 쿼리 개선 검증")
    public void findAllMenu_WhenFetchEagerMenuGroup_AndSelectMenuProductsInBatchSize() {
        // When
        List<Menu> actual = menuRepository.findAll();

        // Then
        List<MenuResponse> menuResponses = actual.stream()
            .map(MenuResponse::from)
            .collect(Collectors.toList());

        MenuResponse menuResponse = menuResponses.stream()
            .filter(it -> it.getId().equals(커플_냉삼_메뉴.getId()))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);

        assertThat(menuResponse.getMenuProducts()).extracting("productId")
            .containsExactly(물냉면.getId(), 비빔냉면.getId(), 삼겹살.getId());
    }
}

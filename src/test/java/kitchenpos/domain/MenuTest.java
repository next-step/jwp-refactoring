package kitchenpos.domain;

import kitchenpos.dao.MenuGroupRepository;
import kitchenpos.dao.MenuProductRepository;
import kitchenpos.dao.MenuRepository;
import kitchenpos.dao.ProductRepository;
import kitchenpos.domain.validator.MenuValidator;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
public class MenuTest {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MenuGroupRepository menuGroupRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private MenuProductRepository menuProductRepository;

    Product 마늘치킨;
    Product 양념치킨;
    MenuGroup 점심메뉴;
    MenuProduct 점심특선_마늘치킨;
    MenuProduct 점심특선_양념치킨;

    @BeforeEach
    void beforeEach() {
        마늘치킨 = productRepository.save(new Product("마늘치킨", 1000));
        양념치킨 = productRepository.save(new Product("양념치킨", 1000));
        점심메뉴 = menuGroupRepository.save(MenuGroup.builder()
                                                 .name("두마리메뉴")
                                                 .build());
        점심특선_마늘치킨 = new MenuProduct(마늘치킨, 1);
        점심특선_양념치킨 = new MenuProduct(양념치킨, 1);
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void save() {
        // given
        Menu menu = new Menu("점심특선", 2000, 점심메뉴, Arrays.asList(점심특선_마늘치킨, 점심특선_양념치킨));

        // when
        Menu 점심특선 = menuRepository.save(menu);

        // then
        assertThat(점심특선.getId()).isNotNull();
    }

    @DisplayName("0원 보다 작은 금액으로 메뉴를 생성할 수 없다.")
    @Test
    void save_throwsException_givenPriceLessThanZero() {
        // when
        // then
        assertThatThrownBy(() -> new Menu("점심특선", -2000, 점심메뉴, Arrays.asList(점심특선_마늘치킨, 점심특선_양념치킨)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴를 구성하는 상품의 가격 * 수량의 합계는 메뉴 가격과 일치해야 한다.")
    @Test
    void save_throwsException_givenWrongPrice() {
        // given
        MenuRequest request = new MenuRequest("점심특선", BigDecimal.valueOf(4000), 점심메뉴.getId()
                , Arrays.asList(new MenuProductRequest(마늘치킨.getId(), 1), new MenuProductRequest(양념치킨.getId(), 1)));

        // when
        // then
        assertThatThrownBy(() -> new MenuValidator(menuRepository, menuProductRepository, productRepository).validateMenuProducts(request))
                .isInstanceOf(IllegalArgumentException.class);
    }
}

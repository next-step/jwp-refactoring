package kitchenpos.menu.application;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.domain.MenuProductBag;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.menu.application.MenuGroupServiceTest.메뉴_그룹_추천_메뉴;
import static kitchenpos.menu.domain.MenuProductTest.메뉴_상품;
import static kitchenpos.menu.domain.MenuTest.메뉴;
import static kitchenpos.product.domain.ProductTest.상품_콜라;
import static kitchenpos.product.domain.ProductTest.상품_통다리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@Transactional
@SpringBootTest
@DisplayName("메뉴 테스트 ")
public class MenuServiceTest {

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuService menuService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final Menu 메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(productRepository.save(상품_통다리()), 5),
                        메뉴_상품(productRepository.save(상품_콜라()), 1))));
        //when:
        final Menu 저장된_메뉴 = menuService.create(메뉴);
        //then:
        assertThat(저장된_메뉴).isEqualTo(메뉴);
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 0보다 작은 경우")
    @Test
    void 생성_예외_메뉴의_가격이_0보다_작은_경우() {
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.valueOf(-1)),
                menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(productRepository.save(상품_통다리()), 5),
                        메뉴_상품(productRepository.save(상품_콜라()), 1))))));
    }

    @DisplayName("생성 예외 - 메뉴 그룹이 존재하지 않는 경우")
    @Test
    void 생성_예외_메뉴_그룹이_존재하지_않는_경우() {
        //given:
        final Menu 메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                메뉴_그룹_추천_메뉴().getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(productRepository.save(상품_통다리()), 5),
                        메뉴_상품(productRepository.save(상품_콜라()), 1))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(메뉴));
    }

    @DisplayName("생성 예외 - 상품이 존재하지 않는 경우")
    @Test
    void 생성_예외_상품이_존재하지_않는_경우() {
        final Menu 메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(상품_통다리(), 5),
                        메뉴_상품(상품_콜라(), 1))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(메뉴));
    }

    @DisplayName("생성 예외 - 메뉴의 가격이 상품 목록 가격의 합보다 큰 경우")
    @Test
    void 생성_예외_메뉴의_가격이_상품_목록의_가격_합보다_큰_경우() {
        //given:
        final int expensivePrice = 99999;
        final Menu 메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.valueOf(expensivePrice)),
                menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(productRepository.save(상품_통다리()), 5),
                        메뉴_상품(productRepository.save(상품_콜라()), 1))));
        //when,then:
        assertThatIllegalArgumentException().isThrownBy(() -> menuService.create(메뉴));
    }

    @DisplayName("목록 조회 성공")
    @Test
    void 목록_조회_성공() {
        //given:
        final Menu 메뉴 = 메뉴(Name.from("자메이카 통다리 1인 세트"),
                Price.from(BigDecimal.ONE),
                menuGroupRepository.save(메뉴_그룹_추천_메뉴()).getId(),
                MenuProductBag.from(Arrays.asList(
                        메뉴_상품(productRepository.save(상품_통다리()), 5),
                        메뉴_상품(productRepository.save(상품_콜라()), 1))));
        //when:
        final Menu 저장된_메뉴 = menuService.create(메뉴);
        //then:
        assertThat(menuService.list()).contains(저장된_메뉴);
    }
}

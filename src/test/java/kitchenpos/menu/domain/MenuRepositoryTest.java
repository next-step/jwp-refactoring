package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
public class MenuRepositoryTest {
    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리메뉴"));
        Menu expected = new Menu("후라이드치킨", BigDecimal.valueOf(12000), menuGroup);

        // when
        Menu actual = menuRepository.save(expected);

        // then
        assertThat(actual == expected).isTrue();
    }

    @DisplayName("메뉴 가격이 0미만인 경우 예외")
    @Test
    void validMenuPrice() {
        MenuGroup menuGroup = menuGroupRepository.save(new MenuGroup("한마리메뉴"));

        assertThatThrownBy(() -> {
            Menu expected = new Menu("후라이드치킨", BigDecimal.valueOf(-1), menuGroup);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("없는 메뉴 그룹인 경우 예외")
    @Test
    void validExistMenuGroup() {
        Menu expected = new Menu("후라이드치킨", BigDecimal.valueOf(12000), new MenuGroup("튀김메뉴"));

        assertThatThrownBy(() -> {
            Menu actual = menuRepository.save(expected);
        }).isInstanceOf(DataIntegrityViolationException.class);
    }

    @DisplayName("메뉴 가격이 메뉴별 상품의 총 가격보다 클 경우 예외")
    @Test
    void validMenuPrice2() {
        Product 짬뽕 = productRepository.save(new Product("짬뽕", BigDecimal.valueOf(8000)));
        Product 탕수육 = productRepository.save(new Product("탕수육", BigDecimal.valueOf(15000)));
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        Menu A세트 = new Menu("A세트",BigDecimal.valueOf(400000), 세트_메뉴);
        List<MenuProduct> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProduct(A세트, 짬뽕, 2), new MenuProduct(A세트, 탕수육, 1));

        assertThatThrownBy(() -> {
            A세트.addAllMenuProducts(짬뽕_2개_탕수육_1개);
        }).isInstanceOf(IllegalArgumentException.class);
    }
}

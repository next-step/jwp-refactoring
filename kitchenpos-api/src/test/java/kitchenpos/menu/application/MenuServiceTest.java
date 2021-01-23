package kitchenpos.menu.application;


import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class MenuServiceTest {
    private Product 짬뽕;
    private Product 탕수육;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuGroupRepository menuGroupRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MenuProductRepository menuProductRepository;

    @BeforeEach
    void setUp() {
        짬뽕 = productRepository.save(new Product("짬뽕", BigDecimal.valueOf(8000)));
        탕수육 = productRepository.save(new Product("탕수육", BigDecimal.valueOf(15000)));
    }

    @AfterEach
    void cleanup() {
        menuProductRepository.deleteAllInBatch();
        productRepository.deleteAllInBatch();
        menuRepository.deleteAllInBatch();
        menuGroupRepository.deleteAllInBatch();
    }

    @DisplayName("메뉴 등록")
    @Test
    void create() {
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        List<MenuProductRequest> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProductRequest(짬뽕.getId(), 2), new MenuProductRequest(탕수육.getId(), 1));
        MenuRequest menuRequest = new MenuRequest("A세트", BigDecimal.valueOf(30000), 세트_메뉴.getId(), 짬뽕_2개_탕수육_1개);

        MenuResponse menuResponse = menuService.create(menuRequest);

        assertThat(menuResponse.getMenuProducts())
                .extracting("menuId")
                .containsExactly(menuResponse.getId(), menuResponse.getId());
    }

    @DisplayName("없는 메뉴 그룹인 경우 예외")
    @Test
    void validExistMenuGroup() {
        List<MenuProductRequest> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProductRequest(짬뽕.getId(), 2), new MenuProductRequest(탕수육.getId(), 1));
        MenuRequest menuRequest = new MenuRequest("A세트", BigDecimal.valueOf(30000), 10L, 짬뽕_2개_탕수육_1개);

        Assertions.assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(EntityNotFoundException.class);
    }

    @DisplayName("메뉴 가격이 0 미만인 경우 예외")
    @Test
    void validMenuPrice() {
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        List<MenuProductRequest> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProductRequest(짬뽕.getId(), 2), new MenuProductRequest(탕수육.getId(), 1));
        MenuRequest menuRequest = new MenuRequest("A세트", BigDecimal.valueOf(-1), 세트_메뉴.getId(), 짬뽕_2개_탕수육_1개);

        Assertions.assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 가격이 메뉴별 상품의 총 가격보다 클 경우")
    @Test
    void validMenuPrice2() {
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        List<MenuProductRequest> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProductRequest(짬뽕.getId(), 2), new MenuProductRequest(탕수육.getId(), 1));
        MenuRequest menuRequest = new MenuRequest("A세트", BigDecimal.valueOf(100000), 세트_메뉴.getId(), 짬뽕_2개_탕수육_1개);

        Assertions.assertThatThrownBy(() -> {
            menuService.create(menuRequest);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 주문 목록 조회")
    @Test
    void list() {
        MenuGroup 세트_메뉴 = menuGroupRepository.save(new MenuGroup("세트 메뉴"));
        List<MenuProductRequest> 짬뽕_2개_탕수육_1개 = Arrays.asList(new MenuProductRequest(짬뽕.getId(), 2), new MenuProductRequest(탕수육.getId(), 1));
        MenuRequest menuRequest = new MenuRequest("A세트", BigDecimal.valueOf(30000), 세트_메뉴.getId(), 짬뽕_2개_탕수육_1개);
        MenuResponse createdMenuResponse = menuService.create(menuRequest);

        assertThat(menuService.list().size()).isEqualTo(1);

    }
}
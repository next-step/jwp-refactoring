package kitchenpos.study;

import kitchenpos.menu.domain.entity.*;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.domain.entity.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
public class RelationMappingTest {
    @Autowired
    MenuProductRepository menuProductRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    ProductRepository productRepository;

    @Test
    void relationMapping2() {
        MenuProductRequest menuProductRequest = new MenuProductRequest(null,4L,2L);
        MenuRequest menuRequest = new MenuRequest("훈제치킨",
                BigDecimal.valueOf(13000),
                2L,
                Arrays.asList(menuProductRequest));

        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(IllegalArgumentException::new);

        List<Long> productsIds = menuRequest.getMenuProducts().stream().map(MenuProductRequest::getProductId).collect(Collectors.toList());

        List<MenuProduct> menuProductList = productRepository.findAllById(productsIds)
                .stream().map(product -> new MenuProduct(product, 1)).collect(Collectors.toList());

//        for (MenuProductRequest productRequest: menuRequest.getMenuProducts()){
//            Product product = productRepository.findById(productRequest.getProductId())
//                    .orElseThrow(IllegalArgumentException::new);
//            MenuProduct menuProduct = new MenuProduct(product, productRequest.getQuantity());
//
//            menuProductList.add(menuProduct);
//        }

        Menu menu = Menu.of(menuRequest.getName(),menuRequest.getPrice(),menuGroup,menuProductList);

        menuRepository.save(menu);

        Menu menu1 = menuRepository.findById(menu.getId()).orElseThrow(IllegalArgumentException::new);
        System.out.println("menu1 = " + menu1);
    }
}
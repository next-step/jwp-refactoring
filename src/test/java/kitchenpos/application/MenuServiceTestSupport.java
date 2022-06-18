package kitchenpos.application;

import kitchenpos.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
public class MenuServiceTestSupport {

    private final ProductRepository productRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuRepository menuRepository;

    public MenuServiceTestSupport(ProductRepository productRepository, MenuGroupRepository menuGroupRepository,
                                  MenuRepository menuRepository) {
        this.productRepository = productRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuRepository = menuRepository;
    }

    public MenuEntity 신메뉴_강정치킨_가져오기() {
        ProductEntity 강정치킨 = productRepository.save(new ProductEntity("강정치킨", BigDecimal.valueOf(15_000)));
        MenuGroupEntity 신메뉴 = menuGroupRepository.save(new MenuGroupEntity("신메뉴"));
        MenuEntity 강정치킨_메뉴 = new MenuEntity("강정치킨", BigDecimal.valueOf(15_000), 신메뉴.getId());
        강정치킨_메뉴.addMenuProducts(Arrays.asList(new MenuProductEntity(강정치킨, 1L)));
        return menuRepository.save(강정치킨_메뉴);
    }
}

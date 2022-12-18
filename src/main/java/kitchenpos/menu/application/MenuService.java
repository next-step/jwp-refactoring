package kitchenpos.menu.application;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.*;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.repository.MenuRepository;
import kitchenpos.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class MenuService {
    public static final String PRICE_NOT_NULL_EXCEPTION_MESSAGE = "가격은 필수입니다.";
    public static final String MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE = "메뉴 그룹이 존재하지 않습니다.";
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final ProductRepository productRepository;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupRepository menuGroupRepository,
            final ProductRepository productRepository
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public MenuResponse create(final MenuCreateRequest request) {


        final List<MenuProduct> menuProducts = request.getMenuProducts();
        validateExistMenuProducts(menuProducts);

        Menu menu = new Menu(new Name(request.getName()), new Price(request.getPrice()), findMenuGroup(request), menuProducts);

        return new MenuResponse(menuRepository.save(menu));
    }

    private MenuGroup findMenuGroup(MenuCreateRequest request) {
        if (request.getMenuGroupId() == null) {
            throw new IllegalArgumentException(MENU_GROUP_NOT_EXIST_EXCEPTION_MESSAGE);
        }
        return menuGroupRepository.findById(request.getMenuGroupId()).orElseThrow(EntityNotFoundException::new);
    }

    private void validateExistMenuProducts(List<MenuProduct> menuProducts) {
        for (final MenuProduct menuProduct : menuProducts) {
            productRepository.findById(menuProduct.getProduct().getId())
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    public List<Menu> list() {
        return menuRepository.findAll();
    }
}


package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.domainService.MenuProductDomainService;
import kitchenpos.dto.dto.MenuProductDTO;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuResponse;
import kitchenpos.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuProductDomainService menuProductDomainService;

    public MenuService(MenuRepository menuRepository,
        MenuProductDomainService menuProductDomainService) {
        this.menuRepository = menuRepository;
        this.menuProductDomainService = menuProductDomainService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        final List<MenuProductDTO> menuProducts = menuRequest.getMenuProducts();

        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(),
            menuRequest.getMenuGroupId());

        menuProductDomainService.validateComponentForCreateMenu(menuRequest);

        for (MenuProductDTO menuProductDTO : menuProducts) {
            menu.addMenuProduct(
                new MenuProduct(menu, menuProductDTO.getProductId(), menuProductDTO.getQuantity()));
        }
        return MenuResponse.of(menuRepository.save(menu));
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();
        return menus.stream()
            .map(MenuResponse::of)
            .collect(Collectors.toList());
    }
}

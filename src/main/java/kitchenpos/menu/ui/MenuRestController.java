package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
public class MenuRestController {
    private final MenuService menuService;

    public MenuRestController(final MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/api/menus")
    public ResponseEntity<MenuResponse> create(@RequestBody final MenuRequest request) {
        final Menu created = menuService.create(request.toEntity());
        final URI uri = URI.create("/api/menus/" + created.getId());
        return ResponseEntity.created(uri)
                .body(new MenuResponse(created))
                ;
    }

    @GetMapping("/api/menus")
    public ResponseEntity<List<MenuResponse>> list() {
        return ResponseEntity.ok()
                .body(menuService.list().stream()
                        .map(MenuResponse::new)
                        .collect(toList()))
                ;
    }

    static class MenuRequest {
        private String name;
        private long price;
        private Long menuGroupId;
        private List<MenuProductRequest> menuProducts;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public Long getMenuGroupId() {
            return menuGroupId;
        }

        public void setMenuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
        }

        public List<MenuProductRequest> getMenuProducts() {
            return menuProducts;
        }

        public void setMenuProducts(List<MenuProductRequest> menuProducts) {
            this.menuProducts = menuProducts;
        }

        public Menu toEntity() {
            Menu menu = new Menu(name, price, menuGroupId);
            menuProducts.stream()
                    .map(MenuProductRequest::toEntity)
                    .forEach(menu::addMenuProduct);
            return menu;
        }
    }

    static class MenuProductRequest {
        private Long productId;
        private long quantity;

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }

        public MenuProduct toEntity() {
            return new MenuProduct(productId, quantity);
        }
    }

    static class MenuResponse {
        private Long id;
        private String name;
        private long price;
        private Long menuGroupId;
        private List<MenuProductResponse> menuProducts;

        public MenuResponse(Menu menu) {
            id = menu.getId();
            name = menu.getName();
            price = menu.getPrice().getValue();
            menuGroupId = menu.getMenuGroupId();
            menuProducts = menu.getMenuProducts().getMenuProducts().stream()
                    .map(MenuProductResponse::new)
                    .collect(toList());
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getPrice() {
            return price;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public Long getMenuGroupId() {
            return menuGroupId;
        }

        public void setMenuGroupId(Long menuGroupId) {
            this.menuGroupId = menuGroupId;
        }

        public List<MenuProductResponse> getMenuProducts() {
            return menuProducts;
        }

        public void setMenuProducts(List<MenuProductResponse> menuProducts) {
            this.menuProducts = menuProducts;
        }
    }

    static class MenuProductResponse {
        private Long seq;
        private Long productId;
        private long quantity;

        public MenuProductResponse(MenuProduct menuProduct) {
            seq = menuProduct.getSeq();
            productId = menuProduct.getProductId();
            quantity = menuProduct.getQuantity();
        }

        public Long getSeq() {
            return seq;
        }

        public void setSeq(Long seq) {
            this.seq = seq;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public long getQuantity() {
            return quantity;
        }

        public void setQuantity(long quantity) {
            this.quantity = quantity;
        }
    }
}

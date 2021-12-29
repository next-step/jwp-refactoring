package kitchenpos.menu.application;

import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.application.ProductService;
import kitchenpos.product.domain.Product;

@Component
public class MenuValidator {
    private ProductService productService;
    
    public MenuValidator(ProductService productService) {
        this.productService = productService;
    }
    
    public void checkTotalPrice(Menu menu) {
        long sumProductPrice = 0;
        for (MenuProduct menuProduct : menu.getMenuProducts()) {
            Product product = productService.findById(menuProduct.getProductId());
            sumProductPrice += product.getPrice().getValue() * menuProduct.getQuantity();
        }
        if (menu.getPrice().getValue() > sumProductPrice) {
            throw new IllegalArgumentException("메뉴 가격이 상품 가격의 합보다 큽니다");
        }
    }
    
    public void checkProducts(List<Long> productIds) {
        List<Product> products = productService.findAllByIds(productIds);
        
        if (products.size() != productIds.size()) {
            new IllegalArgumentException("메뉴에는 저장된 상품만 등록할 수 있습니다");
        }
    }
}

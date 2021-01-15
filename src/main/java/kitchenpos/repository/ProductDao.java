package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.Product;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(idClass = Long.class, domainClass = Product.class)
public interface ProductDao {
    Product save(Product entity);

    Optional<Product> findById(Long id);

    List<Product> findAll();
}

package kitchenpos;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.DisplayName;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "kitchenpos")
public class ArchTest {

    @DisplayName("패키지 의존성 검사")
    @com.tngtech.archunit.junit.ArchTest
    public static void checkPackageDependencies(JavaClasses classes) {
        ArchRule freeOfCycles = slices().matching("kitchenpos.(*)..")
                .should().beFreeOfCycles();
        freeOfCycles.check(classes);
    }
}

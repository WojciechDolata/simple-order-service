package pl.wdolata.simpleorderservice

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses

import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.library.dependencies.SlicesRuleDefinition
import org.springframework.web.bind.annotation.RestController
import spock.lang.Shared
import spock.lang.Specification

class ArchitectureSpec extends Specification {

    private static final String BASE_PACKAGE = 'pl.wdolata.simpleorderservice..'

    @Shared
    JavaClasses classes = new ClassFileImporter()
        .withImportOption(new ImportOption.DoNotIncludeTests())
        .importPackages(BASE_PACKAGE)

    def 'should keep controllers in the api package'() {
        when:
        classes().that().areAnnotatedWith(RestController)
            .should().resideInAPackage(BASE_PACKAGE + 'api..')
            .check(classes)

        then:
        noExceptionThrown()
    }

    def 'should keep the domain independent of adapters and api'() {
        when:
        noClasses().that().resideInAPackage(BASE_PACKAGE + 'domain..')
            .should().dependOnClassesThat().resideInAnyPackage(BASE_PACKAGE + 'adapters..', BASE_PACKAGE + 'api..')
            .check(classes)

        then:
        noExceptionThrown()
    }

    def 'should keep adapters independent of each other'() {
        when:
        SlicesRuleDefinition.slices()
            .matching(BASE_PACKAGE + 'adapters.(*)..')
            .should().notDependOnEachOther()
            .check(classes)

        then:
        noExceptionThrown()
    }

    def 'should keep the api independent of adapters'() {
        when:
        noClasses().that().resideInAPackage(BASE_PACKAGE + 'api..')
            .should().dependOnClassesThat().resideInAPackage(BASE_PACKAGE + 'adapters..')
            .check(classes)

        then:
        noExceptionThrown()
    }

}

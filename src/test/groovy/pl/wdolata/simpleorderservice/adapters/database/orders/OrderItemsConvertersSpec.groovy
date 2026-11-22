package pl.wdolata.simpleorderservice.adapters.database.orders

import org.postgresql.util.PGobject
import pl.wdolata.simpleorderservice.adapters.database.orders.converters.OrderItemsReadingConverter
import pl.wdolata.simpleorderservice.adapters.database.orders.converters.OrderItemsWritingConverter
import pl.wdolata.simpleorderservice.fixtures.entities.OrderEntities
import pl.wdolata.simpleorderservice.support.JsonUtils
import spock.lang.Specification
import tools.jackson.core.JacksonException
import tools.jackson.databind.json.JsonMapper

class OrderItemsConvertersSpec extends Specification {

    def jsonMapper = JsonMapper.builder().build()
    def writingConverter = new OrderItemsWritingConverter(jsonMapper)
    def readingConverter = new OrderItemsReadingConverter(jsonMapper)

    def 'should write items as a flat jsonb array'() {
        when:
        def result = writingConverter.convert(OrderEntities.items())

        then:
        result.type == 'jsonb'
        JsonUtils.parse(result.value) == [
            [sku: 'BOOK-1', quantity: 2, unitPrice: 30.00],
            [sku: 'PEN-7', quantity: 1, unitPrice: 40.00]
        ]
    }

    def 'should read items from a jsonb array'() {
        given:
        def pgObject = new PGobject(type: 'jsonb', value: '[{"sku":"BOOK-1","quantity":2,"unitPrice":30.00}]')

        when:
        def result = readingConverter.convert(pgObject)

        then:
        result.items().size() == 1
        result.items()[0].sku() == 'BOOK-1'
        result.items()[0].quantity() == 2
        result.items()[0].unitPrice() == 30.00G
    }

    def 'should survive a write and read round trip'() {
        expect:
        readingConverter.convert(writingConverter.convert(OrderEntities.items())) == OrderEntities.items()
    }

    def 'should refuse to read a jsonb value that is not an array of items'() {
        given:
        def pgObject = new PGobject(type: 'jsonb', value: '{"sku":"BOOK-1"}')

        when:
        readingConverter.convert(pgObject)

        then:
        thrown(JacksonException)
    }

}

package pl.wdolata.simpleorderservice.support

import groovy.json.JsonGenerator
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

class JsonUtils {

    private static final JsonGenerator GENERATOR = new JsonGenerator.Options().excludeNulls().build()

    static String toJsonString(Object json) {
        return GENERATOR.toJson(json)
    }

    static Object parse(String json) {
        return new JsonSlurper().parseText(json)
    }

    /**
     * Reads a JSON fixture from the classpath and lets the test tweak it, e.g.
     * <pre>parseJsonFile('api/request.json') { it['couponCode'] = 'OTHER' }</pre>
     */
    static String parseJsonFile(String path, Closure modifications = {}) {
        def stream = JsonUtils.classLoader.getResourceAsStream(path)
        if (stream == null) {
            throw new IllegalArgumentException("JSON fixture not found on classpath: $path")
        }
        def json = new JsonSlurper().parse(stream)
        modifications.call(json)
        return GENERATOR.toJson(json)
    }

    static String pretty(Object value) {
        return JsonOutput.prettyPrint(value instanceof String ? value : toJsonString(value))
    }

}

package pl.wdolata.simpleorderservice.fixtures

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions

@Component
class OrderApiFixture {

    private static final String ORDERS_PATH = '/orders'

    @Autowired
    private MockMvc mockMvc

    ResultActions placeOrder(String requestBody) {
        return mockMvc.perform(
            post(ORDERS_PATH)
                .content(requestBody)
                .header('Content-Type', 'application/json')
        )
    }

}

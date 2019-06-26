package uk.co.suskins.hrvsm.api.Controllers

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import spock.lang.Specification
import uk.co.suskins.hrvsm.service.impl.TwitterServiceImpl

class HSSMTwitterRestApiTest extends Specification {
    def mockTwitterService

    MockMvc mockMvc

    def setup() {
        mockTwitterService = Mock(TwitterServiceImpl)
        def HRVSMTwitterRestApi = new HSSMTwitterRestApi(mockTwitterService)
        mockMvc = MockMvcBuilders.standaloneSetup(HRVSMTwitterRestApi).build()
    }

    def "twitterStart endpoint, success"() {
        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/start")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()
        then:
            1 * mockTwitterService.twitterStartStream()
            result.response.getStatus() == 200
    }

    def "twitterStop endpoint, success"() {
        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/stop")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()
        then:
            1 * mockTwitterService.twitterStopStream()
            result.response.getStatus() == 200

    }

    def "twitterStatus endpoint success"() {
        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/twitter/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()
        then:
            1 * mockTwitterService.getTwitterConnectionStatus()
            result.response.getStatus() == 200
    }


    def "twitterStatus endpoint stubbed return false"() {
        given:
            mockTwitterService.getTwitterConnectionStatus() >> false

        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/twitter/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()
        then:
            1 * mockTwitterService.getTwitterConnectionStatus()
            result.response.getContentAsString() == "false"
            result.response.getStatus() == 200
            result.response.getHeader("Content-Type") == "application/json;charset=UTF-8"
    }

    def "twitterStatus endpoint stubbed return true"() {
        given:
            mockTwitterService.getTwitterConnectionStatus() >> true
        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/twitter/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()
        then:
            result.response.getContentAsString() == "true"
            result.response.getStatus() == 200
            result.response.getHeader("Content-Type") == "application/json;charset=UTF-8"
    }
}

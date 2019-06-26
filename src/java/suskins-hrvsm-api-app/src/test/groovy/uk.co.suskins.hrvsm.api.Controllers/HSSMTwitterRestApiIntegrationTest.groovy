package uk.co.suskins.hrvsm.api.Controllers

import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException
import spock.lang.Specification
import uk.co.suskins.hrvsm.repository.TweetsRepository
import uk.co.suskins.hrvsm.service.Config.TwitterConfig
import uk.co.suskins.hrvsm.service.impl.TwitterServiceImpl
import uk.co.suskins.hrvsm.service.nlp.impl.TwitterPreprocessorServiceImpl

class HSSMTwitterRestApiIntegrationTest extends Specification {
    def twitterService

    MockMvc mockMvc

    def setup() {
        def twitterConfig = new TwitterConfig()
        twitterConfig.setTwitterAccessSecret("accessSecret")
        twitterConfig.setTwitterAccessToken("accessToken")
        twitterConfig.setTwitterConsumerKey("consumerKey")
        twitterConfig.setTwitterConsumerSecret("consumerSecret")

        twitterService = new TwitterServiceImpl(twitterConfig,
                Mock(TwitterPreprocessorServiceImpl),
                Mock(TweetsRepository))

        def HRVSMTwitterRestApi = new HSSMTwitterRestApi(twitterService)

        mockMvc = MockMvcBuilders.standaloneSetup(HRVSMTwitterRestApi).build()
    }

    def "twitterStatus endpoint, returning false"() {
        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.get("/twitter/status")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()

        then:
            result.response.getContentAsString() == "false"
            result.response.getStatus() == 200
            result.response.getHeader("Content-Type") == "application/json;charset=UTF-8"
    }

    def "twitterStatus endpoint, returning true"() {
        given:
            twitterService.setTwitterConnectionStatus(true)

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

    def "twitterStart endpoint success, exception not thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(false)

        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/start")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()

        then:
            notThrown NestedServletException
            result.response.getContentAsString() == ""
            result.response.getStatus() == 200
    }

    def "twitterStart endpoint fail, exception thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(true)

        when:
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/start")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()

        then:
            thrown NestedServletException
    }

    def "twitterStop endpoint success, exception not thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(true)

        when:
            def result = mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/stop")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()

        then:
            notThrown NestedServletException
            result.response.getContentAsString() == ""
            result.response.getStatus() == 200
    }

    def "twitterStop integration fail, exception thrown"() {
        given:
            twitterService.setTwitterConnectionStatus(false)

        when:
            mockMvc
                    .perform(MockMvcRequestBuilders.post("/twitter/stop")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                    .andReturn()

        then:
            thrown NestedServletException
    }
}

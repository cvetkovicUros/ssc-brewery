package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BeerRestControllerIT extends BaseTestClass{

    @Test
    void findBeers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer"))
                .andExpect(status().isOk());
    }

    @Test
    void findBeersById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/beer/97df0c39-90c4-4ae0-b663-453e8e19c311"))
                .andExpect(status().isOk());
    }
}

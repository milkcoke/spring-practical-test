package kioskapp.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kioskapp.service.order.OrderService;
import kioskapp.service.order.dto.OrderCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrderController.class)
class OrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private OrderService orderService;

  @Test
  @DisplayName("상품 번호 누락시 주문에 실패한다.")
  void postOrderWithoutProductNumber() throws Exception{
    // given
    List<String> productNumbers = List.of();
    OrderCreateRequest orderRequest = OrderCreateRequest.builder()
        .productNumbers(productNumbers)
        .build();

    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/orders")
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("product number is required"))
        .andExpect(jsonPath("$.data").isEmpty());
  }



  @Test
  @DisplayName("2개 상품을 주문한다.")
  void postOrder() throws Exception {
    // given
    List<String> productNumbers = List.of("001", "002");
    OrderCreateRequest orderRequest = OrderCreateRequest.builder()
        .productNumbers(productNumbers)
        .build();
    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/orders")
                .content(objectMapper.writeValueAsString(orderRequest))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("product number is required"))
        .andExpect(jsonPath("$.data").isEmpty());
  }
}
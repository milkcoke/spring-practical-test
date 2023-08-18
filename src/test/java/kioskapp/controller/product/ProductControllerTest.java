package kioskapp.controller.product;

import kioskapp.ControllerTestSupport;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import kioskapp.controller.product.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends ControllerTestSupport {

  @Test
  @DisplayName("판매 상품 전체를 조회한다.")
  public void getAllProducts() throws Exception {
    // given

    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.get("/api/v1/products")
        )
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.code").value(200))
        .andExpect(jsonPath("$.status").value("OK"))
        .andExpect(jsonPath("$.message").value("OK"))
        .andExpect(jsonPath("$.data").isArray());
  }
  @Test
  @DisplayName("첫 신규 상품을 등록한다.")
  public void postProduct() throws Exception {
    // given
    ProductCreateRequest postAmericanoReq = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(4000)
        .build();

    // perform: API Call
    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsString(postAmericanoReq))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value("CREATED"))
        .andExpect(jsonPath("$.code").value("201"))
        .andExpect(jsonPath("$.message").value("CREATED"))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("상품명을 누락하여 상품 등록에 실패한다.")
  public void postProductWithNoName() throws Exception {
    // given
    ProductCreateRequest postAmericanoReqWithoutNameReq = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .price(4000)
        .build();
    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsString(postAmericanoReqWithoutNameReq))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("product name is required"))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("상품 종류를 누락하여 상품 등록에 실패한다.")
  public void postProductWithNoType() throws Exception {
    // given
    ProductCreateRequest postAmericanoReqWithoutNameReq = ProductCreateRequest.builder()
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(4000)
        .build();
    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsString(postAmericanoReqWithoutNameReq))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("product type is required"))
        .andExpect(jsonPath("$.data").isEmpty());
  }


  @Test
  @DisplayName("상품 판매상태를 누락하여 상품 등록에 실패한다.")
  public void postProductWithNoSellingStatus() throws Exception{
    // given
    ProductCreateRequest postAmericanoReqWithoutSellingStatus = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .name("아메리카노")
        .price(4000)
        .build();

    // when // then
    mockMvc.perform(
        MockMvcRequestBuilders.post("/api/v1/products")
            .content(objectMapper.writeValueAsString(postAmericanoReqWithoutSellingStatus))
            .contentType(MediaType.APPLICATION_JSON)
    )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("product selling status is required"))
        .andExpect(jsonPath("$.data").isEmpty());
  }

  @Test
  @DisplayName("상품 가격 음수값 입력시 상품 등록에 실패한다.")
  public void postProductPriceWithNegative() throws Exception{
    // given
    ProductCreateRequest postAmericanoWithNegativePriceReq = ProductCreateRequest.builder()
        .type(ProductType.HANDMADE)
        .sellingStatus(ProductSellingStatus.SELLING)
        .name("아메리카노")
        .price(-1)
        .build();
    // when // then
    mockMvc.perform(
            MockMvcRequestBuilders.post("/api/v1/products")
                .content(objectMapper.writeValueAsString(postAmericanoWithNegativePriceReq))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("400"))
        .andExpect(jsonPath("$.status").value("BAD_REQUEST"))
        .andExpect(jsonPath("$.message").value("price should be greater than or equal to zero"))
        .andExpect(jsonPath("$.data").isEmpty());
  }
}
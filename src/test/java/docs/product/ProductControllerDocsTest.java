package docs.product;

import docs.RestDocsSupport;
import kioskapp.controller.product.ProductController;
import kioskapp.controller.product.dto.ProductCreateRequest;
import kioskapp.domain.product.ProductSellingStatus;
import kioskapp.domain.product.ProductType;
import kioskapp.service.product.ProductService;
import kioskapp.service.product.dto.ProductCreateServiceRequest;
import kioskapp.service.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * SpringBoot 와 무관한 standalone test
 */
public class ProductControllerDocsTest extends RestDocsSupport {
  private final ProductService productService= mock(ProductService.class);
  @Override
  protected Object initController() {
    return new ProductController(productService);
  }

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
        .andExpect(jsonPath("$.data").isArray())
        .andDo(document("product-readAll",
            preprocessResponse(prettyPrint()),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER)
                    .description("HTTP 응답 코드"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("응답 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("응답 메시지"),
                fieldWithPath("data").type(JsonFieldType.ARRAY)
                    .description("응답 데이터")
            )
            ));
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

    given(productService.createProduct(any(ProductCreateServiceRequest.class)))
        .willReturn(ProductResponse.builder()
            .id(1L)
            .productNumber("001")
            .type(ProductType.HANDMADE)
            .sellingStatus(ProductSellingStatus.SELLING)
            .name("아메리카노")
            .price(4000)
            .build()
        );

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
        .andDo(document("product-post",
            preprocessRequest(prettyPrint()),
            preprocessResponse(prettyPrint()),
            requestFields(
                fieldWithPath("type").type(JsonFieldType.STRING)
                    .description("상품 종류"),
                fieldWithPath("sellingStatus").type(JsonFieldType.STRING)
                    .description("상품 판매 상태"),
                fieldWithPath("name").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            ),
            responseFields(
                fieldWithPath("code").type(JsonFieldType.NUMBER)
                    .description("HTTP 응답 코드"),
                fieldWithPath("status").type(JsonFieldType.STRING)
                    .description("응답 코드"),
                fieldWithPath("message").type(JsonFieldType.STRING)
                    .description("응답 메시지"),
                fieldWithPath("data").type(JsonFieldType.OBJECT)
                    .description("응답 데이터"),
                fieldWithPath("data.id").type(JsonFieldType.NUMBER)
                    .description("상품 ID"),
                fieldWithPath("data.productNumber").type(JsonFieldType.STRING)
                    .description("상품 번호"),
                fieldWithPath("data.type").type(JsonFieldType.STRING)
                    .description("상품 종류"),
                fieldWithPath("data.sellingStatus").type(JsonFieldType.STRING)
                    .optional()
                    .description("상품 판매 상태"),
                fieldWithPath("data.name").type(JsonFieldType.STRING)
                    .description("상품명"),
                fieldWithPath("data.price").type(JsonFieldType.NUMBER)
                    .description("상품 가격")
            )
        ));
  }


}

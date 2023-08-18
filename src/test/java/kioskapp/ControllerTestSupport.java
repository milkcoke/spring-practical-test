package kioskapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import kioskapp.controller.order.OrderController;
import kioskapp.controller.product.ProductController;
import kioskapp.service.order.OrderService;
import kioskapp.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

// SpringBoot Test 는 전체 Bean Context 를 띄운다면
// WebMVC Test 는 Controller 관련 Bean 만 올리는 가벼운 테스트 Annotation.
@WebMvcTest(controllers = {
    ProductController.class,
    OrderController.class
})
public abstract class ControllerTestSupport {

  @Autowired
  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @MockBean
  protected OrderService orderService;

  // Container 에 Mockito 로 만든 mock 객체를 넣어주는 역할
  // ProductService 대신 ProductService Mock 객체를 넣어줌.
  // 이거 없으면 WebMvcAnnotation 만으론 Product Service Bean 이 없어서 에러남.
  @MockBean
  protected ProductService productService;
}

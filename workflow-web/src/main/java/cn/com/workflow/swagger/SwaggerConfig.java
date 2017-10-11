package cn.com.workflow.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
@Configuration
@EnableSwagger2
@EnableWebMvc
@ComponentScan(basePackages="cn.com.workflow.controller.web.*")
public class SwaggerConfig extends WebMvcConfigurerAdapter{
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
        .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars*")
        .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
    
    @Bean
    public Docket taskDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("任务类服务")
                .select()
//                .paths(PathSelectors.ant("/task/**"))
                .apis(RequestHandlerSelectors.any())
                .build()
                .apiInfo(apiInfo());
    }
    
//    @Bean
//    public Docket processDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("流程类服务")
//                .select()
//                .paths(PathSelectors.ant("/process/**"))
//                .apis(RequestHandlerSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
//    
//    @Bean
//    public Docket queryDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .groupName("查询类服务")
//                .select()
//                .paths(PathSelectors.ant("/worklist/**"))
//                .apis(RequestHandlerSelectors.any())
//                .build()
//                .apiInfo(apiInfo());
//    }
    
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("工作流平台对外开放接口API文档")
                .description("工作流平台对外开放接口")
                .version("1.0.0")
                .termsOfServiceUrl("http://xxx.xxx.com")
                .license("LICENSE")
                .licenseUrl("http://xxx.xxx.com")
                .build();
    }
}

package dev.stiebo.openapi_generator_sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication(
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
@ComponentScan(
		basePackages = {"org.openapitools", "dev.stiebo.openapi_generator_sample"},
		nameGenerator = FullyQualifiedAnnotationBeanNameGenerator.class
)
public class OpenapiGeneratorSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpenapiGeneratorSampleApplication.class, args);
	}

}

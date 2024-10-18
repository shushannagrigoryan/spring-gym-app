package org.example.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.configuration.SpringDocSpecPropertiesConfiguration;
import org.springdoc.core.configuration.SpringDocUIConfiguration;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.webmvc.core.configuration.MultipleOpenApiSupportConfiguration;
import org.springdoc.webmvc.core.configuration.SpringDocWebMvcConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

public class AppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(final ServletContext servletContext) {

        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(WebMvcConfig.class);
        rootContext.setConfigLocation("org.example.config");

        servletContext.addListener(new ContextLoaderListener(rootContext));

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher",  new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
        encodingFilter.setInitParameter("encoding", "UTF-8");
        encodingFilter.setInitParameter("forceEncoding", "true");
        encodingFilter.addMappingForUrlPatterns(null, true, "/*");

        rootContext.register(this.getClass(),
                SpringDocConfiguration.class,
                SpringDocConfigProperties.class,
                SpringDocSpecPropertiesConfiguration.class,
                SpringDocWebMvcConfiguration.class,
                MultipleOpenApiSupportConfiguration.class,
                SwaggerConfig.class,
                SwaggerUiConfigProperties.class,
                SwaggerUiOAuthProperties.class,
                SpringDocUIConfiguration.class
        );

    }

}
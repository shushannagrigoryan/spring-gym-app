//package org.example.config;
//
//import jakarta.servlet.FilterRegistration;
//import jakarta.servlet.ServletContext;
//import jakarta.servlet.ServletRegistration;
//import org.springframework.web.WebApplicationInitializer;
//import org.springframework.web.context.ContextLoaderListener;
//import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
//import org.springframework.web.filter.CharacterEncodingFilter;
//import org.springframework.web.servlet.DispatcherServlet;
//
//public class MainWebAppInitializer implements WebApplicationInitializer {
//    @Override
//    public void onStartup(final ServletContext servletContext) {
//
//        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
//        rootContext.register(WebConfig.class);
//        rootContext.setConfigLocation("org.example.config");
//
//        servletContext.addListener(new ContextLoaderListener(rootContext));
//
//        ServletRegistration.Dynamic dispatcher =
//                servletContext.addServlet("dispatcher",  new DispatcherServlet(rootContext));
//        dispatcher.setLoadOnStartup(1);
//        dispatcher.addMapping("/");
//
//        FilterRegistration.Dynamic encodingFilter =
//                servletContext.addFilter("encoding-filter", new CharacterEncodingFilter());
//        encodingFilter.setInitParameter("encoding", "UTF-8");
//        encodingFilter.setInitParameter("forceEncoding", "true");
//        encodingFilter.addMappingForUrlPatterns(null, true, "/*");
//    }
//}
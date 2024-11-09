package configtest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.example.auth.AuthInterceptor;
import org.example.config.WebMvcConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@ExtendWith(MockitoExtension.class)
public class WebMvcConfigTest {
    @Mock
    private AuthInterceptor authInterceptor;
    @Mock
    private InterceptorRegistry registry;
    @InjectMocks
    private WebMvcConfig webMvcConfig;

    @Test
    public void testAddInterceptors() {
        //given
        InterceptorRegistration interceptorRegistration = mock(InterceptorRegistration.class);
        when(registry.addInterceptor(authInterceptor)).thenReturn(interceptorRegistration);

        //when
        webMvcConfig.addInterceptors(registry);

        //then
        verify(registry.addInterceptor(authInterceptor))
            .addPathPatterns("/trainees/**", "/trainers/**", "/users/**", "/trainingTypes/**", "/trainings/**");
    }
}

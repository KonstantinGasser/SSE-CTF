package com.sse.upgrade;

import com.sse.upgrade.security.PasswordAuthenticationProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class UpgradeApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpgradeApplication.class, args);
    }

    @Bean
    PasswordAuthenticationProvider createPasswordAuthenticationProvider() {
        return new PasswordAuthenticationProvider();
    }

//    @Bean
//    public EmbeddedServletContainerCustomizer customizer() {
//        return new EmbeddedServletContainerCustomizer() {
//
//            @Override
//            public void customize(ConfigurableEmbeddedServletContainer container) {
//                if (container instanceof TomcatEmbeddedServletContainerFactory) {
//                    customizeTomcat((TomcatEmbeddedServletContainerFactory) container);
//                }
//            }
//
//            private void customizeTomcat(TomcatEmbeddedServletContainerFactory tomcat) {
//                tomcat.addContextCustomizers(new TomcatContextCustomizer() {
//
//                    @Override
//                    public void customize(Context context) {
//                        Wrapper defServlet = (Wrapper) context.findChild("default");
//                        defServlet.addInitParameter("listings", "true");
//                    }
//                });
//            }
//        };
//    }
}

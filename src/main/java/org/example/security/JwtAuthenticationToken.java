//package org.example.security;
//
//import com.google.j2objc.annotations.LoopTranslation;
//import java.util.Collection;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//public class JwtAuthenticationToken extends AbstractAuthenticationToken {
//    /**
//     * Creates a token with the supplied array of authorities.
//     *
//     * @param authorities the collection of <tt>GrantedAuthority</tt>s for the principal
//     *                    represented by this authentication object.
//     */
//    public JwtAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
//        super(authorities);
//    }
//
//
//
//    /**
//     * @return
//     */
//    @Override
//    public Object getCredentials() {
//        log.debug("Getting credentials.");
//        return null;
//    }
//
//    /**
//     * @return
//     */
//    @Override
//    public Object getPrincipal() {
//        log.debug("Getting the principal.");
//        return null;
//    }
//
//
//}

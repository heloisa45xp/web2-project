package br.edu.ifto.pwebII.config;

import org.springframework.security.core.Authentication;

/**
 * Apoio aos controllers para decidir o destino apos uma operacao.
 *
 * As listagens exigem o perfil ADMIN. Por isso, o ADMIN volta para a lista
 * correspondente e os demais usuarios (inclusive o anonimo) voltam para a home,
 * evitando um redirecionamento que resultaria em 403.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean isAdmin(Authentication authentication) {
        return authentication != null
                && authentication.getAuthorities().stream()
                        .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }

    public static String redirectAfterOperation(Authentication authentication, String listUrl) {
        return isAdmin(authentication) ? "redirect:" + listUrl : "redirect:/";
    }
}

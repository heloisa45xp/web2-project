package br.edu.ifto.pwebII.config;

import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilitario de seguranca para verificar perfis e definir o destino
 * adequado apos operacoes (evitando redirecionamentos que resultariam em 403).
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static boolean hasRole(Authentication authentication, String role) {
        return hasAnyRole(authentication, role);
    }

    public static boolean hasAnyRole(Authentication authentication, String... roles) {
        if (authentication == null || roles == null) {
            return false;
        }

        Set<String> roleSet = Arrays.stream(roles)
                .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
                .collect(Collectors.toSet());

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> roleSet.contains(authority.getAuthority()));
    }

    public static boolean isAdmin(Authentication authentication) {
        return hasRole(authentication, "ADMIN");
    }

    /**
     * Decide o redirecionamento apos criar, editar ou excluir:
     * - Lista de medicos (/medico/list): liberada para ADMIN.
     * - Lista de pacientes (/paciente/list): liberada para ADMIN, SECRETARIO e MEDICO.
     * - Lista de consultas (/consulta/list): liberada para ADMIN, SECRETARIO e MEDICO.
     * - Para perfis sem acesso a listagem (ex.: PACIENTE), redireciona para a pagina inicial.
     */
    public static String redirectAfterOperation(Authentication authentication, String listUrl) {
        if (authentication == null) {
            return "redirect:/";
        }

        if (listUrl.contains("/medico/list")) {
            return hasRole(authentication, "ADMIN") ? "redirect:" + listUrl : "redirect:/";
        }

        if (hasAnyRole(authentication, "ADMIN", "SECRETARIO", "MEDICO")) {
            return "redirect:" + listUrl;
        }

        return "redirect:/";
    }
}

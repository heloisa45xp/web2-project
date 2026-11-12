package br.edu.ifto.pwebII.config;

import br.edu.ifto.pwebII.model.entity.Usuario;
import br.edu.ifto.pwebII.model.jdbc.repository.RepositorioUsuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Autenticacao com credenciais armazenadas no banco de dados.
 *
 * Implementa UserDetailsService para carregar o usuario e seus perfis (roles)
 * a partir do RepositorioUsuario.
 */
@Service
@Transactional(readOnly = true)
public class UsuarioDetailsService implements UserDetailsService {

    private final RepositorioUsuario repositorioUsuario;

    public UsuarioDetailsService(RepositorioUsuario repositorioUsuario) {
        this.repositorioUsuario = repositorioUsuario;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = repositorioUsuario.buscarPorUsuario(username);

        if (usuario == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        return usuario;
    }
}

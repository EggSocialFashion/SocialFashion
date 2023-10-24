package proyecto.socialfashion.SeguridadWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import proyecto.socialfashion.Servicios.UsuarioServicio;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {
    
    @Autowired
    public UsuarioServicio usuarioServicio;
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(usuarioServicio)
                .passwordEncoder(new BCryptPasswordEncoder());
               
    }
  
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                        .antMatchers("/admin/*").hasRole("ADMIN")
                        .antMatchers("/css/*", "/js/*", "/img/*", "/**")
                        .permitAll()
                .and().formLogin()
                        .loginPage("/usuarios/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/publicacionesSocialFashion")
                        .permitAll()
                .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/usuarios/login")
                        .permitAll()
                .and().csrf()
                        .disable();
                

    }
}
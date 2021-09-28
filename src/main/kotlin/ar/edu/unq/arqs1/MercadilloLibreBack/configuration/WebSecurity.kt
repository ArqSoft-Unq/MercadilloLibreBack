package ar.edu.unq.arqs1.MercadilloLibreBack.configuration

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurity : WebSecurityConfigurerAdapter(false) {

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
                .antMatchers("/public/**/*").permitAll()
                .anyRequest().authenticated().and()
                .formLogin()
                .permitAll()
                .and()
                .logout()
                .permitAll()
    }
}
package ar.edu.unq.arqs1.MercadilloLibreBack.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.authentication.HttpStatusEntryPoint

@Configuration
@EnableWebSecurity
class WebSecurity : WebSecurityConfigurerAdapter(false) {

    override fun configure(http: HttpSecurity) {
        http
            .httpBasic().disable()
            .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/v1/**").permitAll()
                .anyRequest().authenticated()
            .and()
            .exceptionHandling().authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            .and().csrf().disable()
    }
}
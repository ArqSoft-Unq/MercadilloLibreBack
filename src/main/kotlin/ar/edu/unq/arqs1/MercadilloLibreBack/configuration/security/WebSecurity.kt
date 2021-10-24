package ar.edu.unq.arqs1.MercadilloLibreBack.configuration.security

import ar.edu.unq.arqs1.MercadilloLibreBack.security.JwtTokenFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.servlet.http.HttpServletResponse


@Configuration
@EnableWebSecurity
class WebSecurity : WebSecurityConfigurerAdapter(true) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Autowired
    lateinit var jwtTokenFilter: JwtTokenFilter

    override fun configure(http: HttpSecurity) {
        http
            .cors().and().csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().exceptionHandling().authenticationEntryPoint { _, response, ex ->
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    ex.message
                )
            }
            .and().authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/manage/**").permitAll()
                .antMatchers("/v1/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/**").permitAll()
                .antMatchers(HttpMethod.POST, "/v1/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/v1/**").permitAll()
                .antMatchers(HttpMethod.DELETE, "/v1/**").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/v1/**").permitAll()
            .anyRequest().authenticated().and().anonymous()
            .and().addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun corsFilter(): CorsFilter? {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*")
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }
}
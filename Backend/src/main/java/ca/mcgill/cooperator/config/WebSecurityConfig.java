package ca.mcgill.cooperator.config;

import ca.mcgill.cooperator.jwt.JwtAuthenticationEntryPoint;
import ca.mcgill.cooperator.jwt.JwtAuthenticationProvider;
import ca.mcgill.cooperator.jwt.JwtRequestFilter;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("ca.mcgill.cooperator.jwt")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired private UserDetailsService jwtUserDetailsService;

    @Autowired private JwtRequestFilter jwtRequestFilter;
    //
    //	@Autowired
    //	private JwtAuthenticationProvider jwtAuthenticationProvider;

    //	@Autowired
    //	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    //
    //		auth.auth
    //	}

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        auth.parentAuthenticationManager(authenticationManagerBean())
                .userDetailsService(jwtUserDetailsService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return new ProviderManager(
                Arrays.asList((AuthenticationProvider) new JwtAuthenticationProvider()));
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // enable anonymous access
                .anonymous()
                .and()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                // make sure we use stateless session; session won't be used to
                // store user's state.
                //				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                //				.and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    public void configure(WebSecurity webSecurity) {
        // temporary while McGill VPN blocks internet-bound traffic
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String securityEnabled = dotenv.get("SECURITY_ENABLED");

        if (securityEnabled != null && securityEnabled.toLowerCase().equals("yes")) {
            webSecurity
                    // don't authenticate these routes
                    .ignoring()
                    .antMatchers("/auth/**");
        } else {
            webSecurity
                    // allow access to all routes
                    .ignoring()
                    .antMatchers("**");
        }
    }
}

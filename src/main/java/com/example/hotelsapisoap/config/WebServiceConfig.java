package com.example.hotelsapisoap.config;

import java.util.Properties;

import com.example.hotelsapisoap.exception.BadRequestException;
import com.example.hotelsapisoap.exception.NotFoundException;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.example.hotelsapisoap.exception.DetailSoapFaultDefinitionExceptionResolver;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext){
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/ws/*");
    }

    @Bean(name = "hotels")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema hotelsSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("HotelPort");
        wsdl11Definition.setTargetNamespace("http://hotels.com/soap");
        wsdl11Definition.setLocationUri("/ws");
        wsdl11Definition.setSchema(hotelsSchema);
        return wsdl11Definition;
    }


    @Bean
    public XsdSchema hotelsSchema(){
        return new SimpleXsdSchema(new ClassPathResource("hotels.xsd"));
    }

    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(NotFoundException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(BadRequestException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }
}

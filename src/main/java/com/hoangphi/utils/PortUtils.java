package com.hoangphi.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.net.InetAddress;

@Component
@RequiredArgsConstructor
public class PortUtils {
    private final  Environment environment;

    private String port;

    public String getPort(){
        if(port==null)
            port=environment.getProperty("local.server.port");
        return port;
    }
    public String getServerUrlPrefix(){
        return "http://"+ InetAddress.getLoopbackAddress().getHostName()+":"+getPort();
    }
    public String getUrlImage(String name)
    {
        if(Validate.isUrl(name)){
            return name;
        }
        return getServerUrlPrefix()+"/images/"+name;
    }
    public String getUrlImage(String name, String pathName) {
        // check if name is url return name
        if (Validate.isUrl(name)) {
            return name;
        }
        return getServerUrlPrefix() + "/images/" + pathName + "/" + name;
    }





}

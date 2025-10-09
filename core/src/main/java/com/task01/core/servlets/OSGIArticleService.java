package com.task01.core.servlets;

import org.osgi.service.component.annotations.Component;

@Component(service = OSGIArticleService.class, immediate = true)
public class OSGIArticleService {

    // String name = "Sai Shyam";

    public static String userName() {
        return "Response is Comming inside the OSGIArticleService";
    }

}

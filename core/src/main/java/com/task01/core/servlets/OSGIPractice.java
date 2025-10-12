package com.task01.core.servlets;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true)
public class OSGIPractice {
    private static final Logger log = LoggerFactory.getLogger(OSGIPractice.class);
    String name = "Shyam";
    @Reference
    OSGIArticleService oSGIArticleService;

    @Activate
    public void activate() {
        if (name != null) {
            log.info("Curse is Inside the Dectivate Method");
            log.info("Response {}", oSGIArticleService.userName());
        }
    }

    @Deactivate
    public static void deactivate() {
        log.info("Curse Inside the deactivate Method");
    }

    @Modified
    public static void modified() {
        log.info("Curse Comes inside the Modified");
    }

}

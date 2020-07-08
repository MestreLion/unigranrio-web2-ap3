package com.rodrigosilva.ap3.view.support;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;

/* Needed for CDI @Inject of FacesContext in LocaleBean */

@ApplicationScoped
@FacesConfig
public class FaceConfig {
}

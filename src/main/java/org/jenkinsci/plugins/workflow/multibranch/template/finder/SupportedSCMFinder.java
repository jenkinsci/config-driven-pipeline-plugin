package org.jenkinsci.plugins.workflow.multibranch.template.finder;

import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Finder to return only supported SCMs
 */
public class SupportedSCMFinder {

    public static Collection<? extends SCMDescriptor<?>> getSupportedSCMs() {
        List<SCMDescriptor<?>> list = new ArrayList<>();
        for (SCMDescriptor<?> scmDescriptor : SCM.all()) {
            // It doesn't really make sense to have the None SCM per the spirit of this plugin.
            if (!scmDescriptor.getDisplayName().equals("None")) {
                list.add(scmDescriptor);
            }
        }
        return list;
    }
}

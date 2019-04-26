package org.jenkinsci.plugins.workflow.multibranch.template;

import hudson.Extension;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import jenkins.branch.MultiBranchProjectFactoryDescriptor;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowMultiBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.WorkflowMultiBranchProject;
import org.jenkinsci.plugins.workflow.multibranch.template.finder.SupportedSCMFinder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.util.Collection;

import static org.jenkinsci.plugins.workflow.multibranch.template.ConfigDrivenWorkflowBranchProjectFactory.USER_DEFINITION_PATH;

/**
 * Defines organization folders by {@link WorkflowBranchProjectFactory}.
 */
public class ConfigDrivenWorkflowMultiBranchProjectFactory extends AbstractWorkflowMultiBranchProjectFactory {

    private String scriptPath = USER_DEFINITION_PATH;
    private SCM jenkinsFileScm = null;

    public Object readResolve() {
        if (this.scriptPath == null) {
            this.scriptPath = USER_DEFINITION_PATH;
        }
        return this;
    }

    @DataBoundSetter
    public void setScriptPath(String scriptPath) {
        if (StringUtils.isEmpty(scriptPath)) {
            this.scriptPath = USER_DEFINITION_PATH;
        } else {
            this.scriptPath = scriptPath;
        }
    }

    public String getScriptPath() { return scriptPath; }


    public SCM getJenkinsFileScm() {
        return jenkinsFileScm;
    }

    @DataBoundSetter
    public void setJenkinsFileScm(SCM jenkinsFileScm) {
        this.jenkinsFileScm = jenkinsFileScm;
    }

    @DataBoundConstructor
    public ConfigDrivenWorkflowMultiBranchProjectFactory() {}

    @Override protected SCMSourceCriteria getSCMSourceCriteria(SCMSource source) {
        return newProjectFactory().getSCMSourceCriteria(source);
    }

    private ConfigDrivenWorkflowBranchProjectFactory newProjectFactory() {
        ConfigDrivenWorkflowBranchProjectFactory workflowBranchProjectFactory = new ConfigDrivenWorkflowBranchProjectFactory();
        workflowBranchProjectFactory.setScriptPath(scriptPath);
        workflowBranchProjectFactory.setJenkinsFileScm(jenkinsFileScm);
        return workflowBranchProjectFactory;
    }

    @Extension
    public static class DescriptorImpl extends MultiBranchProjectFactoryDescriptor {

        @Override public ConfigDrivenWorkflowMultiBranchProjectFactory newInstance() {
            return new ConfigDrivenWorkflowMultiBranchProjectFactory();
        }

        @Override public String getDisplayName() {
            return Messages.ProjectRecognizer_DisplayName();
        }

        public Collection<? extends SCMDescriptor<?>> getApplicableDescriptors() {
            return SupportedSCMFinder.getSupportedSCMs();
        }
    }

    protected void customize(WorkflowMultiBranchProject project) throws IOException, InterruptedException {
        project.setProjectFactory(newProjectFactory());
    }
}

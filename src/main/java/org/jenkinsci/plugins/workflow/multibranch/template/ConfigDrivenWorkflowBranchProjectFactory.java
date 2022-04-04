package org.jenkinsci.plugins.workflow.multibranch.template;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.TaskListener;
import hudson.scm.SCM;
import hudson.scm.SCMDescriptor;
import jenkins.scm.api.SCMProbeStat;
import jenkins.scm.api.SCMSource;
import jenkins.scm.api.SCMSourceCriteria;
import org.apache.commons.lang.StringUtils;
import org.jenkinsci.plugins.workflow.flow.FlowDefinition;
import org.jenkinsci.plugins.workflow.multibranch.AbstractWorkflowBranchProjectFactory;
import org.jenkinsci.plugins.workflow.multibranch.template.finder.SupportedSCMFinder;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.io.IOException;
import java.util.Collection;

public class ConfigDrivenWorkflowBranchProjectFactory extends AbstractWorkflowBranchProjectFactory {
    // TODO: Make this a parameter that users can adjust to their liking
    public static final String USER_DEFINITION_PATH = ".yourconfig.yml";
    public static final String USER_DEFINITION_PIPELINE_PATH = "Jenkinsfile";
    public static final String PIPELINE_TEMPLATE = "pipeline_template";

    private String scriptPath = USER_DEFINITION_PATH;
    private String pipelinePath = USER_DEFINITION_PIPELINE_PATH;
    private SCM jenkinsFileScm = null;

    public Object readResolve() {
        if (this.scriptPath == null) {
            this.scriptPath = USER_DEFINITION_PATH;
        }
        if (this.pipelinePath == null) {
            this.pipelinePath = USER_DEFINITION_PIPELINE_PATH;
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

    @DataBoundSetter
    public void setPipelinePath(String pipelinePath) {
        if (StringUtils.isEmpty(pipelinePath)) {
            this.pipelinePath = USER_DEFINITION_PIPELINE_PATH;
        } else {
            this.pipelinePath = pipelinePath;
        }
    }

    public String getScriptPath() { return scriptPath; }

    public String getPipelinePath() { return pipelinePath; }

    public SCM getJenkinsFileScm() {
        return jenkinsFileScm;
    }

    @DataBoundSetter
    public void setJenkinsFileScm(SCM jenkinsFileScm) {
        this.jenkinsFileScm = jenkinsFileScm;
    }

    @DataBoundConstructor
    public ConfigDrivenWorkflowBranchProjectFactory() {}

    @Override protected FlowDefinition createDefinition() {
        // This creates the CpsScmFlowDefinition... create a new type of "binder"???
        // We need a non-hardcoded version of this class... it does almost everything we want already...
        return new ConfigFileSCMBinder(scriptPath, pipelinePath, jenkinsFileScm);
    }

    @Override protected SCMSourceCriteria getSCMSourceCriteria(SCMSource source) {
        return new SCMSourceCriteria() {
            @Override public boolean isHead(@NonNull SCMSourceCriteria.Probe probe, @NonNull TaskListener listener) throws IOException {
                SCMProbeStat stat = probe.stat(scriptPath);
                switch (stat.getType()) {
                    case NONEXISTENT:
                        if (stat.getAlternativePath() != null) {
                            listener.getLogger().format("      ‘%s’ not found (but found ‘%s’, search is case sensitive)%n", scriptPath, stat.getAlternativePath());
                        } else {
                            listener.getLogger().format("      ‘%s’ not found%n", scriptPath);
                        }
                        return false;
                    case DIRECTORY:
                        listener.getLogger().format("      ‘%s’ found but is a directory not a file%n", scriptPath);
                        return false;
                    default:
                        listener.getLogger().format("      ‘%s’ found%n", scriptPath);
                        return true;

                }
            }

            @Override
            public int hashCode() {
                return getClass().hashCode();
            }

            @Override
            public boolean equals(Object obj) {
                return getClass().isInstance(obj);
            }
        };
    }

    @Extension
    public static class DescriptorImpl extends AbstractWorkflowBranchProjectFactory.AbstractWorkflowBranchProjectFactoryDescriptor {
        // This should update the UI
        @Override public String getDisplayName() {
            return "by " + Messages.ProjectRecognizer_DisplayName();
        }

        public Collection<? extends SCMDescriptor<?>> getApplicableDescriptors() {
            return SupportedSCMFinder.getSupportedSCMs();
        }
    }

}
